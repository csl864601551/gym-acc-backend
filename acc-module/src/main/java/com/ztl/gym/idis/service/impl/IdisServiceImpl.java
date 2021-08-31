package com.ztl.gym.idis.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.redis.RedisCache;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.spring.SpringUtils;
import com.ztl.gym.idis.domain.IdisRecord;
import com.ztl.gym.idis.domain.vo.IdisCodeParam;
import com.ztl.gym.idis.domain.vo.IdisCodeParamValue;
import com.ztl.gym.idis.domain.vo.IdisRespBody;
import com.ztl.gym.idis.mapper.IdisMapper;
import com.ztl.gym.idis.model.result.IdisBatchResult;
import com.ztl.gym.idis.model.result.IdisResult;
import com.ztl.gym.idis.prop.IdisProp;
import com.ztl.gym.idis.service.IdisRecordService;
import com.ztl.gym.idis.service.IdisService;
import com.ztl.gym.idis.util.IDISConst;
import com.ztl.gym.idis.util.OkHttpCli;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * IDIS同步Service业务层处理
 *
 * @author zt_sly
 * @date 2021-07-21
 */
@Slf4j
@Service
public class IdisServiceImpl implements IdisService {

    private static final String IDIS_TOKEN_CACHE_KEY = "idis:token";


    @Autowired
    private IdisMapper idisMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IdisRecordService idisRecordService;

    @Autowired
    private OkHttpCli okHttpCli;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer syncCode(Integer maxNum, IdisProp idisProp) {


        // 查询需要同步的标识
        List<Map<String, String>> codeList = idisMapper.selectCodeNotSynced(maxNum, idisProp.getCompanyId());
        if (CollectionUtil.isEmpty(codeList)) {
            log.info("没有需要同步的标识");
            return 0;
        }

        // 开始多线程同步
        List<CompletableFuture<IdisRecord>> recordFutureList =
                codeList.stream().map(code -> SpringUtils.getAopProxy(this).doOnceSyncCode(code, idisProp)).collect(Collectors.toList());

        // 等待多线程全部结束
        List<IdisRecord> recordList = recordFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());

        // 批量记录请求日志
        idisRecordService.batchWriteIdisRecord(recordList);

