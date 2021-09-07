package com.ztl.gym.web.controller.product;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.product.domain.ProductBatch;
import com.ztl.gym.product.service.IProductBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品批次Controller
 *
 * @author ruoyi
 * @date 2021-04-14
 */
@RestController
@RequestMapping("/product/batch")
public class ProductBatchController extends BaseController
{
    @Autowired
    private IProductBatchService productBatchService;

    /**
     * 查询产品批次列表
     */
    @PreAuthorize("@ss.hasPermi('product:batch:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam Map<String, Object> param)
    {
        startPage();
        List<Map<String,Object>> list = productBatchService.selectProductBatchList(param);
        return getDataTable(list);
    }



    /**
     * 获取产品批次详细信息
     */
    @PreAuthorize("@ss.hasPermi('product:batch:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(productBatchService.selectProductBatchById(id));
    }

    /**
     * 新增产品批次
     */
    @PreAuthorize("@ss.hasPermi('product:batch:add')")
    @Log(title = "产品批次", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductBatch productBatch)
    {
        //根据企业查询改企业的产品
        List<ProductBatch> productBatchList = productBatchService.selectProductBatchList(productBatch);
        if(productBatchList.size()>0){
            return error("该批次号已存在!!!");
        }
        return toAjax(productBatchService.insertProductBatch(productBatch));
    }

    /**
     * 修改产品批次
     */
    @PreAuthorize("@ss.hasPermi('product:batch:edit')")
    @Log(title = "产品批次", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductBatch productBatch)
    {
        return toAjax(productBatchService.updateProductBatch(productBatch));
    }

    /**
     * 删除产品批次
     */
    @PreAuthorize("@ss.hasPermi('product:batch:remove')")
    @Log(title = "产品批次", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(productBatchService.deleteProductBatchByIds(ids));
    }
}
