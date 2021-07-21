package com.ztl.gym.web.controller.product;

import cn.hutool.core.util.StrUtil;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.product.domain.Attr;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.service.IAttrService;
import com.ztl.gym.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private IAttrService attrService;

    /**
     * 查询产品列表
     */
    @PreAuthorize("@ss.hasPermi('product:show:list')")
    @GetMapping("/list")
    public TableDataInfo list(Product product)
    {
        List<Product> lists = new ArrayList<Product>();
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
        if(StrUtil.isNotEmpty(product.getProductName())&&StrUtil.isNotEmpty(product.getProductNo())){
            Product productquery = new Product();
            productquery.setProductName(product.getProductName());
            productquery.setProductNo(product.getProductNo());
            List<Product> list = tProductService.selectTProductList1(productquery);
            if(list.size()>0){
                return error("该产品编号和产品名称已存在！！！");
            }
        }
        //判断属性列表的值是否对应
        boolean istrue = true;
        if(product.getAttributeList()!=null){
            if(product.getAttributeList().size()>0){
                for(int i=0; i<product.getAttributeList().size(); i++){
                    Map<String,Object> topmap = new HashMap<String,Object>();
                    topmap = product.getAttributeList().get(i);
                    String name = topmap.get("attrNameCn").toString();
                    String value = topmap.get("attrValue").toString();
                    Attr attrinfo = attrService.selectAttrByName(name);
                    if(attrinfo!=null){
                        if(attrinfo.getInputType()==1){

                        }else if(attrinfo.getInputType()==2){
                            List<Map<String,Object>> attlist = attrService.getAttrValuesById(attrinfo.getId());
                            if(attlist.size()>0){
                                for(int j=0; j<attlist.size(); j++){
                                    Map<String,Object> secendmap = new HashMap<String,Object>();
                                    secendmap = attlist.get(j);
                                    String twovalue = secendmap.get("value").toString();
                                    if(twovalue.equals(value)){
                                        istrue = false;
                                    }
                                }
                                if(istrue){
                                    return error("产品属性类型和值不一致！！！");
                                }
                            }
                        }
                    }
                }
            }
        }
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
        if(StrUtil.isNotEmpty(product.getUpdateType())){

        }else{
            //判断属性列表的值是否对应
            boolean istrue = true;
            if(product.getAttributeList()!=null) {
                if (product.getAttributeList().size() > 0) {
                    for (int i = 0; i < product.getAttributeList().size(); i++) {
                        Map<String, Object> topmap = new HashMap<String, Object>();
                        topmap = product.getAttributeList().get(i);
                        String name = topmap.get("attrNameCn").toString();
                        String value = topmap.get("attrValue").toString();
                        Attr attrinfo = attrService.selectAttrByName(name);
                        if (attrinfo != null) {
                            if (attrinfo.getInputType() == 1) {

                            } else if (attrinfo.getInputType() == 2) {
                                List<Map<String, Object>> attlist = attrService.getAttrValuesById(attrinfo.getId());
                                if (attlist.size() > 0) {
                                    for (int j = 0; j < attlist.size(); j++) {
                                        Map<String, Object> secendmap = new HashMap<String, Object>();
                                        secendmap = attlist.get(j);
                                        String twovalue = secendmap.get("value").toString();
                                        if (twovalue.equals(value)) {
                                            istrue = false;
                                        }
                                    }
                                    if (istrue) {
                                        return error("产品属性类型和值不一致！！！");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
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
