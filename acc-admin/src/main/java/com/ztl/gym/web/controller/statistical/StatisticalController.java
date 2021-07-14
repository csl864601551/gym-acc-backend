package com.ztl.gym.web.controller.statistical;

import cn.hutool.core.date.DateUtil;
import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.mix.service.IMixRecordService;
import com.ztl.gym.product.service.IProductService;
import com.ztl.gym.storage.service.IStorageOutService;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenz
 * @create 2021-07-10 9:17
 */
@RestController
@RequestMapping("/statistical/statistical")
public class StatisticalController {


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

    /**
     * 查询首页信息
     */
    @PostMapping("/indexsj")
    public AjaxResult selectindexsj(@RequestBody Map<String,Object> map)
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
        //平台管理员
        int qynum =0;
        int jxsnum =0;
        int cpchlnum =0;
        int chzlnum =0;
        int smzlnum =0;
        int ljczfynum =0;
        //产品总览
        int yxjnum =0;
        int ysjnum =0;
        int kcjznum =0;
        int qbcpnum =0;
        //经销商总览
        int jrxznum =0;
        int zrxznum =0;
        int byxznum =0;
        int jxszs =0;

        //平台方
        if(isadmin){
            //企业总数
            query.put("type",5);
            query.put("topdeptId",AccConstants.ADMIN_DEPT_ID);
            qynum = deptService.selectCountBydept(query);
            //产品出货总量
            cpchlnum = storageOutService.selectCountByDept(query);
            //窜货总量
            chzlnum = mixRecordService.selectmixnum(query);
            //生码总量
            smzlnum = codeRecordService.selectcodenum(query);
            //累计充值费用

            //产品总览
            //已下架
            query.put("type",1);
            yxjnum = tProductService.selectProductNum(query);
            //已上架
            query.put("type",2);
            ysjnum = tProductService.selectProductNum(query);
            //库存紧张
            query.put("type",3);
            kcjznum = tProductService.selectProductNum(query);
            //全部产品
            query.put("type",4);
            qbcpnum = tProductService.selectProductNum(query);

            //经销商总览
            //今日新增
            Date today = DateUtil.date();
            Date begintime = DateUtil.beginOfDay(today);
            Date endtime = DateUtil.endOfDay(today);
            query.put("type",1);
            query.put("begintime",begintime);
            query.put("endtime",endtime);
            jrxznum = deptService.selectCountBydept(query);
            //昨日新增
            Date yesterday = DateUtil.yesterday();
            begintime = DateUtil.beginOfDay(yesterday);
            endtime = DateUtil.endOfDay(yesterday);
            query.put("type",2);
            query.put("begintime",begintime);
            query.put("endtime",endtime);
            zrxznum = deptService.selectCountBydept(query);
            //本月新增
            begintime = DateUtil.beginOfMonth(today);
            endtime = DateUtil.endOfMonth(today);
            query.put("type",3);
            query.put("begintime",begintime);
            query.put("endtime",endtime);
            byxznum = deptService.selectCountBydept(query);
            //经销商总数
            query.put("type",3);
            jxszs = deptService.selectCountBydept(query);

        }else{
            query.put("deptId",deptId);
            //经销商总数
            query.put("type",3);
            jxsnum = deptService.selectCountBydept(query);
            //产品出货总数
            cpchlnum = storageOutService.selectCountByDept(query);
            //窜货总数
            chzlnum = mixRecordService.selectmixnum(query);
            //剩余码量
            //已经使用的码量
            smzlnum = codeRecordService.selectcodenum(query);
            //剩余费用

            //产品总览
            //已下架
            query.put("type",1);
            yxjnum = tProductService.selectProductNum(query);
            //已上架
            query.put("type",2);
            ysjnum = tProductService.selectProductNum(query);
            //库存紧张
            query.put("type",3);
            kcjznum = tProductService.selectProductNum(query);
            //全部产品
            query.put("type",4);
            qbcpnum = tProductService.selectProductNum(query);

            //经销商总览
            //今日新增
            Date today = DateUtil.date();
            Date begintime = DateUtil.beginOfDay(today);
            Date endtime = DateUtil.endOfDay(today);
            query.put("type",1);
            query.put("begintime",begintime);
            query.put("endtime",endtime);
            jrxznum = deptService.selectCountBydept(query);
            //昨日新增
            Date yesterday = DateUtil.yesterday();
            begintime = DateUtil.beginOfDay(yesterday);
            endtime = DateUtil.endOfDay(yesterday);
            query.put("type",2);
            query.put("begintime",begintime);
            query.put("endtime",endtime);
            zrxznum = deptService.selectCountBydept(query);
            //本月新增
            begintime = DateUtil.beginOfMonth(today);
            endtime = DateUtil.endOfMonth(today);
            query.put("type",3);
            query.put("begintime",begintime);
            query.put("endtime",endtime);
            byxznum = deptService.selectCountBydept(query);
            //经销商总数
            query.put("type",3);
            jxszs = deptService.selectCountBydept(query);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("qynum", qynum);
        ajax.put("jxsnum", jxsnum);
        ajax.put("cpchlnum", cpchlnum);
        ajax.put("chzlnum", chzlnum);
        ajax.put("smzlnum", smzlnum);
        ajax.put("ljczfynum", ljczfynum);
        ajax.put("yxjnum", yxjnum);
        ajax.put("ysjnum", ysjnum);
        ajax.put("kcjznum", kcjznum);
        ajax.put("qbcpnum", qbcpnum);
        ajax.put("jrxznum", jrxznum);
        ajax.put("zrxznum", zrxznum);
        ajax.put("byxznum", byxznum);
        ajax.put("jxszs", jxszs);
        return ajax;
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


}
