package com.ztl.gym.web.controller.code;

import java.util.List;

import com.ztl.gym.code.domain.CodeOperationLog;
import com.ztl.gym.code.service.ICodeOperationLogService;
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
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 操作日志Controller
 *
 * @author ruoyi
 * @date 2021-08-05
 */
@RestController
@RequestMapping("/operation/log")
public class CodeOperationLogController extends BaseController
{
    @Autowired
    private ICodeOperationLogService codeOperationLogService;

    /**
     * 查询操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('operation:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(CodeOperationLog codeOperationLog)
    {
        startPage();
        List<CodeOperationLog> list = codeOperationLogService.selectCodeOperationLogList(codeOperationLog);
        return getDataTable(list);
    }

//    /**
//     * 导出操作日志列表
//     */
//    @PreAuthorize("@ss.hasPermi('product:log:export')")
//    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(CodeOperationLog codeOperationLog)
//    {
//        List<CodeOperationLog> list = codeOperationLogService.selectCodeOperationLogList(codeOperationLog);
//        ExcelUtil<CodeOperationLog> util = new ExcelUtil<CodeOperationLog>(CodeOperationLog.class);
//        return util.exportExcel(list, "log");
//    }
//
//    /**
//     * 获取操作日志详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('product:log:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return AjaxResult.success(codeOperationLogService.selectCodeOperationLogById(id));
//    }
//
//    /**
//     * 新增操作日志
//     */
//    @PreAuthorize("@ss.hasPermi('product:log:add')")
//    @Log(title = "操作日志", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody CodeOperationLog codeOperationLog)
//    {
//        return toAjax(codeOperationLogService.insertCodeOperationLog(codeOperationLog));
//    }
//
//    /**
//     * 修改操作日志
//     */
//    @PreAuthorize("@ss.hasPermi('product:log:edit')")
//    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody CodeOperationLog codeOperationLog)
//    {
//        return toAjax(codeOperationLogService.updateCodeOperationLog(codeOperationLog));
//    }
//
//    /**
//     * 删除操作日志
//     */
//    @PreAuthorize("@ss.hasPermi('product:log:remove')")
//    @Log(title = "操作日志", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(codeOperationLogService.deleteCodeOperationLogByIds(ids));
//    }
}
