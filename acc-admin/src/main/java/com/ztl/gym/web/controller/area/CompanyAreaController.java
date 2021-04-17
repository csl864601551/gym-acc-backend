package com.ztl.gym.web.controller.area;

import java.util.Date;
import java.util.List;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.area.service.ICompanyAreaService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 经销商销售区域 Controller
 *
 * @author lujian
 * @date 2021-04-17
 */
@RestController
@RequestMapping("/area/area")
public class CompanyAreaController extends BaseController {
    @Autowired
    private ICompanyAreaService companyAreaService;

    /**
     * 查询经销商销售区域 列表
     */
    @PreAuthorize("@ss.hasPermi('system:area:list')")
    @GetMapping("/list")
    public TableDataInfo list(CompanyArea companyArea) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            companyArea.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        startPage();
        List<CompanyArea> list = companyAreaService.selectCompanyAreaList(companyArea);
        return getDataTable(list);
    }

    /**
     * 导出经销商销售区域 列表
     */
    @PreAuthorize("@ss.hasPermi('system:area:export')")
    @Log(title = "经销商销售区域 ", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CompanyArea companyArea) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            companyArea.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        List<CompanyArea> list = companyAreaService.selectCompanyAreaList(companyArea);
        ExcelUtil<CompanyArea> util = new ExcelUtil<CompanyArea>(CompanyArea.class);
        return util.exportExcel(list, "area");
    }

    /**
     * 获取经销商销售区域 详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:area:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(companyAreaService.selectCompanyAreaById(id));
    }

    /**
     * 新增经销商销售区域
     */
    @PreAuthorize("@ss.hasPermi('system:area:add')")
    @Log(title = "经销商销售区域 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CompanyArea companyArea) {
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            companyArea.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
            String[] ancestors = loginUser.getDept().getAncestors().split(",");
            if (ancestors.length > 2) {
                companyArea.setTenantId(companyId);
            }
        }
        companyArea.setCreateUser(loginUser.getUserId());
        companyArea.setCreateTime(new Date());
        companyArea.setUpdateUser(loginUser.getUserId());
        companyArea.setUpdateTime(new Date());
        return toAjax(companyAreaService.insertCompanyArea(companyArea));
    }

    /**
     * 修改经销商销售区域
     */
    @PreAuthorize("@ss.hasPermi('system:area:edit')")
    @Log(title = "经销商销售区域 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CompanyArea companyArea) {
        return toAjax(companyAreaService.updateCompanyArea(companyArea));
    }

    /**
     * 删除经销商销售区域
     */
    @PreAuthorize("@ss.hasPermi('system:area:remove')")
    @Log(title = "经销商销售区域 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(companyAreaService.deleteCompanyAreaByIds(ids));
    }
}
