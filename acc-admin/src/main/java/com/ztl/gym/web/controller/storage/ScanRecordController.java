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
import com.ztl.gym.storage.domain.ScanRecord;
import com.ztl.gym.storage.service.IScanRecordService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 扫码记录Controller
 *
 * @author ruoyi
 * @date 2021-04-28
 */
@RestController
@RequestMapping("/storage/record")
public class ScanRecordController extends BaseController
{
    @Autowired
    private IScanRecordService scanRecordService;

    /**
     * 查询扫码记录列表
     */
    @PreAuthorize("@ss.hasPermi('product:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScanRecord scanRecord)
    {
        startPage();
        List<ScanRecord> list = scanRecordService.selectScanRecordList(scanRecord);
        return getDataTable(list);
    }

    /**
     * 导出扫码记录列表
     */
    @Log(title = "扫码记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ScanRecord scanRecord)
    {
        List<ScanRecord> list = scanRecordService.selectScanRecordList(scanRecord);
        ExcelUtil<ScanRecord> util = new ExcelUtil<ScanRecord>(ScanRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 获取扫码记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('product:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(scanRecordService.selectScanRecordById(id));
    }

    /**
     * 新增扫码记录
     */
    @Log(title = "扫码记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScanRecord scanRecord)
    {
        return toAjax(scanRecordService.insertScanRecord(scanRecord));
    }

    /**
     * 修改扫码记录
     */
    @Log(title = "扫码记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScanRecord scanRecord)
    {
        return toAjax(scanRecordService.updateScanRecord(scanRecord));
    }

    /**
     * 删除扫码记录
     */
    @Log(title = "扫码记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(scanRecordService.deleteScanRecordByIds(ids));
    }
}
