package com.ztl.gym.web.controller.storage;

import java.util.List;
import java.util.Map;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.storage.service.IStorageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.service.IStorageInService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 入库Controller
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@RestController
@RequestMapping("/storage/in")
public class StorageInController extends BaseController {
    @Autowired
    private IStorageInService storageInService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private CommonService commonService;

    /**
     * 查询入库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:in:list')")
    @GetMapping("/list")
    public TableDataInfo list(StorageIn storageIn) {
        startPage();
        List<StorageIn> list = storageInService.selectStorageInList(storageIn);
        return getDataTable(list);
    }

    /**
     * 导出入库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:in:export')")
    @Log(title = "入库", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(StorageIn storageIn) {
        List<StorageIn> list = storageInService.selectStorageInList(storageIn);
        ExcelUtil<StorageIn> util = new ExcelUtil<StorageIn>(StorageIn.class);
        return util.exportExcel(list, "in");
    }

    /**
     * 获取入库详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:in:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(storageInService.selectStorageInById(id));
    }
    /**
     * 获取入库码产品详细信息
     */
    @GetMapping("/getCodeDetailById")
    public AjaxResult getCodeDetailById(@RequestParam("companyId") Long companyId, @RequestParam("id") Integer id) {
        return AjaxResult.success(storageInService.getCodeDetailById(companyId,id));
    }

    /**
     * 新增入库
     */
    @PreAuthorize("@ss.hasPermi('storage:in:add')")
    @Log(title = "入库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Map<String, Object> map) {
        storageInService.insertStorageIn(map);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", map.get("id").toString());
        return ajax;
    }

    /**
     * 修改入库
     */
    @PreAuthorize("@ss.hasPermi('storage:in:edit')")
    @Log(title = "入库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StorageIn storageIn) {
        return toAjax(storageInService.updateStorageIn(storageIn));
    }

    /**
     * 删除入库
     */
    @PreAuthorize("@ss.hasPermi('storage:in:remove')")
    @Log(title = "入库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(storageInService.deleteStorageInByIds(ids));
    }

    /**
     * updateInStatusByCode
     * PDA扫码入库
     */
    @Log(title = "PDA扫码入库", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/updateInStatusByCode")
    public AjaxResult updateInStatusByCode(@RequestBody Map<String, Object> map)
    {
        return toAjax(storageInService.updateInStatusByCode(map));
    }
    /**
     * updateTenantIn
     * PC经销商确认收货
     */
    @Log(title = "经销商确认收货", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/updateTenantIn")
    public AjaxResult updateTenantIn(@RequestBody Map<String, Object> map)
    {
        return toAjax(storageInService.updateTenantIn(map));
    }
}
