package com.ztl.gym.web.controller.code;

import com.ztl.gym.code.domain.SecurityCodeRecord;
import com.ztl.gym.code.domain.vo.ScanSecurityCodeOutBean;
import com.ztl.gym.code.service.ISecurityCodeRecordService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("@ss.hasPermi('product:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(SecurityCodeRecord securityCodeRecord) {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            securityCodeRecord.setCompanyId(company_id);
        }
        startPage();
        List<SecurityCodeRecord> list = securityCodeRecordService.selectSecurityCodeRecordList(securityCodeRecord);
        return getDataTable(list);
    }

    /**
     * 导出防伪记录 company_id字段分列表
     */
    @PreAuthorize("@ss.hasPermi('product:record:export')")
    @Log(title = "防伪记录 company_id字段分", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SecurityCodeRecord securityCodeRecord)
    {
        List<SecurityCodeRecord> list = securityCodeRecordService.selectSecurityCodeRecordList(securityCodeRecord);
        ExcelUtil<SecurityCodeRecord> util = new ExcelUtil<SecurityCodeRecord>(SecurityCodeRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 获取防伪记录详细信息
     */
/*    @PreAuthorize("@ss.hasPermi('product:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(securityCodeRecordService.selectSecurityCodeRecordById(id));
    }*/

    /**
     * 通过accCodecheck防伪码
     *
     * @param securityCodeRecord 防伪扫描对象
     * @return 响应
     */
    @Log(title = "防伪记录", businessType = BusinessType.INSERT)
    @PostMapping("/accCode")
    public AjaxResult checkByAccCode(@RequestBody SecurityCodeRecord securityCodeRecord) {
        logger.info("the method checkByAccCode enter,check SecurityCode param is {}", securityCodeRecord);

        //参数校验,code和accCode必须有一个不为空
        if (StringUtils.isBlank(securityCodeRecord.getCodeAcc())) {
            logger.error("codeAcc字段不能为空");
            return AjaxResult.error("参数校验失败");
        }
        //不从登录信息获取企业id，根据防伪码解析
        securityCodeRecord.setCompanyId(CodeRuleUtils.getCompanyIdBySecurityCode(securityCodeRecord.getCodeAcc()));
        ScanSecurityCodeOutBean scanSecurityCodeOutBean = null;

        scanSecurityCodeOutBean = securityCodeRecordService.getSecurityCodeInfo(securityCodeRecord);

        logger.info("the method checkByAccCode end,result is {}", scanSecurityCodeOutBean);
        return AjaxResult.success(scanSecurityCodeOutBean);
    }

    /**
     * 通过Codecheck防伪码
     *
     * @param securityCodeRecord 防伪扫描对象
     * @return 响应
     */
    @Log(title = "防伪记录", businessType = BusinessType.INSERT)
    @PostMapping("/code")
    public AjaxResult checkByCode(@RequestBody SecurityCodeRecord securityCodeRecord) {
        logger.info("the method checkByCode enter,check SecurityCode param is {}", securityCodeRecord);

        //参数校验,code和accCode必须有一个不为空
        if (StringUtils.isBlank(securityCodeRecord.getCode())) {
            logger.error("code字段不能为空");
            return AjaxResult.error("参数校验失败");
        }
        securityCodeRecord.setCompanyId(CodeRuleUtils.getCompanyIdByCode(securityCodeRecord.getCode()));
        ScanSecurityCodeOutBean scanSecurityCodeOutBean = null;

        scanSecurityCodeOutBean = securityCodeRecordService.getSecurityCodeInfoByCode(securityCodeRecord);

        logger.info("the method checkByCode end,result is {}", scanSecurityCodeOutBean);
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
