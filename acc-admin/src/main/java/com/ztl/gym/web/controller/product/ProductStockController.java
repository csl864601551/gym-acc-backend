package com.ztl.gym.web.controller.product;

import java.util.List;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.SecurityUtils;
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
import com.ztl.gym.product.domain.ProductStock;
import com.ztl.gym.product.service.IProductStockService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 库存统计 Controller
 *
 * @author ruoyi
 * @date 2021-04-25
 */
@RestController
@RequestMapping("/product/stock")
public class ProductStockController extends BaseController {
    @Autowired
    private IProductStockService productStockService;

    /**
     * 查询库存统计 列表
     */
    @PreAuthorize("@ss.hasPermi('product:stock:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductStock productStock) {
        startPage();
        if (!SecurityUtils.getLoginUserCompany().getDeptId().equals(AccConstants.ADMIN_DEPT_ID)) {
            productStock.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
            productStock.setTenantId(SecurityUtils.getLoginUserCompany().getDeptId());
            productStock.setStockLevel(AccConstants.STOCK_LEVEL_COMPANY);
            if (SecurityUtils.getLoginUserTopCompanyId() != SecurityUtils.getLoginUserCompany().getDeptId()) {
                productStock.setStockLevel(AccConstants.STOCK_LEVEL_TENANT);
            }
        }

        List<ProductStock> list = productStockService.selectProductStockList(productStock);
        return getDataTable(list);
    }

    /**fdfd
     * 查询企业库存统计
     */
    @PreAuthorize("@ss.hasPermi('product:stock:listByTenant')")
    @GetMapping("/listByTenant")
    public TableDataInfo listByTenant(ProductStock productStock) {
        startPage();
        List<ProductStock> list = productStockService.selectProductStockList(productStock);
        return getDataTable(list);
    }

    /**
     * 导出库存统计 列表
     */
    @PreAuthorize("@ss.hasPermi('product:stock:export')")
    @Log(title = "库存统计 ", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ProductStock productStock) {
        List<ProductStock> list = productStockService.selectProductStockList(productStock);
        ExcelUtil<ProductStock> util = new ExcelUtil<ProductStock>(ProductStock.class);
        return util.exportExcel(list, "stock");
    }

    /**
     * 获取库存统计 详细信息
     */
    @PreAuthorize("@ss.hasPermi('product:stock:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(productStockService.selectProductStockById(id));
    }

    /**
     * 新增库存统计
     */
    @PreAuthorize("@ss.hasPermi('product:stock:add')")
    @Log(title = "库存统计 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductStock productStock) {
        return toAjax(2);
    }

    /**
     * 修改库存统计
     */
    @PreAuthorize("@ss.hasPermi('product:stock:edit')")
    @Log(title = "库存统计 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductStock productStock) {
        return toAjax(productStockService.updateProductStock(productStock));
    }

    /**
     * 删除库存统计
     */
    @PreAuthorize("@ss.hasPermi('product:stock:remove')")
    @Log(title = "库存统计 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(productStockService.deleteProductStockByIds(ids));
    }
}
