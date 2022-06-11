package com.ztl.gym.web.controller.storage;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.storage.domain.ErpDetail;
import com.ztl.gym.storage.service.IErpDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 对接ERP明细Controller
 *
 * @author ruoyi
 * @date 2022-06-11
 */
@RestController
@RequestMapping("/erp/detail")
public class ErpDetailController extends BaseController
{
    @Autowired
    private IErpDetailService erpDetailService;

    /**
     * 查询对接ERP明细列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ErpDetail erpDetail)
    {
        startPage();
        List<ErpDetail> list = erpDetailService.selectErpDetailList(erpDetail);
        return getDataTable(list);
    }

    /**
     * 导出对接ERP明细列表
     */
    @Log(title = "对接ERP明细", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ErpDetail erpDetail)
    {
        List<ErpDetail> list = erpDetailService.selectErpDetailList(erpDetail);
        ExcelUtil<ErpDetail> util = new ExcelUtil<ErpDetail>(ErpDetail.class);
        return util.exportExcel(list, "detail");
    }

    /**
     * 获取对接ERP明细详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(erpDetailService.selectErpDetailById(id));
    }

    /**
     * 新增对接ERP明细
     */
    @Log(title = "对接ERP明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ErpDetail erpDetail)
    {
        return toAjax(erpDetailService.insertErpDetail(erpDetail));
    }

    /**
     * 修改对接ERP明细
     */
    @Log(title = "对接ERP明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ErpDetail erpDetail)
    {
        return toAjax(erpDetailService.updateErpDetail(erpDetail));
    }

    /**
     * 删除对接ERP明细
     */
    @Log(title = "对接ERP明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(erpDetailService.deleteErpDetailByIds(ids));
    }
}
