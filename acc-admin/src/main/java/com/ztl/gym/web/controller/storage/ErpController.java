package com.ztl.gym.web.controller.storage;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.storage.domain.Erp;
import com.ztl.gym.storage.service.IErpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 对接ERP主Controller
 *
 * @author ruoyi
 * @date 2022-06-11
 */
@RestController
@RequestMapping("/erp")
public class ErpController extends BaseController
{
    @Autowired
    private IErpService erpService;

    /**
     * 查询对接ERP主列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Erp erp)
    {
        startPage();
        List<Erp> list = erpService.selectErpList(erp);
        return getDataTable(list);
    }

    /**
     * 导出对接ERP主列表
     */
    @Log(title = "对接ERP主", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Erp erp)
    {
        List<Erp> list = erpService.selectErpList(erp);
        ExcelUtil<Erp> util = new ExcelUtil<Erp>(Erp.class);
        return util.exportExcel(list, "erp");
    }

    /**
     * 获取对接ERP主详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(erpService.selectErpById(id));
    }

    /**
     * 新增对接ERP主
     */
    @Log(title = "对接ERP主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Erp erp)
    {
        return toAjax(erpService.insertErp(erp));
    }

    /**
     * 修改对接ERP主
     */
    @Log(title = "对接ERP主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Erp erp)
    {
        return toAjax(erpService.updateErp(erp));
    }

    /**
     * 删除对接ERP主
     */
    @Log(title = "对接ERP主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(erpService.deleteErpByIds(ids));
    }
}
