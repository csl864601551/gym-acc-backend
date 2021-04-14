package com.ztl.gym.web.controller.product;

import com.ztl.gym.common.core.text.Convert;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.product.domain.TProductCategory;
import com.ztl.gym.product.service.ITProductCategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 产品分类Controller
 *
 * @author ruoyi
 * @date 2021-04-12
 */
@RestController
@RequestMapping("/product/category")
public class TProductCategoryController extends BaseController
{
    @Autowired
    private ITProductCategoryService tProductCategoryService;

    @GetMapping("/getCategory")
    public List<TProductCategory> getCategory(@RequestParam("type") String type, @RequestParam("id") String id)
    {remove
        List<TProductCategory> categoryList = null;
        TProductCategory tProductCategory = new TProductCategory();
        if(type.equals("1")) {
            //一级类型
            tProductCategory.setpId(Convert.toLong(0));
        } else if(type.equals("2")) {
            //二级类型
            tProductCategory.setpId(Convert.toLong(id));
        }
        categoryList = tProductCategoryService.selectTProductCategoryList(tProductCategory);
        return categoryList;
    }

    /**
     * 查询产品分类列表
     */
    @PreAuthorize("@ss.hasPermi('system:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(TProductCategory tProductCategory)
    {
        startPage();
        List<TProductCategory> list = tProductCategoryService.selectTProductCategoryList(tProductCategory);
        return getDataTable(list);
    }

    /**
     * 导出产品分类列表
     */
    @PreAuthorize("@ss.hasPermi('system:category:export')")
    @Log(title = "产品分类", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TProductCategory tProductCategory)
    {
        List<TProductCategory> list = tProductCategoryService.selectTProductCategoryList(tProductCategory);
        ExcelUtil<TProductCategory> util = new ExcelUtil<TProductCategory>(TProductCategory.class);
        return util.exportExcel(list, "category");
    }

    /**
     * 获取产品分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tProductCategoryService.selectTProductCategoryById(id));
    }

    /**
     * 新增产品分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:add')")
    @Log(title = "产品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TProductCategory tProductCategory)
    {
        return toAjax(tProductCategoryService.insertTProductCategory(tProductCategory));
    }

    /**
     * 修改产品分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:edit')")
    @Log(title = "产品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TProductCategory tProductCategory)
    {
        return toAjax(tProductCategoryService.updateTProductCategory(tProductCategory));
    }

    /**
     * 删除产品分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:remove')")
    @Log(title = "产品分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tProductCategoryService.deleteTProductCategoryByIds(ids));
    }
}
