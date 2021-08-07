package com.ztl.gym.web.controller.statistical;

import com.alibaba.fastjson.JSONObject;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.statistical.service.IScanCodeStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Description:
 * @Author: zyl
 * @Date: 2021/8/4 8:53
 */
@Api(value = "扫码统计接口",tags = "扫码统计")
@RestController
@RequestMapping("/statistical/scanCode")
public class ScanCodeStatisticsController {

    @Autowired
    private IScanCodeStatisticsService service;


    /**
     * 获取用户公司id
     * @return
     */
    public Long getDeptId() {
        Long deptId = null;
        //获取用户部门信息
        Long company_id=SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)) {
            deptId = company_id;
        }
        return deptId;
    }

    @ApiOperation(value = "总生码量")
    @GetMapping("/generateCount")
    public AjaxResult generateCount() {
        try {
            Long deptId = getDeptId();
            JSONObject result = service.generateCount(deptId);

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "总扫码量")
    @GetMapping("/scanCount")
    public AjaxResult scanCount() {
        try {
            Long deptId = getDeptId();
            JSONObject result = service.scanWeekAndDayRate(deptId);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "查验次数")
    @GetMapping("/securityCount")
    public AjaxResult securityCount() {
        try {
            Long deptId = getDeptId();
            JSONObject result = service.securityCount(deptId);

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "窜货率")
    @GetMapping("/mixRate")
    public AjaxResult mixRate() {
        try {
            Long deptId = getDeptId();
            JSONObject result = service.mixRate(deptId);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "扫码数量展示图")
    @GetMapping("/scanEChartTime")
    public AjaxResult scanEChartTime(Integer day) {
        try {
            if (day == null || day<=0) {
                throw new CustomException("传入天数有误");
            }
            Long deptId = getDeptId();
            JSONObject result = service.scanEChartTime(day,deptId);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }


    @ApiOperation(value = "近一周标识生成量")
    @GetMapping("/generateEChartWeek")
    public AjaxResult generateEChartWeek() {
        try {
            Long deptId = getDeptId();
            JSONObject result = service.generateEChartWeek(deptId);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "区域扫码统计")
    @GetMapping("/scanEChartArea")
    public AjaxResult scanEChartArea() {
        try {
            Long deptId = getDeptId();
            JSONObject result = service.scanEChartArea(deptId);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * @param type 1-企业端 2-平台端
     * @return
     */
    @ApiOperation(value = "昨日扫码记录时间分布图")
    @GetMapping("/scanEChartYesterday")
    public AjaxResult scanEChartYesterday(Integer type) {
        try {
            if (type == null) {
                throw new CustomException("传入参数有误");
            }
            Long deptId ;
            if (type == 2) {
                //平台端 admin 查找所有
                deptId = null;
            } else {
                deptId = getDeptId();
            }
            JSONObject result = service.scanEChartYesterday(deptId);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * @param startTime 起始时间 yyyy-MM-dd
     * @param endTime   结束时间
     * @param type 1-企业端 2-平台端
     * @return
     */
    @ApiOperation(value = "统计时间分布图")
    @GetMapping("/statisticalDateEChart")
    public AjaxResult statisticalDateEChart(String startTime,String endTime,Integer type) {
        try {
            if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime) || type == null) {
                throw new CustomException("传入参数有误");
            }
            Long deptId ;
            if (type == 2) {
                //平台端 admin 查找所有
                deptId = null;
            } else {
                deptId = getDeptId();
            }
            JSONObject result = service.statisticalDateEChart(deptId,startTime,endTime);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }


    @ApiOperation(value = "生码排行(平台端)")
    @GetMapping("/generateCodeTop")
    public AjaxResult generateCodeTop() {
        try {
            JSONObject result = service.generateCodeTop();
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "扫码排行(平台端)")
    @GetMapping("/scanCodeTop")
    public AjaxResult scanCodeTop() {
        try {
            JSONObject result = service.scanCodeTop();
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "查验排行(平台端)")
    @GetMapping("/securityCodeTop")
    public AjaxResult securityCodeTop() {
        try {
            JSONObject result = service.securityCodeTop();
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }


}
