package com.ztl.gym.web.controller.idis;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.idis.domain.IdisRecord;
import com.ztl.gym.idis.service.IIdisRecordService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * IDIS同步记录Controller
 *
 * @author zt_sly
 * @date 2021-07-21
 */
@RestController
@RequestMapping("/idis/record")
public class IdisRecordController extends BaseController {

    @Autowired
    private IIdisRecordService idisRecordService;

    /**
     * 查询IDIS同步记录列表
     */
    @PreAuthorize("@ss.hasPermi('idis:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(IdisRecord idisRecord) {
        startPage();
        List<IdisRecord> list = idisRecordService.selectIdisRecordList(idisRecord);
        return getDataTable(list);
    }

    /**
     * 导出IDIS同步记录列表
     */
    @PreAuthorize("@ss.hasPermi('idis:record:export')")
    @Log(title = "IDIS同步记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IdisRecord idisRecord) {
        List<IdisRecord> list = idisRecordService.selectIdisRecordList(idisRecord);
        ExcelUtil<IdisRecord> util = new ExcelUtil<>(IdisRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 获取IDIS同步记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('idis:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(idisRecordService.selectIdisRecordById(id));
    }

    /**
     * 删除IDIS同步记录
     */
    @PreAuthorize("@ss.hasPermi('idis:record:remove')")
    @Log(title = "IDIS同步记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(idisRecordService.deleteIdisRecordByIds(ids));
    }

}