        // 更新成功同步的码的状态
        List<String> successCodeList =
                recordList.stream().filter(r -> "1".equals(r.getRespCode())).map(IdisRecord::getCode).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(successCodeList)) {
            idisMapper.updateStatusForSyncedCode(successCodeList, idisProp.getCompanyId());
        }

        return codeList.size();
    }

    /**
     * {@link Async} 注解拿掉了, 多线程下调用企业节点API, 企业节点行为异常 <br>
     * 例如 Connection refused, general error 但实则同步成功 <br>
     * 因为企业节点不稳定, 所以暂时单线程跑
     */
    @Override
    public CompletableFuture<IdisRecord> doOnceSyncCode(Map<String, String> codeInfo, IdisProp idisProp) {
        // 获取token
        String token = "Bearer " + getIdisToken(idisProp);
        log.debug("token = {}", token);

        // 构造请求体
        List<IdisCodeParam> codeParamList = new ArrayList<>();
        idisProp.getAttrList().forEach(attr ->
                codeParamList.add(new IdisCodeParam(attr, new IdisCodeParamValue(codeInfo.getOrDefault(attr, "")))));
        Map<String, Object> mapBody = new HashMap<>(1);
        mapBody.put("value", codeParamList);
        String jsonReqBody = JSONUtil.toJsonStr(mapBody);
        log.debug("jsonReqBody = {}", jsonReqBody);

        // 构造URL
        String code = codeInfo.get("CODE");
        String idisCode = StrUtil.format("{}/{}", idisProp.getPrefix(), code);
        String url = URLUtil.encode(StrUtil.format("http://{}:{}/{}", idisProp.getHost(), idisProp.getPort(), idisCode));
        log.debug("url = {}", url);

        IdisRecord.IdisRecordBuilder record =
                IdisRecord.builder().type("创建标识").code(code).url(url).param(jsonReqBody);

        // 发送创建标识请求
        HttpResponse resp;
        try {
            resp = HttpUtil.createPost(url)
                    .header(Header.AUTHORIZATION, token)
                    .body(jsonReqBody, ContentType.JSON.getValue())
                    .execute();
        } catch (Exception e) {
            log.error("请求idis创建标识接口失败", e);
            return CompletableFuture.completedFuture(record.respMsg(e.getMessage()).build());
        }

        // 解析响应体
        if (resp.isOk()) {
            IdisRespBody respBody = JSONUtil.parse(resp.body()).toBean(IdisRespBody.class);
            if (!respBody.isSuccess()) {
                log.error("请求idis创建标识接口失败, 错误信息: {}", respBody.getMsg());
            }
            return CompletableFuture.completedFuture(
                    record.respCode(respBody.getResponseCode()).respMsg(respBody.getMsg()).build());
        } else {
            String errMsg = StrUtil.format("请求idis创建标识接口失败, 错误码: {}", resp.getStatus());
            log.error(errMsg);
            return CompletableFuture.completedFuture(record.respMsg(errMsg).build());
        }
    }

    private synchronized String getIdisToken(IdisProp idisProp) {
        // 从缓存中读取token
        String cacheToken = redisCache.getCacheObject(IDIS_TOKEN_CACHE_KEY);
        if (StrUtil.isNotBlank(cacheToken)) {
            return cacheToken;
        }

        // 缓存中没有token, 再次登录获取
        Map<String, Object> mapReqBody = new HashMap<>(2);
        mapReqBody.put("user", idisProp.getUser());
        mapReqBody.put("passwd", idisProp.getPwd());
        String jsonReqBody = JSONUtil.toJsonStr(mapReqBody);
        log.debug("jsonReqBody = {}", jsonReqBody);

        // 构造URL
        String url = URLUtil.encode(StrUtil.format("http://{}:{}/login", idisProp.getHost(), idisProp.getPort()));
        log.debug("url = {}", url);

        IdisRecord.IdisRecordBuilder record = IdisRecord.builder().type("登录").url(url);

        // 发送登录请求
        HttpResponse resp;
        try {
            resp = HttpUtil.createPost(url).body(jsonReqBody, ContentType.JSON.getValue()).execute();
        } catch (Exception e) {
            idisRecordService.writeIdisRecord(record.respMsg(e.getMessage()).build());
            throw new CustomException("请求idis登录接口失败", e);
        }

        // 解析响应体
        if (resp.isOk()) {
            IdisRespBody respBody = JSONUtil.parse(resp.body()).toBean(IdisRespBody.class);
            idisRecordService.writeIdisRecord(record.respCode(respBody.getResponseCode()).respMsg(respBody.getMsg()).build());

            if (respBody.isSuccess()) {
                // 成功拿到token, 写入缓存
                String token = respBody.getToken();
                // IDIS token 过期时间是 1hour, 所以自身缓存设置 30min
                redisCache.setCacheObject(IDIS_TOKEN_CACHE_KEY, token, 30, TimeUnit.MINUTES);
                return token;
            } else {
                throw new CustomException(
                        StrUtil.format("请求idis登录接口失败, 错误信息: {}", respBody.getMsg()), HttpStatus.ERROR);
            }
        } else {
            String errMsg = StrUtil.format("请求idis登录接口失败, 错误码: {}", resp.getStatus());
            idisRecordService.writeIdisRecord(record.respMsg(errMsg).build());
            throw new CustomException(errMsg, HttpStatus.ERROR);
        }
    }

    /**
     * 托管
     */

    @Override
    public void release(IdisProp idisProp) {
        //获取token
        JSONObject jsonObject = new JSONObject();
        String username = idisProp.getUser();
        String password = idisProp.getPwd();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        String json = jsonObject.toJSONString();
        String idisTokenObj = okHttpCli.doPostJson(IDISConst.URL_TOKEN, json);
        String idisToken = JSON.parseObject(JSON.parseObject(idisTokenObj).getString("data")).getString("token");
        //查询模板
        Map<String, String> map = new HashMap<>();
        map.put("prefix", idisProp.getPrefix());//前缀
        map.put("version", "V1.0.0");//版本号
        String retStr = okHttpCli.doGetWithBearerToken(IDISConst.URL_TEMPLATE_DETAIL, map, idisToken);
        JSONObject ret = JSON.parseObject(retStr);
        // 整合获取模板名称与index对应关系
        Map<String, Object> indexMap = new HashMap<String, Object>();//模板
        if ((Integer) ret.get("status") == 1) {
            JSONObject data = (JSONObject) ret.get("data");

            JSONArray items = (JSONArray) ((JSONObject) ret.get("data")).get("items");
            indexMap = new HashMap<String, Object>();//模板
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                indexMap.put(item.getString("name"),
                        item.getInteger("idIndex") + IDISConst.SPLITE_INDEX_TYPE + item.getString("idType"));
            }
        } else {
            if (null == ret.get("data")) {
                log.info("获取数据模板：prefix={}, templateVersion={}, 为空", idisProp.getPrefix(), idisProp.getVersion());
                //没有模板，则创建模板

                Map<String, Object> metadata = new HashMap<String, Object>();//模板数据限制
                metadata.put("minLength", 0);
                metadata.put("type", "String");
                metadata.put("maxLength", 10000);

                Map<String, Object> item1 = new HashMap<String, Object>();//模板内容
                item1.put("name", "产品名称");
                item1.put("idType", "productName");
                item1.put("metadata", metadata);
                Map<String, Object> item2 = new HashMap<String, Object>();//模板内容
                item2.put("name", "产品编号");
                item2.put("idType", "productNo");
                item2.put("metadata", metadata);
                Map<String, Object> item3 = new HashMap<String, Object>();//模板内容
                item3.put("name", "码");
                item3.put("idType", "code");
                item3.put("metadata", metadata);
                JSONArray items = new JSONArray();//模板内容汇总
                items.add(item1);
                items.add(item2);
                items.add(item3);

                indexMap = new HashMap<String, Object>();//模板
                indexMap.put("prefix", idisProp.getPrefix());//前缀
                indexMap.put("version", "V1.0.0");//版本号
                indexMap.put("items", items);//版本号

                String jsonTemp = JSON.toJSON(indexMap).toString();
                log.info(jsonTemp);
                String retStrTemp = okHttpCli.doPostJsonWithBearerToken(IDISConst.URL_TEMPLATE_ADD, jsonTemp, idisToken);


            }
        }

        //同步标识
        JSONArray items = new JSONArray();//标识内容汇总
        Map<String, Object> codeData = null;//每条标识记录
        JSONArray codeJson = null;//每条标识属性记录
        Map<String, Object> codeMap = null;//每条标识属性map
        Map<String, Object> dataMap = null;//每条标识属性数据map
        for (int i = 0; i < idisProp.getCodeList().size(); i++) {
            //产品名称
            dataMap=new HashMap<String, Object>();
            dataMap.put("format","string");
            dataMap.put("value",idisProp.getCodeList().get(i).getProductName());
            codeMap=new HashMap<String, Object>();
            codeMap.put("index",2000);
            codeMap.put("type","productName");
            codeMap.put("data",dataMap);
            codeJson = new JSONArray();//标识内容汇总
            codeJson.add(codeMap);
            //产品编号
            dataMap=new HashMap<String, Object>();
            dataMap.put("format","string");
            dataMap.put("value",idisProp.getCodeList().get(i).getProductNo());
            codeMap=new HashMap<String, Object>();
            codeMap.put("index",2001);
            codeMap.put("type","productNo");
            codeMap.put("data",dataMap);
            codeJson.add(codeMap);
            //码
            dataMap=new HashMap<String, Object>();
            dataMap.put("format","string");
            dataMap.put("value",idisProp.getCodeList().get(i).getCode());
            codeMap=new HashMap<String, Object>();
            codeMap.put("index",2002);
            codeMap.put("type","code");
            codeMap.put("data",dataMap);
            codeJson.add(codeMap);

            codeData = new HashMap<String, Object>();
            codeData.put("handle", idisProp.getPrefix()+"/"+idisProp.getCodeList().get(i).getCode());//标识
            codeData.put("templateVersion", "V1.0.0");//数据模板的产品型号
            codeData.put("value", codeJson);//标识属性
            items.add(codeData);

        }

        String jsonList = JSON.toJSONString(items);
        log.info(jsonList);
        String retStrCode = okHttpCli.doPostJsonWithBearerToken(IDISConst.URL_DATA_ADD_BATCH, jsonList, idisToken);
        IdisResult<List<IdisBatchResult>> res = JSON.parseObject(retStrCode, new TypeReference<IdisResult<List<IdisBatchResult>>>() {
        });
        log.info("同步标识", res);

    }


}
