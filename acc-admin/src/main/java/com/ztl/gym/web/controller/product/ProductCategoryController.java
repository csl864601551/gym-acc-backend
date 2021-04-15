package com.ztl.gym.web.controller.product;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.core.text.Convert;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.product.domain.ProductCategory;
import com.ztl.gym.product.service.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品分类Controller
 *
 * @author ruoyi
 * @date 2021-04-12
 */
@RestController
@RequestMapping("/product/category")
public class ProductCategoryController extends BaseController
{
    @Autowired
    private IProductCategoryService tProductCategoryService;

    @GetMapping("/getCategory")
    public List<ProductCategory> getCategory(@RequestParam("type") String type, @RequestParam("id") String id)
    {
        List<ProductCategory> categoryList = null;
        ProductCategory productCategory = new ProductCategory();
        if(type.equals("1")) {
            //一级类型
            productCategory.setpId(Convert.toLong(0));
        } else if(type.equals("2")) {
            //二级类型
            productCategory.setpId(Convert.toLong(id));
        }
        categoryList = tProductCategoryService.selectTProductCategoryList(productCategory);
        return categoryList;
    }
    @GetMapping("/getCategoryDic")
    public List<Map<String,Object>> getCategoryDic()
    {
        List<Map<String,Object>> categoryDic = tProductCategoryService.getCategoryDic();
        return categoryDic;
    }

    /**
     * 查询产品分类列表
     */
    @PreAuthorize("@ss.hasPermi('product:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductCategory productCategory)
    {
        startPage();
        List<ProductCategory> list = tProductCategoryService.selectTProductCategoryList(productCategory);
        return getDataTable(list);
    }

    /**
     * 导出产品分类列表
     */
    @PreAuthorize("@ss.hasPermi('product:category:export')")
    @Log(title = "产品分类", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ProductCategory productCategory)
    {
        List<ProductCategory> list = tProductCategoryService.selectTProductCategoryList(productCategory);
        ExcelUtil<ProductCategory> util = new ExcelUtil<ProductCategory>(ProductCategory.class);
        return util.exportExcel(list, "category");
    }

    /**
     * 获取产品分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('product:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tProductCategoryService.selectTProductCategoryById(id));
    }

    /**
     * 新增产品分类
     */
    @PreAuthorize("@ss.hasPermi('product:category:add')")
    @Log(title = "产品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductCategory productCategory)
    {
        return toAjax(tProductCategoryService.insertTProductCategory(productCategory));
    }

    /**
     * 修改产品分类
     */
    @PreAuthorize("@ss.hasPermi('product:category:edit')")
    @Log(title = "产品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductCategory productCategory)
    {
        return toAjax(tProductCategoryService.updateTProductCategory(productCategory));
    }

    /**
     * 删除产品分类
     */
    @PreAuthorize("@ss.hasPermi('product:category:remove')")
    @Log(title = "产品分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tProductCategoryService.deleteTProductCategoryByIds(ids));
    }
}
