package com.ztl.gym.web.controller.system;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.system.domain.SysPlat;
import com.ztl.gym.system.service.ISysPlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台Controller
 *
 * @author ruoyi
 * @date 2021-04-17
 */
@RestController
@RequestMapping("/system/plat")
public class SysPlatController extends BaseController
{
    @Autowired
    private ISysPlatService sysPlatService;

    /**
     * 查询平台列表
     */
    @PreAuthorize("@ss.hasPermi('system:plat:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPlat sysPlat)
    {
        startPage();
        List<SysPlat> list = sysPlatService.selectSysPlatList(sysPlat);
        return getDataTable(list);
    }

    /**
     * 导出平台列表
     */
    @PreAuthorize("@ss.hasPermi('system:plat:export')")
    @Log(title = "平台", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysPlat sysPlat)
    {
        List<SysPlat> list = sysPlatService.selectSysPlatList(sysPlat);
        ExcelUtil<SysPlat> util = new ExcelUtil<SysPlat>(SysPlat.class);
        return util.exportExcel(list, "plat");
    }

    /**
     * 获取平台详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:plat:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysPlatService.selectSysPlatById(id));
    }

    /**
     * 新增平台
     */
    @PreAuthorize("@ss.hasPermi('system:plat:add')")
    @Log(title = "平台", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPlat sysPlat)
    {
        return toAjax(sysPlatService.insertSysPlat(sysPlat));
    }

    /**
     * 修改平台
     */
    @PreAuthorize("@ss.hasPermi('system:plat:edit')")
    @Log(title = "平台", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPlat sysPlat)
    {
        return toAjax(sysPlatService.updateSysPlat(sysPlat));
    }

    /**
     * 删除平台
     */
    @PreAuthorize("@ss.hasPermi('system:plat:remove')")
    @Log(title = "平台", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysPlatService.deleteSysPlatByIds(ids));
    }
}
