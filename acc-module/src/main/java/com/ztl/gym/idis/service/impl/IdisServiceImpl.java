package com.ztl.gym.idis.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.redis.RedisCache;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.spring.SpringUtils;
import com.ztl.gym.idis.domain.IdisRecord;
import com.ztl.gym.idis.domain.vo.IdisCodeParam;
import com.ztl.gym.idis.domain.vo.IdisCodeParamValue;
import com.ztl.gym.idis.domain.vo.IdisRespBody;
import com.ztl.gym.idis.mapper.IdisMapper;
import com.ztl.gym.idis.prop.IdisProp;
import com.ztl.gym.idis.service.IIdisRecordService;
import com.ztl.gym.idis.service.IIdisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class IdisServiceImpl implements IIdisService {

    private static final String IDIS_TOKEN_CACHE_KEY = "idis:token";

    @Autowired
    private IdisProp idisProp;

    @Autowired
    private IdisMapper idisMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IIdisRecordService idisRecordService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer syncCode(Integer maxNum) {
        // 查询需要同步的标识
        List<Map<String, String>> codeList = idisMapper.selectCodeNotSynced(maxNum);
        if (CollectionUtil.isEmpty(codeList)) {
            log.info("没有需要同步的标识");
            return 0;
        }

        // 开始多线程同步
        List<CompletableFuture<IdisRecord>> recordFutureList =
                codeList.stream().map(code -> SpringUtils.getAopProxy(this).doOnceSyncCode(code)).collect(Collectors.toList());

        // 等待多线程全部结束
        List<IdisRecord> recordList = recordFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());

        // 批量记录请求日志
        idisRecordService.batchWriteIdisRecord(recordList);

        // 更新成功同步的码的状态
        List<String> successCodeList =
                recordList.stream().filter(r -> "1".equals(r.getRespCode())).map(IdisRecord::getCode).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(successCodeList)) {
            idisMapper.updateStatusForSyncedCode(successCodeList);
        }

        return codeList.size();
    }

    /**
     * {@link Async} 注解拿掉了, 多线程下调用企业节点API, 企业节点行为异常 <br>
     * 例如 Connection refused, general error 但实则同步成功 <br>
     * 因为企业节点不稳定, 所以暂时单线程跑
     */
    @Override
    public CompletableFuture<IdisRecord> doOnceSyncCode(Map<String, String> codeInfo) {
        // 获取token
        String token = "Bearer " + getIdisToken();
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

    private synchronized String getIdisToken() {
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

}
