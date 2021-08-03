package com.ztl.gym.web.controller.code;

import java.util.List;

import com.ztl.gym.code.domain.SecurityCodeRecord;
import com.ztl.gym.code.domain.vo.ScanSecurityCodeOutBean;
import com.ztl.gym.code.service.ISecurityCodeRecordService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.web.controller.payment.PaymentRecordController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.BusinessType;

import javax.validation.Valid;

/**
 * 防伪记录 Controller
 *
 * @author wujinhao
 * @date 2021-08-02
 */
@RestController
@RequestMapping("/securityCode/record")
public class SecurityCodeRecordController extends BaseController {

    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(SecurityCodeRecordController.class);

    @Autowired
    private ISecurityCodeRecordService securityCodeRecordService;

    /**
     * 查询防伪记录 company_id字段分列表
     */
/*    @PreAuthorize("@ss.hasPermi('product:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(SecurityCodeRecord securityCodeRecord) {
        startPage();
        List<SecurityCodeRecord> list = securityCodeRecordService.selectSecurityCodeRecordList(securityCodeRecord);
        return getDataTable(list);
    }*/

    /**
     * 导出防伪记录 company_id字段分列表
     */
    /*@PreAuthorize("@ss.hasPermi('product:record:export')")
    @Log(title = "防伪记录 company_id字段分", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SecurityCodeRecord securityCodeRecord)
    {
        List<SecurityCodeRecord> list = securityCodeRecordService.selectSecurityCodeRecordList(securityCodeRecord);
        ExcelUtil<SecurityCodeRecord> util = new ExcelUtil<SecurityCodeRecord>(SecurityCodeRecord.class);
        return util.exportExcel(list, "record");
    }*/

    /**
     * 获取防伪记录详细信息
     */
/*    @PreAuthorize("@ss.hasPermi('product:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(securityCodeRecordService.selectSecurityCodeRecordById(id));
    }*/

    /**
     * check防伪码
     *
     * @param securityCodeRecord 防伪扫描对象
     * @return 响应
     */
    @Log(title = "防伪记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult check(@RequestBody SecurityCodeRecord securityCodeRecord) {
        logger.info("the method check enter,check SecurityCode param is {}", securityCodeRecord);

        //参数校验,code和accCode必须有一个不为空
        if (StringUtils.isBlank(securityCodeRecord.getCode()) && StringUtils.isBlank(securityCodeRecord.getCodeAcc())) {
            return AjaxResult.error("参数校验失败");
        }
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            securityCodeRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        } else {
            securityCodeRecord.setCompanyId(companyId);
        }
        ScanSecurityCodeOutBean scanSecurityCodeOutBean = null;
        if (StringUtils.isBlank(securityCodeRecord.getCode())) {
            scanSecurityCodeOutBean = securityCodeRecordService.getSecurityCodeInfo(securityCodeRecord);
        } else {
            scanSecurityCodeOutBean = securityCodeRecordService.getSecurityCodeInfoByCode(securityCodeRecord);
        }
        logger.info("the method check end,result is {}", scanSecurityCodeOutBean);
        return AjaxResult.success(scanSecurityCodeOutBean);
    }

    /**
     * 修改防伪记录
     */
/*    @PreAuthorize("@ss.hasPermi('product:record:edit')")
    @Log(title = "防伪记录 company_id字段分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SecurityCodeRecord securityCodeRecord) {
        return toAjax(securityCodeRecordService.updateSecurityCodeRecord(securityCodeRecord));
    }*/

    /**
     * 删除防伪记录
     */
/*    @PreAuthorize("@ss.hasPermi('product:record:remove')")
    @Log(title = "防伪记录 company_id字段分", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(securityCodeRecordService.deleteSecurityCodeRecordByIds(ids));
    }*/
}
