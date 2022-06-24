package com.ztl.gym.web.controller.print;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.print.domain.PrintData;
import com.ztl.gym.print.service.PrintDataService;
import com.ztl.gym.web.controller.code.CodeRecordController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boxCode/print")
public class PrintDataController {
    @Autowired
    public PrintDataService printDataService;

    @Autowired
    private ICodeService codeService;

    private static final Logger log = LoggerFactory.getLogger(PrintDataController.class);

    /**
     * 获取打印数据
     * @param productLine
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/getData")
    public AjaxResult getData(@RequestParam(value = "productLine") String productLine) {
        try {
            Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();

            List<PrintData> lsPrint =  printDataService.getData(productLine);
            return AjaxResult.success(lsPrint);
        } catch (Exception err) {
            return AjaxResult.error(err.toString());
        }
    }

    /**
     * 更新打印状态
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/updatePrintStatus")
    public AjaxResult updatePrint(@RequestParam(value = "id") Long id) {
        try {
            int count = printDataService.updatePrint(id);
            return AjaxResult.success(count);
        } catch (Exception err) {
            return AjaxResult.error(err.toString());
        }
    }

    /**
     * 补打箱码
     * @param map
     * @return
     */
    @Log(title = "补打箱码", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/printBoxCode")
    @DataSource(DataSourceType.SHARDING)
    public AjaxResult printBoxCode(@RequestBody Map<String,Object> map) {
        try {
            log.info("开始补打箱码！");
            //获取箱码信息
            Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();

            Code temp = new Code();
            temp.setCode(map.get("productCode").toString());
            temp.setCompanyId(companyId);
            Code codeResult=codeService.selectCode(temp);//查询单码数据

            if(codeResult != null) {
                if(codeResult.getCode() == null) {
                    log.error("单码未装箱或有误！" + codeResult.getCode());
                    return AjaxResult.error("单码未装箱或有误");
                }
                Map<String, Object> params = new HashMap<>();
                params.put("boxCode", codeResult.getpCode());
                params.put("productLine", map.get("productLine").toString());
                //获取箱码补打信息
                PrintData printData = printDataService.getPrintBoxData(params);
                log.info("补打箱码成功 " + codeResult.getpCode());
                return AjaxResult.success(printData);
            } else {
                log.error("单码不存在或有误！");
                return AjaxResult.error("单码不存在或有误");
            }
        } catch (Exception err) {
            return AjaxResult.error(err.toString());
        }
    }

    /**
     * 补打单码
     * @param map
     * @return
     */
    @Log(title = "补打单码", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/printProductCode")
    @DataSource(DataSourceType.SHARDING)
    public AjaxResult printProductCode(@RequestBody Map<String,Object> map) {
        try {
            log.info("开始补打单码！");
            //获取箱码信息
            Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();

            Code temp = new Code();
            temp.setCode(map.get("productCode").toString());
            temp.setCompanyId(companyId);
            Code codeResult=codeService.selectCode(temp);//查询单码数据

            if(codeResult != null) {
                log.info("补打单码成功 " + codeResult.getCode());
                return AjaxResult.success(codeResult.getCode());
            } else {
                log.error("单码不存在或有误！");
                return AjaxResult.error("单码不存在或有误");
            }
        } catch (Exception err) {
            return AjaxResult.error(err.toString());
        }
    }
}
