package com.ztl.gym.web.controller.product;

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
import com.ztl.gym.product.domain.ProductStockFlow;
import com.ztl.gym.product.service.IProductStockFlowService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 *  产品库存明细Controller
 *
 * @author ruoyi
 * @date 2021-04-25
 */
@RestController
@RequestMapping("/product/stockFlow")
public class ProductStockFlowController extends BaseController
{
    @Autowired
    private IProductStockFlowService productStockFlowService;

    /**
     * 查询 产品库存明细列表
     */
    @PreAuthorize("@ss.hasPermi('product:stockFlow:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductStockFlow productStockFlow)
    {
        startPage();
        List<ProductStockFlow> list = productStockFlowService.selectProductStockFlowList(productStockFlow);
        return getDataTable(list);
    }

    /**
     * 导出 产品库存明细列表
     */
    @PreAuthorize("@ss.hasPermi('product:stockFlow:export')")
    @Log(title = " 产品库存明细", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ProductStockFlow productStockFlow)
    {
        List<ProductStockFlow> list = productStockFlowService.selectProductStockFlowList(productStockFlow);
        ExcelUtil<ProductStockFlow> util = new ExcelUtil<ProductStockFlow>(ProductStockFlow.class);
        return util.exportExcel(list, "stockFlow");
    }

    /**
     * 获取 产品库存明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('product:stockFlow:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(productStockFlowService.selectProductStockFlowById(id));
    }

    /**
     * 新增 产品库存明细
     */
    @PreAuthorize("@ss.hasPermi('product:stockFlow:add')")
    @Log(title = " 产品库存明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductStockFlow productStockFlow)
    {
        return toAjax(productStockFlowService.insertProductStockFlow(productStockFlow));
    }

    /**
     * 修改 产品库存明细
     */
    @PreAuthorize("@ss.hasPermi('product:stockFlow:edit')")
    @Log(title = " 产品库存明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductStockFlow productStockFlow)
    {
        return toAjax(productStockFlowService.updateProductStockFlow(productStockFlow));
    }

    /**
     * 删除 产品库存明细
     */
    @PreAuthorize("@ss.hasPermi('product:stockFlow:remove')")
    @Log(title = " 产品库存明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(productStockFlowService.deleteProductStockFlowByIds(ids));
    }
}
