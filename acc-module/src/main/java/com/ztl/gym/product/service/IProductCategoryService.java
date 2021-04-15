package com.ztl.gym.product.service;

import java.util.List;
import java.util.Map;

import com.ztl.gym.product.domain.ProductCategory;

/**
 * 产品分类Service接口
 * 
 * @author ruoyi
 * @date 2021-04-12
 */
public interface IProductCategoryService 
{
    /**
     * 查询产品分类
     * 
     * @param id 产品分类ID
     * @return 产品分类
     */
    public ProductCategory selectTProductCategoryById(Long id);

    /**
     * 查询产品分类列表
     * 
     * @param productCategory 产品分类
     * @return 产品分类集合
     */
    public List<ProductCategory> selectTProductCategoryList(ProductCategory productCategory);

    /**
     * 新增产品分类
     * 
     * @param productCategory 产品分类
     * @return 结果
     */
    public int insertTProductCategory(ProductCategory productCategory);

    /**
     * 修改产品分类
     * 
     * @param productCategory 产品分类
     * @return 结果
     */
    public int updateTProductCategory(ProductCategory productCategory);

    /**
     * 批量删除产品分类
     * 
     * @param ids 需要删除的产品分类ID
     * @return 结果
     */
    public int deleteTProductCategoryByIds(Long[] ids);

    /**
     * 删除产品分类信息
     * 
     * @param id 产品分类ID
     * @return 结果
     */
    public int deleteTProductCategoryById(Long id);

    List<Map<String, Object>> getCategoryDic();
}
