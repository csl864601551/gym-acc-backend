package com.ztl.gym.web.controller.template;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.template.domain.SecurityCodeTemplate;
import com.ztl.gym.template.service.ISecurityCodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * templateController
 *
 * @author ruoyi
 * @date 2021-07-19
 */
@RestController
@RequestMapping("/template/template")
public class SecurityCodeTemplateController extends BaseController
{
    @Autowired
    private ISecurityCodeTemplateService securityCodeTemplateService;

    /**
     * 查询防伪模板列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SecurityCodeTemplate securityCodeTemplate)
    {
        startPage();
        List<SecurityCodeTemplate> list = securityCodeTemplateService.selectSecurityCodeTemplateList(securityCodeTemplate);
        return getDataTable(list);
    }

    /**
     * 导出防伪模板列表
     */
    @PreAuthorize("@ss.hasPermi('template:template:export')")
    @Log(title = "template", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SecurityCodeTemplate securityCodeTemplate)
    {
        List<SecurityCodeTemplate> list = securityCodeTemplateService.selectSecurityCodeTemplateList(securityCodeTemplate);
        ExcelUtil<SecurityCodeTemplate> util = new ExcelUtil<SecurityCodeTemplate>(SecurityCodeTemplate.class);
        return util.exportExcel(list, "template");
    }

    /**
     * 获取防伪模板详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(securityCodeTemplateService.selectSecurityCodeTemplateById(id));
    }

    /**
     * 新增防伪模板
     */
    @PreAuthorize("@ss.hasPermi('template:template:add')")
    @Log(title = "template", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SecurityCodeTemplate securityCodeTemplate)
    {
        //获取登录人信息
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            companyId = SecurityUtils.getLoginUserTopCompanyId();
        }
        if(securityCodeTemplate.getType()!=null){
            if(securityCodeTemplate.getType()==0){
                securityCodeTemplate.setIsOpen("0");
            }else{
                securityCodeTemplate.setIsOpen("1");
            }
        }else{
            securityCodeTemplate.setType(1);
            securityCodeTemplate.setIsOpen("1");
        }
        securityCodeTemplate.setCompanyId(companyId);
        securityCodeTemplate.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return toAjax(securityCodeTemplateService.insertSecurityCodeTemplate(securityCodeTemplate));
    }

    /**
     * 修改防伪模板
     */
    @PreAuthorize("@ss.hasPermi('template:template:edit')")
    @Log(title = "template", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SecurityCodeTemplate securityCodeTemplate)
    {
        //获取登录人信息
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            companyId = SecurityUtils.getLoginUserTopCompanyId();
        }
        securityCodeTemplate.setCompanyId(companyId);
        //根据类型判断 是否展示
        if(securityCodeTemplate.getType()!=null){
            if(securityCodeTemplate.getType()==0){
                securityCodeTemplate.setIsOpen("0");
            }else{
                securityCodeTemplate.setIsOpen("1");
            }
        }else{
            securityCodeTemplate.setType(1);
            securityCodeTemplate.setIsOpen("1");
        }
        securityCodeTemplate.setUpdateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return toAjax(securityCodeTemplateService.updateSecurityCodeTemplate(securityCodeTemplate));
    }

    /**
     * 删除防伪模板
     */
    @PreAuthorize("@ss.hasPermi('template:template:remove')")
    @Log(title = "template", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(securityCodeTemplateService.deleteSecurityCodeTemplateByIds(ids));
    }
}
