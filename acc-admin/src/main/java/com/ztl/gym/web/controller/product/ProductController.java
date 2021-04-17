package com.ztl.gym.web.controller.product;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品Controller
 *
 * @author ruoyi
 * @date 2021-04-10
 */
@RestController
@RequestMapping("/product/show")
public class ProductController extends BaseController
{
    @Autowired
    private IProductService tProductService;

    /**
     * 查询产品列表
     */
    @PreAuthorize("@ss.hasPermi('product:show:list')")
    @GetMapping("/list")
    public TableDataInfo list(Product product)
    {
        startPage();
        List<Product> list = tProductService.selectTProductList(product);
        return getDataTable(list);
    }

    /**
     * 导出产品列表
     */
    @PreAuthorize("@ss.hasPermi('product:show:export')")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Product product)
    {
        List<Product> list = tProductService.selectTProductList(product);
        ExcelUtil<Product> util = new ExcelUtil<Product>(Product.class);
        return util.exportExcel(list, "product");
    }

    /**
     * 获取产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('product:show:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tProductService.selectTProductById(id));
    }

    /**
     * 新增产品
     */
    @PreAuthorize("@ss.hasPermi('product:show:add')")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Product product)
    {
        return toAjax(tProductService.insertTProduct(product));
    }

    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('product:show:edit')")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Product product)
    {
        return toAjax(tProductService.updateTProduct(product));
    }

    /**
     * 删除产品
     */
    @PreAuthorize("@ss.hasPermi('product:show:remove')")
    @Log(title = "产品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tProductService.deleteTProductByIds(ids));
    }

    /**
     * 删除产品
     */
    @PreAuthorize("@ss.hasPermi('product:del:remove')")
    @Log(title = "产品", businessType = BusinessType.DELETE)
    @DeleteMapping("/del/{ids}")
    public AjaxResult removeTrue(@PathVariable Long[] ids)
    {
        return toAjax(tProductService.deleteTProductTrueByIds(ids));
    }
}
