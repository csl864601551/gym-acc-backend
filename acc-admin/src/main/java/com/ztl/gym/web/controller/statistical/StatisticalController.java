package com.ztl.gym.web.controller.statistical;

import cn.hutool.core.date.DateUtil;
import com.ztl.gym.code.service.ICodeAccRecordService;
import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.code.service.ICodeSingleService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.ConversionUtill;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.mix.service.IMixRecordService;
import com.ztl.gym.payment.domain.PurchaseRecord;
import com.ztl.gym.payment.service.IPaymentRecordService;
import com.ztl.gym.payment.service.IPurchaseRecordService;
import com.ztl.gym.product.service.IProductService;
import com.ztl.gym.statistical.domain.StatisticalBean;
import com.ztl.gym.storage.domain.ScanRecord;
import com.ztl.gym.storage.service.IScanRecordService;
import com.ztl.gym.storage.service.IStorageOutService;
import com.ztl.gym.system.service.ISysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author shenz
 * @create 2021-07-10 9:17
 */
@RestController
@RequestMapping("/statistical/statistical")
public class StatisticalController {

    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(StatisticalController.class);

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IStorageOutService storageOutService;

    @Autowired
    private IMixRecordService mixRecordService;

    @Autowired
    private ICodeRecordService codeRecordService;

    @Autowired
    private IProductService tProductService;

    @Autowired
    private IScanRecordService scanRecordService;

    @Autowired
    private ICodeSingleService codeSingleService;

    @Autowired
    private ICodeAccRecordService codeAccRecordService;

    @Autowired
    private IPaymentRecordService paymentRecordService;

    @Autowired
    private IPurchaseRecordService purchaseRecordService;

