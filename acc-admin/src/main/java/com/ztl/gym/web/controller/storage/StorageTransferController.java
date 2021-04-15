package com.ztl.gym.web.controller.storage;

import java.util.List;
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
import com.ztl.gym.storage.domain.StorageTransfer;
import com.ztl.gym.storage.service.IStorageTransferService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 调货Controller
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@RestController
@RequestMapping("/storage/transfer")
public class StorageTransferController extends BaseController
{
    @Autowired
    private IStorageTransferService storageTransferService;

    /**
     * 查询调货列表
     */
    @PreAuthorize("@ss.hasPermi('storage:transfer:list')")
    @GetMapping("/list")
    public TableDataInfo list(StorageTransfer storageTransfer)
    {
        startPage();
        List<StorageTransfer> list = storageTransferService.selectStorageTransferList(storageTransfer);
        return getDataTable(list);
    }

    /**
     * 导出调货列表
     */
    @PreAuthorize("@ss.hasPermi('storage:transfer:export')")
    @Log(title = "调货", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(StorageTransfer storageTransfer)
    {
        List<StorageTransfer> list = storageTransferService.selectStorageTransferList(storageTransfer);
        ExcelUtil<StorageTransfer> util = new ExcelUtil<StorageTransfer>(StorageTransfer.class);
        return util.exportExcel(list, "transfer");
    }

    /**
     * 获取调货详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:transfer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(storageTransferService.selectStorageTransferById(id));
    }

    /**
     * 新增调货
     */
    @PreAuthorize("@ss.hasPermi('storage:transfer:add')")
    @Log(title = "调货", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StorageTransfer storageTransfer)
    {
        return toAjax(storageTransferService.insertStorageTransfer(storageTransfer));
    }

    /**
     * 修改调货
     */
    @PreAuthorize("@ss.hasPermi('storage:transfer:edit')")
    @Log(title = "调货", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StorageTransfer storageTransfer)
    {
        return toAjax(storageTransferService.updateStorageTransfer(storageTransfer));
    }

    /**
     * 删除调货
     */
    @PreAuthorize("@ss.hasPermi('storage:transfer:remove')")
    @Log(title = "调货", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(storageTransferService.deleteStorageTransferByIds(ids));
    }
}
