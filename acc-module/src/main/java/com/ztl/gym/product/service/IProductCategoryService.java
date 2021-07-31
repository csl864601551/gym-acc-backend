package com.ztl.gym.product.service;

import com.ztl.gym.product.domain.ProductCategory;

import java.util.List;
import java.util.Map;

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
    public ProductCategory selectProductCategoryById(Long id);

    /**
     * 查询产品分类列表
     * 
     * @param productCategory 产品分类
     * @return 产品分类集合
     */
    public List<ProductCategory> selectProductCategoryList(ProductCategory productCategory);


    /**
     * 查询产品分类列表数量
     *
     * @param productCategory 产品分类
     * @return 产品分类集合
     */
    public int  selectProductCategoryListCount(ProductCategory productCategory);

    /**
     * 查询产品分类列表
     *
     * @param productCategory 产品分类
     * @return 产品分类集合
     */
    public List<ProductCategory> selectProductCategoryOneList(ProductCategory productCategory);

    /**
     * 新增产品分类
     * 
     * @param productCategory 产品分类
     * @return 结果
     */
    public int insertProductCategory(ProductCategory productCategory);

    /**
     * 修改产品分类
     * 
     * @param productCategory 产品分类
     * @return 结果
     */
    public int updateProductCategory(ProductCategory productCategory);

    /**
     * 批量删除产品分类
     * 
     * @param ids 需要删除的产品分类ID
     * @return 结果
     */
    public int deleteProductCategoryByIds(Long[] ids);

    /**
     * 删除产品分类信息
     * 
     * @param id 产品分类ID
     * @return 结果
     */
    public int deleteProductCategoryById(Long id);

    List<Map<String, Object>> getCategoryDic(Long id);
}
