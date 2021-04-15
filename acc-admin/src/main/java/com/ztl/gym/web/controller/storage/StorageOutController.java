package com.ztl.gym.web.controller.storage;

import java.util.List;
import java.util.Map;

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
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.service.IStorageOutService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 出库Controller
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@RestController
@RequestMapping("/storage/out")
public class StorageOutController extends BaseController
{
    @Autowired
    private IStorageOutService storageOutService;

    /**
     * 查询出库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:out:list')")
    @GetMapping("/list")
    public TableDataInfo list(StorageOut storageOut)
    {
        startPage();
        List<StorageOut> list = storageOutService.selectStorageOutList(storageOut);
        return getDataTable(list);
    }

    /**
     * 导出出库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:out:export')")
    @Log(title = "出库", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(StorageOut storageOut)
    {
        List<StorageOut> list = storageOutService.selectStorageOutList(storageOut);
        ExcelUtil<StorageOut> util = new ExcelUtil<StorageOut>(StorageOut.class);
        return util.exportExcel(list, "out");
    }

    /**
     * 获取出库详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:out:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(storageOutService.selectStorageOutById(id));
    }

    /**
     * 新增出库
     */
    @PreAuthorize("@ss.hasPermi('storage:out:add')")
    @Log(title = "出库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Map<String, Object> storageOut)
    {
        return toAjax(storageOutService.insertStorageOut(storageOut));
    }

    /**
     * 修改出库
     */
    @PreAuthorize("@ss.hasPermi('storage:out:edit')")
    @Log(title = "出库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StorageOut storageOut)
    {
        return toAjax(storageOutService.updateStorageOut(storageOut));
    }

    /**
     * 删除出库
     */
    @PreAuthorize("@ss.hasPermi('storage:out:remove')")
    @Log(title = "出库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(storageOutService.deleteStorageOutByIds(ids));
    }
}
