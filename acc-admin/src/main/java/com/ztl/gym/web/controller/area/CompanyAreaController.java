package com.ztl.gym.web.controller.area;

import cn.hutool.core.util.StrUtil;
import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.area.service.ICompanyAreaService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysRole;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();
        List<SysRole> roles = loginUser.getRoles();
        if(roles.size()>0){
            for (SysRole sysRole : roles) {
                if(sysRole.getRoleId()!=AccConstants.COMPANY_ROLE&&sysRole.getRoleId()!=AccConstants.ADMIN_ROLE){
                    companyArea.setTenantId(SecurityUtils.getLoginUserCompany().getDeptId());
                }
            }
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
    //@PreAuthorize("@ss.hasPermi('system:area:add')")
    @Log(title = "经销商销售区域 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CompanyArea companyArea) {

        CompanyArea companyAreas = new CompanyArea();
        companyAreas.setProvince(companyArea.getProvince());
        companyAreas.setCity(companyArea.getCity());
        companyAreas.setArea(companyArea.getArea());
        companyAreas.setTenantId(companyArea.getTenantId());
        List<CompanyArea> list = companyAreaService.selectCompanyAreaList(companyAreas);
        if (list.size() > 0) {
            return error("该区域或该经销商已经在区域管理中！！！");
        } else {
            SysUser loginUser = SecurityUtils.getLoginUser().getUser();
            Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
            if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
                companyArea.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
                if(companyArea.getTenantId()>0&&companyArea.getTenantId()!=null){

                }else{
                    companyArea.setTenantId(SecurityUtils.getLoginUserCompany().getDeptId());
                }
            }
            companyArea.setCreateUser(loginUser.getUserId());
            companyArea.setCreateTime(DateUtils.getNowDate());
            companyArea.setUpdateUser(loginUser.getUserId());
            companyArea.setUpdateTime(DateUtils.getNowDate());
            return toAjax(companyAreaService.insertCompanyArea(companyArea));
        }
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


    /**
     * 查询经销商可选择的区域
     */
    @Log(title = "查询经销商可选择的区域 ")
    @GetMapping(value = "/selectkxzqy")
    public AjaxResult selectkxzqy() {
        //SysUser loginUser = SecurityUtils.getLoginUser().getUser();
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        List<CompanyArea> list = companyAreaService.selectkyjxsdqList(companyId);
        List<Map<String, String>> QYlist = new ArrayList<Map<String, String>>();
        if (list.size() > 0) {
            for (CompanyArea companyArea : list) {
                Map<String, String> map = new HashMap<>();
                String value = null;
                if (StrUtil.isNotBlank(companyArea.getProvince())) {
                    value = companyArea.getProvince();
                }
                if (StrUtil.isNotBlank(companyArea.getCity())) {
                    value = value + "/" + companyArea.getCity();
                }
                if (StrUtil.isNotBlank(companyArea.getArea())) {
                    value = value + "/" + companyArea.getArea();
                }
                map.put("value", value);
                map.put("label", value);
                QYlist.add(map);
            }
            System.out.println("QYlist==" + QYlist);
        }
//        companyAreaService.selectCompanyAreaById(id);
        return AjaxResult.success(QYlist);
    }


}