    /**
     * 查询首页信息
     */
    @PostMapping("/indexSj")
    public AjaxResult selectIndexSj(@RequestBody Map<String,Object> map)
    {
        logger.info("the method getInfoByKey enter, param is {}", map);
        try {
            StatisticalBean statisticalBean = new StatisticalBean();
            boolean isadmin = false;
            Long deptId = 0L;
            //获取用户部门信息
            Long company_id=SecurityUtils.getLoginUserCompany().getDeptId();
            if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
                deptId = SecurityUtils.getLoginUserCompany().getDeptId();
                isadmin = false;
            }else{
                isadmin = true;
            }
            Map<String, Object> query = new HashMap<String, Object>();
            int codeNum = 0;
            int singCodeNum = 0;

            //平台方
            if (isadmin) {
                //企业总数
                query.put("type", 5);
                query.put("topdeptId", AccConstants.ADMIN_DEPT_ID);
                statisticalBean.setQyNum(deptService.selectCountBydept(query));
                //产品出货总量
                query.put("type", null);
                statisticalBean.setCpchlNum(storageOutService.selectCountByDept(query));
                //窜货总量
                statisticalBean.setChzlNum(mixRecordService.selectmixnum(query));
                //生码总量
                query.put("type", 2);
                codeNum = codeRecordService.selectCodeNum(query);
                singCodeNum = codeSingleService.selectSingCodeNum(query);
                statisticalBean.setSmzlNum(codeNum+singCodeNum);
                //累计充值费用
                statisticalBean.setLjczfyNum(paymentRecordService.getAllAmountNum(query));
                //产品总览
                //已下架
                query.put("type", 1);
                statisticalBean.setYxjNum(tProductService.selectProductNum(query));
                //已上架
                query.put("type", 2);
                statisticalBean.setYsjNum(tProductService.selectProductNum(query));
                //库存紧张
                query.put("type", 3);
                statisticalBean.setKcjzNum(tProductService.selectProductNum(query));
                //全部产品
                query.put("type", 4);
                statisticalBean.setQbcpNum(tProductService.selectProductNum(query));

                //经销商总览
                //今日新增
                Date today = DateUtil.date();
                Date beginTime = DateUtil.beginOfDay(today);
                Date endTime = DateUtil.endOfDay(today);
                query.put("type", 1);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setJrxzNum(deptService.selectCountBydept(query));
                //昨日新增
                Date yesterday = DateUtil.yesterday();
                beginTime = DateUtil.beginOfDay(yesterday);
                endTime = DateUtil.endOfDay(yesterday);
                query.put("type", 2);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setZrxzNum(deptService.selectCountBydept(query));
                //本月新增
                beginTime = DateUtil.beginOfMonth(today);
                endTime = DateUtil.endOfMonth(today);
                query.put("type", 3);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setByxzNum(deptService.selectCountBydept(query));
                //经销商总数
                query.put("type", 3);
                statisticalBean.setJxszs(deptService.selectCountBydept(query));

                //出货统计
                //本月出货总数
                query.put("type", 1);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setBychzsNum(storageOutService.selectCountByDept(query));
                //同比上月
                Date lastMonth = DateUtil.lastMonth();
                beginTime = DateUtil.beginOfMonth(lastMonth);
                endTime = DateUtil.endOfMonth(lastMonth);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                int sgyNum = storageOutService.selectCountByDept(query);
                //上个月-这个月的量
                int gapNum = statisticalBean.getBychzsNum()-sgyNum;
                if (sgyNum > 0 && statisticalBean.getBychzsNum()>0) {
                    statisticalBean.setTbsyNum(new BigDecimal(((float) gapNum / sgyNum)*100).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    statisticalBean.setTbsyNum(new BigDecimal("0"));
                }
                //本周出货总数
                beginTime = DateUtil.beginOfWeek(today);
                endTime = DateUtil.endOfWeek(today);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setBzchslNum(storageOutService.selectCountByDept(query));
                //同比上周
                Date lastWeek = DateUtil.lastWeek();
                beginTime = DateUtil.beginOfWeek(lastWeek);
                endTime = DateUtil.endOfWeek(lastWeek);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                int tbszNum = storageOutService.selectCountByDept(query);
                //上周-这周
                int gapWeekNum = statisticalBean.getBzchslNum()-tbszNum;
                if (tbszNum > 0 && statisticalBean.getBzchslNum()>0) {
                    statisticalBean.setTbszNum(new BigDecimal(((float) gapWeekNum/tbszNum )*100).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    statisticalBean.setTbszNum(new BigDecimal("0"));
                }

                //今日生码总量
                beginTime = DateUtil.beginOfDay(today);
                endTime = DateUtil.endOfDay(today);
                query.put("type", 1);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                codeNum = codeRecordService.selectCodeNum(query);
                singCodeNum = codeSingleService.selectSingCodeNum(query);
                statisticalBean.setJrsmNum(codeNum+singCodeNum);
                //累计生码总量
                query.put("type", 2);
                codeNum = codeRecordService.selectCodeNum(query);
                singCodeNum = codeSingleService.selectSingCodeNum(query);
                statisticalBean.setLjsmNum(codeNum+singCodeNum);
                //今日扫码总量
                query.put("type", 1);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setJrsmZl(scanRecordService.selectScanRecordNum(query));
                //总扫码量
                query.put("type", 2);
                statisticalBean.setLjsmZl(scanRecordService.selectScanRecordNum(query));
                //今日查验总量
                query.put("type", 1);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setJrcyNum(scanRecordService.selectSecueityRecordNum(query));
                //总查验量
                query.put("type", 2);
                statisticalBean.setLjcyNum(scanRecordService.selectSecueityRecordNum(query));

            } else {
                query.put("deptId", deptId);
                //经销商总数
                query.put("type", 3);
                statisticalBean.setJxsNum(deptService.selectCountBydept(query));
                //产品出货总量
                query.put("type", null);
                statisticalBean.setCpchlNum(storageOutService.selectCountByDept(query));
                //窜货总量
                statisticalBean.setChzlNum(mixRecordService.selectmixnum(query));
                //剩余码量
                PurchaseRecord purchaseRecord = new PurchaseRecord();
                Map<String, Object> result = purchaseRecordService.getStatistics(purchaseRecord);
                if(result!=null){
                    if(result.get("code")!=null){
                        statisticalBean.setSymlNum(ConversionUtill.ToBigDecimal(result.get("code")).intValue());
                    }else{
                        statisticalBean.setSymlNum(ConversionUtill.ToBigDecimal("0").intValue());
                    }
                    if(result.get("money")!=null){
                        //剩余费用
                        statisticalBean.setSyfyNum(ConversionUtill.ToBigDecimal(result.get("money")));
                    }else{
                        //剩余费用
                        statisticalBean.setSyfyNum(ConversionUtill.ToBigDecimal(0));
                    }
                }

                //产品总览
                //已下架
                query.put("type", 1);
                statisticalBean.setYxjNum(tProductService.selectProductNum(query));
                //已上架
                query.put("type", 2);
                statisticalBean.setYsjNum(tProductService.selectProductNum(query));
                //库存紧张
                query.put("type", 3);
                statisticalBean.setKcjzNum(tProductService.selectProductNum(query));
                //全部产品
                query.put("type", 4);
                statisticalBean.setQbcpNum(tProductService.selectProductNum(query));

                //经销商总览
                //今日新增
                Date today = DateUtil.date();
                Date beginTime = DateUtil.beginOfDay(today);
                Date endTime = DateUtil.endOfDay(today);
                query.put("type", 1);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setJrxzNum(deptService.selectCountBydept(query));
                //昨日新增
                Date yesterday = DateUtil.yesterday();
                beginTime = DateUtil.beginOfDay(yesterday);
                endTime = DateUtil.endOfDay(yesterday);
                query.put("type", 2);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setZrxzNum(deptService.selectCountBydept(query));
                //本月新增
                beginTime = DateUtil.beginOfMonth(today);
                endTime = DateUtil.endOfMonth(today);
                query.put("type", 3);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setByxzNum(deptService.selectCountBydept(query));
                //经销商总数
                query.put("type", 3);
                statisticalBean.setJxszs(deptService.selectCountBydept(query));

                //出货统计
                //本月出货总数
                query.put("type", 1);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setBychzsNum(storageOutService.selectCountByDept(query));
                //同比上月
                Date lastMonth = DateUtil.lastMonth();
                beginTime = DateUtil.beginOfMonth(lastMonth);
                endTime = DateUtil.endOfMonth(lastMonth);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                int sgyNum = storageOutService.selectCountByDept(query);
                //上个月-这个月的量
                int gapNum = statisticalBean.getBychzsNum()-sgyNum;
                if (sgyNum > 0 && statisticalBean.getBychzsNum()>0) {
                    statisticalBean.setTbsyNum(new BigDecimal(((float) gapNum / sgyNum)*100).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    statisticalBean.setTbsyNum(new BigDecimal("0"));
                }
                //本周出货总数
                beginTime = DateUtil.beginOfWeek(today);
                endTime = DateUtil.endOfWeek(today);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setBzchslNum(storageOutService.selectCountByDept(query));
                //同比上周
                Date lastWeek = DateUtil.lastWeek();
                beginTime = DateUtil.beginOfWeek(lastWeek);
                endTime = DateUtil.endOfWeek(lastWeek);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                int tbszNum = storageOutService.selectCountByDept(query);
                //上周-这周
                int gapWeekNum = statisticalBean.getBzchslNum()-tbszNum;
                if (tbszNum > 0 && statisticalBean.getBzchslNum()>0) {
                    statisticalBean.setTbszNum(new BigDecimal(((float) gapWeekNum/tbszNum )*100).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    statisticalBean.setTbszNum(new BigDecimal("0"));
                }
                //今日生码总量
                beginTime = DateUtil.beginOfDay(today);
                endTime = DateUtil.endOfDay(today);
                query.put("type", 1);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                codeNum = codeRecordService.selectCodeNum(query);
                singCodeNum = codeSingleService.selectSingCodeNum(query);
                statisticalBean.setJrsmNum(codeNum+singCodeNum);
                //累计生码总量
                query.put("type", 2);
                codeNum = codeRecordService.selectCodeNum(query);
                singCodeNum = codeSingleService.selectSingCodeNum(query);
                statisticalBean.setLjsmNum(codeNum+singCodeNum);
                //今日扫码总量
                query.put("type", 1);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setJrsmZl(scanRecordService.selectScanRecordNum(query));
                //总扫码量
                query.put("type", 2);
                statisticalBean.setLjsmZl(scanRecordService.selectScanRecordNum(query));
                //今日查验总量
                query.put("type", 1);
                query.put("beginTime", beginTime);
                query.put("endTime", endTime);
                statisticalBean.setJrcyNum(scanRecordService.selectSecueityRecordNum(query));
                //总查验量
                query.put("type", 2);
                statisticalBean.setLjcyNum(scanRecordService.selectSecueityRecordNum(query));
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("data", statisticalBean);
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        } catch (Exception e) {
            System.out.println(e);
            AjaxResult ajax = AjaxResult.error("查询信息错误！！！："+e.getMessage());
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        }
    }


    /**
     * 查询首页信息
     */
    @PostMapping("/selectOutNum")
    public AjaxResult selectOutNum(@RequestBody Map<String,Object> map)
    {
        boolean isadmin = false;
        Long topdeptId = 0L;
        //获取用户部门信息
        Long deptId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!deptId.equals(AccConstants.ADMIN_DEPT_ID)) {
            topdeptId = SecurityUtils.getLoginUserTopCompanyId();
            isadmin = false;
        }else{
            isadmin = true;
        }
        Map<String, Object> query = new HashMap<String, Object>();
        //本月出货总量
        int bychzlnum = 0;
        //本周出货数量
        int bzchsl = 0;
        //平台方
        if(isadmin){
            //本月出货总量
            query.put("type",1);
            bychzlnum = storageOutService.selectCountByDept(query);

        }else{

        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("jxszs", 0);
        return ajax;
    }



    /**
     * 热力图扫码信息
     */
    //@Log(title = "热力图扫码信息", businessType = BusinessType.INSERT)
    @PostMapping("/getRltXx")
    public AjaxResult getRltXx(@RequestBody Map<String,Object> map)
    {
        try {
            List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
            //查询热力图数据
            List<ScanRecord> list = scanRecordService.selectRLTList(map);
            if(list.size()>0){
                for(int i=0;i<list.size();i++){
                    Map<String,Object> rltmap = new HashMap<String,Object>();
                    ScanRecord scanRecord  = list.get(i);
                    rltmap.put("lng",scanRecord.getLongitude());
                    rltmap.put("lat",scanRecord.getLatitude());
                    rltmap.put("count",i);
//                    rltmap.put("code",scanRecord.getCode());
//                    rltmap.put("id",scanRecord.getId());
                    lists.add(rltmap);
                }
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("rltjson", lists);
            return ajax;
        }catch (Exception e){
            AjaxResult ajax = AjaxResult.error("查询信息错误！！！");
            return ajax;
        }
    }



    /**
     * 点聚合扫码信息
     */
    //@Log(title = "点聚合扫码信息", businessType = BusinessType.INSERT)
    @RequestMapping(value = "getDjhXx", method = {RequestMethod.GET,RequestMethod.POST})
    public AjaxResult getDjhXx(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
            Map<String,Object> map = new HashMap<String,Object>();
            //查询热力图数据
            List<ScanRecord> list = scanRecordService.selectRLTList(map);
            if(list.size()>0){
                for(int i=0;i<list.size();i++){
                    Map<String,Object> rltmap = new HashMap<String,Object>();
                    List<String> list1 = new ArrayList<String>();
                    ScanRecord scanRecord  = list.get(i);
                    list1.add(scanRecord.getLongitude());
                    list1.add(scanRecord.getLatitude());
                    rltmap.put("lnglat",list1);
//                    rltmap.put("code",scanRecord.getCode());
//                    rltmap.put("id",scanRecord.getId());
                    lists.add(rltmap);
                }
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("rltjson", lists);
            return ajax;
        }catch (Exception e){
            AjaxResult ajax = AjaxResult.error("查询信息错误！！！");
            return ajax;
        }
    }


    /**
     * 根据码号查询相关产品和码信息
     */
    @RequestMapping(value = "getDjhXxs", method = {RequestMethod.GET,RequestMethod.POST})
    public AjaxResult getDjhXxs(HttpServletRequest request, HttpServletResponse response) {
        //String code = request.getParameter("code");
        //System.out.println("扫码详情进入成功  code=="+code);
//        String compant_id = WxUtil.splitData(code,"-","-");
//        long  companyId = Integer.parseInt(compant_id)/5;
        String temp ="";
        List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("rltjson", lists);
        return ajax;
    }


    /**
     * 扫码记录时间分布图
     */
    @PostMapping("/selectSmjlTop10")
    public AjaxResult selectSmjlTop10(@RequestBody Map<String, Object> map) {
        logger.info("the method getInfoByKey enter, param is {}", map);
        try {
            boolean isadmin = false;
            //获取用户部门信息
            Long deptId = 0L;
            //获取用户部门信息
            Long company_id=SecurityUtils.getLoginUserCompany().getDeptId();
            if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
                deptId = SecurityUtils.getLoginUserCompany().getDeptId();
                isadmin = false;
            }else{
                isadmin = true;
            }
            //折线图数据
            List<Map<String, Object>> smList = new ArrayList<Map<String, Object>>();
            Map<String, Object> query = new HashMap<String, Object>();
            //平台方
            if (isadmin) {
                List<Map<String, Object>> top10List = scanRecordService.selectSmTop10(query);
                if(top10List!=null){
                    for(int i=0;i<top10List.size();i++){
                        Map<String, Object> top10Map = new HashMap<String, Object>();
                        top10Map = top10List.get(i);
                        if(top10Map.get("code")!=null){
                            Map<String,Object> smxxMap =  scanRecordService.getScanRecordByCode(CodeRuleUtils.getCompanyIdByCode(top10Map.get("code").toString().trim()), top10Map.get("code").toString().trim());
                            if(smxxMap!=null){
                                if(smxxMap.get("productName")!=null){
                                    top10Map.put("productName",smxxMap.get("productName"));
                                }
                                top10Map.put("photoShow",smxxMap.get("photoShow"));
                            }
                        }
                        smList.add(top10Map);
                    }
                }

            } else {
                query.put("deptId", deptId);
                List<Map<String, Object>> top10List = scanRecordService.selectSmTop10(query);
                if(top10List!=null){
                    for(int i=0;i<top10List.size();i++){
                        Map<String, Object> top10Map = new HashMap<String, Object>();
                        top10Map = top10List.get(i);
                        if(top10Map.get("code")!=null){
                            Map<String,Object> smxxMap =  scanRecordService.getScanRecordByCode(CodeRuleUtils.getCompanyIdByCode(top10Map.get("code").toString().trim()), top10Map.get("code").toString().trim());
                            if(smxxMap!=null){
                                if(smxxMap.get("productName")!=null){
                                    top10Map.put("productName",smxxMap.get("productName"));
                                }
                                top10Map.put("photoShow",smxxMap.get("photoShow"));
                            }
                        }
                        smList.add(top10Map);
                    }
                }

            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("smList", smList);
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        } catch (Exception e) {
            System.out.println(e);
            AjaxResult ajax = AjaxResult.error("查询信息错误！！！:"+e.getMessage());
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        }
    }

    /**
     * 出货数量趋势图
     */
    @PostMapping("/selectChsjNum")
    public AjaxResult selectChsjNum(@RequestBody Map<String, Object> map) {
        logger.info("the method getInfoByKey enter, param is {}", map);
        try {
            boolean isadmin = false;
            //获取用户部门信息
            Long deptId = 0L;
            //获取用户部门信息
            Long company_id=SecurityUtils.getLoginUserCompany().getDeptId();
            if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
                deptId = SecurityUtils.getLoginUserCompany().getDeptId();
                isadmin = false;
            }else{
                isadmin = true;
            }
            Map<String, Object> query = new HashMap<String, Object>();
            //折线图数据
            List<Object> chsjXlist = new ArrayList<Object>();
            List<Object> chsjYlist = new ArrayList<Object>();
            //平台方
            if (isadmin) {
                //1本周 2本月 3自选时间
                if (map.get("chType") != null) {
                    if(map.get("chType").equals("1")){
                        query.put("type","1");
                        List<Map<String, Object>> chsjList = storageOutService.selectCountByWeek(query);
                        if (chsjList.size() > 0) {
                            for (int i = 0; i < chsjList.size(); i++) {
                                Map<String, Object> chsjmap = new HashMap<String, Object>();
                                chsjmap = chsjList.get(i);
                                chsjXlist.add(chsjmap.get("countKey"));
                                chsjYlist.add(chsjmap.get("countValue"));
                            }
                        }
                    }else if(map.get("chType").equals("2")){
                        query.put("type","2");
                        List<Map<String, Object>> chsjList = storageOutService.selectCountByWeek(query);
                        if (chsjList.size() > 0) {
                            for (int i = 0; i < chsjList.size(); i++) {
                                Map<String, Object> chsjmap = new HashMap<String, Object>();
                                chsjmap = chsjList.get(i);
                                chsjXlist.add(chsjmap.get("countKey"));
                                chsjYlist.add(chsjmap.get("countValue"));
                            }
                        }
                    }else if(map.get("chType").equals("3")){
                        query.put("type","3");
                        if(map.get("beginTime")!=null&&map.get("endTime")!=null){
                            query.put("beginTime", map.get("beginTime"));
                            query.put("endTime", map.get("endTime"));
                        }
                        List<Map<String, Object>> chsjList = storageOutService.selectCountByWeek(query);
                        if (chsjList.size() > 0) {
                            for (int i = 0; i < chsjList.size(); i++) {
                                Map<String, Object> chsjmap = new HashMap<String, Object>();
                                chsjmap = chsjList.get(i);
                                chsjXlist.add(chsjmap.get("countKey"));
                                chsjYlist.add(chsjmap.get("countValue"));
                            }
                        }
                    }
                }
            } else {
                query.put("deptId", deptId);
                if (map.get("chType") != null) {
                    if(map.get("chType").equals("1")){
                        query.put("type","1");
                        List<Map<String, Object>> chsjList = storageOutService.selectCountByWeek(query);
                        if (chsjList.size() > 0) {
                            for (int i = 0; i < chsjList.size(); i++) {
                                Map<String, Object> chsjmap = new HashMap<String, Object>();
                                chsjmap = chsjList.get(i);
                                chsjXlist.add(chsjmap.get("countKey"));
                                chsjYlist.add(chsjmap.get("countValue"));
                            }
                        }
                    }else if(map.get("chType").equals("2")){
                        query.put("type","2");
                        List<Map<String, Object>> chsjList = storageOutService.selectCountByWeek(query);
                        if (chsjList.size() > 0) {
                            for (int i = 0; i < chsjList.size(); i++) {
                                Map<String, Object> chsjmap = new HashMap<String, Object>();
                                chsjmap = chsjList.get(i);
                                chsjXlist.add(chsjmap.get("countKey"));
                                chsjYlist.add(chsjmap.get("countValue"));
                            }
                        }
                    }else if(map.get("chType").equals("3")){
                        query.put("type","3");
                        if(map.get("beginTime")!=null&&map.get("endTime")!=null){
                            query.put("beginTime", map.get("beginTime"));
                            query.put("endTime", map.get("endTime"));
                        }
                        List<Map<String, Object>> chsjList = storageOutService.selectCountByWeek(query);
                        if (chsjList.size() > 0) {
                            for (int i = 0; i < chsjList.size(); i++) {
                                Map<String, Object> chsjmap = new HashMap<String, Object>();
                                chsjmap = chsjList.get(i);
                                chsjXlist.add(chsjmap.get("countKey"));
                                chsjYlist.add(chsjmap.get("countValue"));
                            }
                        }
                    }
                }
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("chsjXlist", chsjXlist);
            ajax.put("chsjYlist", chsjYlist);
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        } catch (Exception e) {
            System.out.println(e);
            AjaxResult ajax = AjaxResult.error("查询信息错误！！！");
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        }
    }

    /**
     * 查询标识统计趋势图
     */
    @PostMapping("/selectSmSmCyNum")
    public AjaxResult selectSmSmCyNum(@RequestBody Map<String, Object> map) {
        logger.info("the method getInfoByKey enter, param is {}", map);
        try {
            boolean isadmin = false;
            //获取用户部门信息
            Long deptId = 0L;
            //获取用户部门信息
            Long company_id=SecurityUtils.getLoginUserCompany().getDeptId();
            if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
                deptId = SecurityUtils.getLoginUserCompany().getDeptId();
                isadmin = false;
            }else{
                isadmin = true;
            }
            //折线图数据
            //生码数量
            List<Object> smslXlist = new ArrayList<Object>();
            List<Object> smslYlist = new ArrayList<Object>();
            //扫码数量
            List<Object> smslsXlist = new ArrayList<Object>();
            List<Object> smslsYlist = new ArrayList<Object>();
            //查验数量
            List<Object> cyslXlist = new ArrayList<Object>();
            List<Object> cyslYlist = new ArrayList<Object>();
            Map<String, Object> query = new HashMap<String, Object>();
            //平台方
            if (isadmin) {
                //统计图 1 最近7天 2 最近30天 3 选择时间
                if (map.get("bstjType") != null) {
                    //最近7天
                    if (map.get("bstjType").equals("1")) {
                        query.put("type", "1");
                        //生码数量
                        List<Map<String, Object>> smslList = codeRecordService.selectCodeByDate(query);
                        //单码
                        List<Map<String, Object>> singSmslList = codeRecordService.selectCodeSingByDate(query);
                        if (smslList.size() > 0) {
                            for (int i = 0; i < smslList.size(); i++) {
                                Map<String, Object> smslMap = new HashMap<String, Object>();
                                Map<String, Object> singSmslMap = new HashMap<String, Object>();
                                smslMap = smslList.get(i);
                                singSmslMap = singSmslList.get(i);
                                smslXlist.add(smslMap.get("countKey"));
                                smslYlist.add(Integer.parseInt(smslMap.get("countValue").toString())+Integer.parseInt(singSmslMap.get("countValue").toString()));
                            }
                        }
                        //扫码数量
                        List<Map<String, Object>> smslsList = scanRecordService.selectCountByDate(query);
                        if (smslsList.size() > 0) {
                            for (int i = 0; i < smslsList.size(); i++) {
                                Map<String, Object> smslsMap = new HashMap<String, Object>();
                                smslsMap = smslsList.get(i);
                                smslsXlist.add(smslsMap.get("countKey"));
                                smslsYlist.add(smslsMap.get("countValue"));
                            }
                        }
                        //查验数量
                        List<Map<String, Object>> cyslList = scanRecordService.selectSecueityRecordByDate(query);
                        if (cyslList.size() > 0) {
                            for (int i = 0; i < cyslList.size(); i++) {
                                Map<String, Object> cyslMap = new HashMap<String, Object>();
                                cyslMap = cyslList.get(i);
                                cyslXlist.add(cyslMap.get("countKey"));
                                cyslYlist.add(cyslMap.get("countValue"));
                            }
                        }
                    } else if (map.get("bstjType").equals("2")) {
                        query.put("type", "2");
                        //生码数量
                        List<Map<String, Object>> smslList = codeRecordService.selectCodeByDate(query);
                        //单码
                        List<Map<String, Object>> singSmslList = codeRecordService.selectCodeSingByDate(query);
                        if (smslList.size() > 0) {
                            for (int i = 0; i < smslList.size(); i++) {
                                Map<String, Object> smslMap = new HashMap<String, Object>();
                                Map<String, Object> singSmslMap = new HashMap<String, Object>();
                                smslMap = smslList.get(i);
                                singSmslMap = singSmslList.get(i);
                                smslXlist.add(smslMap.get("countKey"));
                                smslYlist.add(Integer.parseInt(smslMap.get("countValue").toString())+Integer.parseInt(singSmslMap.get("countValue").toString()));
                            }
                        }
                        //扫码数量
                        List<Map<String, Object>> smslsList = scanRecordService.selectCountByDate(query);
                        if (smslsList.size() > 0) {
                            for (int i = 0; i < smslsList.size(); i++) {
                                Map<String, Object> smslsMap = new HashMap<String, Object>();
                                smslsMap = smslsList.get(i);
                                smslsXlist.add(smslsMap.get("countKey"));
                                smslsYlist.add(smslsMap.get("countValue"));
                            }
                        }
                        //查验数量
                        List<Map<String, Object>> cyslList = scanRecordService.selectSecueityRecordByDate(query);
                        if (cyslList.size() > 0) {
                            for (int i = 0; i < cyslList.size(); i++) {
                                Map<String, Object> cyslMap = new HashMap<String, Object>();
                                cyslMap = cyslList.get(i);
                                cyslXlist.add(cyslMap.get("countKey"));
                                cyslYlist.add(cyslMap.get("countValue"));
                            }
                        }
                    } else if (map.get("bstjType").equals("3")) {
                        query.put("type", "3");
                        if(map.get("beginTime")!=null&&map.get("endTime")!=null){
                            query.put("beginTime", map.get("beginTime"));
                            query.put("endTime", map.get("endTime"));
                        }
                        //生码数量
                        List<Map<String, Object>> smslList = codeRecordService.selectCodeByDate(query);
                        //单码
                        List<Map<String, Object>> singSmslList = codeRecordService.selectCodeSingByDate(query);
                        if (smslList.size() > 0) {
                            for (int i = 0; i < smslList.size(); i++) {
                                Map<String, Object> smslMap = new HashMap<String, Object>();
                                Map<String, Object> singSmslMap = new HashMap<String, Object>();
                                smslMap = smslList.get(i);
                                singSmslMap = singSmslList.get(i);
                                smslXlist.add(smslMap.get("countKey"));
                                smslYlist.add(Integer.parseInt(smslMap.get("countValue").toString())+Integer.parseInt(singSmslMap.get("countValue").toString()));
                            }
                        }
                        //扫码数量
                        List<Map<String, Object>> smslsList = scanRecordService.selectCountByDate(query);
                        if (smslsList.size() > 0) {
                            for (int i = 0; i < smslsList.size(); i++) {
                                Map<String, Object> smslsMap = new HashMap<String, Object>();
                                smslsMap = smslsList.get(i);
                                smslsXlist.add(smslsMap.get("countKey"));
                                smslsYlist.add(smslsMap.get("countValue"));
                            }
                        }
                        //查验数量
                        List<Map<String, Object>> cyslList = scanRecordService.selectSecueityRecordByDate(query);
                        if (cyslList.size() > 0) {
                            for (int i = 0; i < cyslList.size(); i++) {
                                Map<String, Object> cyslMap = new HashMap<String, Object>();
                                cyslMap = cyslList.get(i);
                                cyslXlist.add(cyslMap.get("countKey"));
                                cyslYlist.add(cyslMap.get("countValue"));
                            }
                        }
                    }
                }
            } else {
                query.put("deptId", deptId);
                //统计图 1 最近7天 2 最近30天 3 选择时间
                if (map.get("bstjType") != null) {
                    //最近7天
                    if (map.get("bstjType").equals("1")) {
                        query.put("type", "1");
                        //生码数量
                        List<Map<String, Object>> smslList = codeRecordService.selectCodeByDate(query);
                        //单码
                        List<Map<String, Object>> singSmslList = codeRecordService.selectCodeSingByDate(query);
                        if (smslList.size() > 0) {
                            for (int i = 0; i < smslList.size(); i++) {
                                Map<String, Object> smslMap = new HashMap<String, Object>();
                                Map<String, Object> singSmslMap = new HashMap<String, Object>();
                                smslMap = smslList.get(i);
                                singSmslMap = singSmslList.get(i);
                                smslXlist.add(smslMap.get("countKey"));
                                smslYlist.add(Integer.parseInt(smslMap.get("countValue").toString())+Integer.parseInt(singSmslMap.get("countValue").toString()));
                            }
                        }
                        //扫码数量
                        List<Map<String, Object>> smslsList = scanRecordService.selectCountByDate(query);
                        if (smslsList.size() > 0) {
                            for (int i = 0; i < smslsList.size(); i++) {
                                Map<String, Object> smslsMap = new HashMap<String, Object>();
                                smslsMap = smslsList.get(i);
                                smslsXlist.add(smslsMap.get("countKey"));
                                smslsYlist.add(smslsMap.get("countValue"));
                            }
                        }
                        //查验数量
                        List<Map<String, Object>> cyslList = scanRecordService.selectSecueityRecordByDate(query);
                        if (cyslList.size() > 0) {
                            for (int i = 0; i < cyslList.size(); i++) {
                                Map<String, Object> cyslMap = new HashMap<String, Object>();
                                cyslMap = cyslList.get(i);
                                cyslXlist.add(cyslMap.get("countKey"));
                                cyslYlist.add(cyslMap.get("countValue"));
                            }
                        }
                    } else if (map.get("bstjType").equals("2")) {
                        query.put("type", "2");
                        //生码数量
                        List<Map<String, Object>> smslList = codeRecordService.selectCodeByDate(query);
                        //单码
                        List<Map<String, Object>> singSmslList = codeRecordService.selectCodeSingByDate(query);
                        if (smslList.size() > 0) {
                            for (int i = 0; i < smslList.size(); i++) {
                                Map<String, Object> smslMap = new HashMap<String, Object>();
                                Map<String, Object> singSmslMap = new HashMap<String, Object>();
                                smslMap = smslList.get(i);
                                singSmslMap = singSmslList.get(i);
                                smslXlist.add(smslMap.get("countKey"));
                                smslYlist.add(Integer.parseInt(smslMap.get("countValue").toString())+Integer.parseInt(singSmslMap.get("countValue").toString()));
                            }
                        }
                        //扫码数量
                        List<Map<String, Object>> smslsList = scanRecordService.selectCountByDate(query);
                        if (smslsList.size() > 0) {
                            for (int i = 0; i < smslsList.size(); i++) {
                                Map<String, Object> smslsMap = new HashMap<String, Object>();
                                smslsMap = smslsList.get(i);
                                smslsXlist.add(smslsMap.get("countKey"));
                                smslsYlist.add(smslsMap.get("countValue"));
                            }
                        }
                        //查验数量
                        List<Map<String, Object>> cyslList = scanRecordService.selectSecueityRecordByDate(query);
                        if (cyslList.size() > 0) {
                            for (int i = 0; i < cyslList.size(); i++) {
                                Map<String, Object> cyslMap = new HashMap<String, Object>();
                                cyslMap = cyslList.get(i);
                                cyslXlist.add(cyslMap.get("countKey"));
                                cyslYlist.add(cyslMap.get("countValue"));
                            }
                        }
                    } else if (map.get("bstjType").equals("3")) {
                        query.put("type", "3");
                        if(map.get("beginTime")!=null&&map.get("endTime")!=null){
                            query.put("beginTime", map.get("beginTime"));
                            query.put("endTime", map.get("endTime"));
                        }
                        //生码数量
                        List<Map<String, Object>> smslList = codeRecordService.selectCodeByDate(query);
                        //单码
                        List<Map<String, Object>> singSmslList = codeRecordService.selectCodeSingByDate(query);
                        if (smslList.size() > 0) {
                            for (int i = 0; i < smslList.size(); i++) {
                                Map<String, Object> smslMap = new HashMap<String, Object>();
                                Map<String, Object> singSmslMap = new HashMap<String, Object>();
                                smslMap = smslList.get(i);
                                singSmslMap = singSmslList.get(i);
                                smslXlist.add(smslMap.get("countKey"));
                                smslYlist.add(Integer.parseInt(smslMap.get("countValue").toString())+Integer.parseInt(singSmslMap.get("countValue").toString()));
                            }
                        }
                        //扫码数量
                        List<Map<String, Object>> smslsList = scanRecordService.selectCountByDate(query);
                        if (smslsList.size() > 0) {
                            for (int i = 0; i < smslsList.size(); i++) {
                                Map<String, Object> smslsMap = new HashMap<String, Object>();
                                smslsMap = smslsList.get(i);
                                smslsXlist.add(smslsMap.get("countKey"));
                                smslsYlist.add(smslsMap.get("countValue"));
                            }
                        }
                        //查验数量
                        List<Map<String, Object>> cyslList = scanRecordService.selectSecueityRecordByDate(query);
                        if (cyslList.size() > 0) {
                            for (int i = 0; i < cyslList.size(); i++) {
                                Map<String, Object> cyslMap = new HashMap<String, Object>();
                                cyslMap = cyslList.get(i);
                                cyslXlist.add(cyslMap.get("countKey"));
                                cyslYlist.add(cyslMap.get("countValue"));
                            }
                        }
                    }
                }
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("smslXlist", smslXlist);
            ajax.put("smslYlist", smslYlist);
            ajax.put("smslsXlist", smslsXlist);
            ajax.put("smslsYlist", smslsYlist);
            ajax.put("cyslXlist", cyslXlist);
            ajax.put("cyslYlist", cyslYlist);
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        } catch (Exception e) {
            System.out.println(e);
            AjaxResult ajax = AjaxResult.error("查询信息错误！！！");
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        }
    }


    /**
     * 扫码记录时间分布图
     */
    @PostMapping("/selectSmjlsjNum")
    public AjaxResult selectSmjlsjNum(@RequestBody Map<String, Object> map) {
        logger.info("the method getInfoByKey enter, param is {}", map);
        try {
            boolean isadmin = false;
            //获取用户部门信息
            Long deptId = 0L;
            //获取用户部门信息
            Long company_id=SecurityUtils.getLoginUserCompany().getDeptId();
            if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
                deptId = SecurityUtils.getLoginUserCompany().getDeptId();
                isadmin = false;
            }else{
                isadmin = true;
            }
            //折线图数据
            List<Object> smjlXlist = new ArrayList<Object>();
            List<Object> smjlYlist = new ArrayList<Object>();
            Map<String, Object> query = new HashMap<String, Object>();
            //平台方
            if (isadmin) {
                //统计图 1今日 2 昨天 3 最近7天 4 最近30天 5 自己选择的时间
                if (map.get("smjlType") != null) {
                    //今日
                    if (map.get("smjlType").equals("1")) {
                        query.put("type", "1");
                        List<Map<String, Object>> smjlList = scanRecordService.selectCountByTime(query);
                        if (smjlList.size() > 0) {
                            for (int i = 0; i < smjlList.size(); i++) {
                                Map<String, Object> smjlMap = new HashMap<String, Object>();
                                smjlMap = smjlList.get(i);
                                smjlXlist.add(smjlMap.get("click_date"));
                                smjlYlist.add(smjlMap.get("count"));
                            }
                        }
                    } else if (map.get("smjlType").equals("2")) {
                        query.put("type", "2");
                        List<Map<String, Object>> smjlList = scanRecordService.selectCountByTime(query);
                        if (smjlList.size() > 0) {
                            for (int i = 0; i < smjlList.size(); i++) {
                                Map<String, Object> smjlMap = new HashMap<String, Object>();
                                smjlMap = smjlList.get(i);
                                smjlXlist.add(smjlMap.get("click_date"));
                                smjlYlist.add(smjlMap.get("count"));
                            }
                        }
                    } else if (map.get("smjlType").equals("3")) {
                        query.put("type", "3");
                        List<Map<String, Object>> smjlList = scanRecordService.selectCountByTime(query);
                        if (smjlList.size() > 0) {
                            for (int i = 0; i < smjlList.size(); i++) {
                                Map<String, Object> smjlMap = new HashMap<String, Object>();
                                smjlMap = smjlList.get(i);
                                smjlXlist.add(smjlMap.get("click_date"));
                                smjlYlist.add(smjlMap.get("count"));
                            }
                        }

                    } else if (map.get("smjlType").equals("4")) {
                        query.put("type", "4");
                        List<Map<String, Object>> smjlList = scanRecordService.selectCountByTime(query);
                        if (smjlList.size() > 0) {
                            for (int i = 0; i < smjlList.size(); i++) {
                                Map<String, Object> smjlMap = new HashMap<String, Object>();
                                smjlMap = smjlList.get(i);
                                smjlXlist.add(smjlMap.get("click_date"));
                                smjlYlist.add(smjlMap.get("count"));
                            }
                        }
                    } else if (map.get("smjlType").equals("5")) {
                        query.put("type", "5");
                        query.put("beginTime", map.get("beginTime"));
                        query.put("endTime", map.get("endTime"));
                        List<Map<String, Object>> smjlList = scanRecordService.selectCountByTime(query);
                        if (smjlList.size() > 0) {
                            for (int i = 0; i < smjlList.size(); i++) {
                                Map<String, Object> smjlMap = new HashMap<String, Object>();
                                smjlMap = smjlList.get(i);
                                smjlXlist.add(smjlMap.get("click_date"));
                                smjlYlist.add(smjlMap.get("count"));
                            }
                        }
                    }
                }
            } else {
                query.put("deptId", deptId);
                //统计图 1今日 2 昨天 3 最近7天 4 最近30天 5 自己选择的时间
                if (map.get("smjlType") != null) {
                    //今日
                    if (map.get("smjlType").equals("1")) {
                        query.put("type", "1");
                        List<Map<String, Object>> smjlList = scanRecordService.selectCountByTime(query);
                        if (smjlList.size() > 0) {
                            for (int i = 0; i < smjlList.size(); i++) {
                                Map<String, Object> smjlMap = new HashMap<String, Object>();
                                smjlMap = smjlList.get(i);
                                smjlXlist.add(smjlMap.get("click_date"));
                                smjlYlist.add(smjlMap.get("count"));
                            }
                        }
                    } else if (map.get("smjlType").equals("2")) {
                        query.put("type", "2");
                        List<Map<String, Object>> smjlList = scanRecordService.selectCountByTime(query);
                        if (smjlList.size() > 0) {
                            for (int i = 0; i < smjlList.size(); i++) {
                                Map<String, Object> smjlMap = new HashMap<String, Object>();
                                smjlMap = smjlList.get(i);
                                smjlXlist.add(smjlMap.get("click_date"));
                                smjlYlist.add(smjlMap.get("count"));
                            }
                        }
                    } else if (map.get("smjlType").equals("3")) {
                        query.put("type", "3");
                        List<Map<String, Object>> smjlList = scanRecordService.selectCountByTime(query);
                        if (smjlList.size() > 0) {
                            for (int i = 0; i < smjlList.size(); i++) {
                                Map<String, Object> smjlMap = new HashMap<String, Object>();
                                smjlMap = smjlList.get(i);
                                smjlXlist.add(smjlMap.get("click_date"));
                                smjlYlist.add(smjlMap.get("count"));
                            }
                        }

                    } else if (map.get("smjlType").equals("4")) {
                        query.put("type", "4");
                        List<Map<String, Object>> smjlList = scanRecordService.selectCountByTime(query);
                        if (smjlList.size() > 0) {
                            for (int i = 0; i < smjlList.size(); i++) {
                                Map<String, Object> smjlMap = new HashMap<String, Object>();
                                smjlMap = smjlList.get(i);
                                smjlXlist.add(smjlMap.get("click_date"));
                                smjlYlist.add(smjlMap.get("count"));
                            }
                        }
                    } else if (map.get("smjlType").equals("5")) {
                        query.put("type", "5");
                        query.put("beginTime", map.get("beginTime"));
                        query.put("endTime", map.get("endTime"));
                        List<Map<String, Object>> smjlList = scanRecordService.selectCountByTime(query);
                        if (smjlList.size() > 0) {
                            for (int i = 0; i < smjlList.size(); i++) {
                                Map<String, Object> smjlMap = new HashMap<String, Object>();
                                smjlMap = smjlList.get(i);
                                smjlXlist.add(smjlMap.get("click_date"));
                                smjlYlist.add(smjlMap.get("count"));
                            }
                        }
                    }
                }
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("smjlXlist", smjlXlist);
            ajax.put("smjlYlist", smjlYlist);
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        } catch (Exception e) {
            System.out.println(e);
            AjaxResult ajax = AjaxResult.error("查询信息错误！！！");
            logger.info("the method getInfoByKey end, result is {}", ajax);
            return ajax;
        }
    }



}
