package com.ztl.gym.product.service;

import java.util.List;

import com.ztl.gym.product.domain.TProductCategory;

/**
 * 产品分类Service接口
 * 
 * @author ruoyi
 * @date 2021-04-12
 */
public interface ITProductCategoryService 
{
    /**
     * 查询产品分类
     * 
     * @param id 产品分类ID
     * @return 产品分类
     */
    public TProductCategory selectTProductCategoryById(Long id);

    /**
     * 查询产品分类列表
     * 
     * @param tProductCategory 产品分类
     * @return 产品分类集合
     */
    public List<TProductCategory> selectTProductCategoryList(TProductCategory tProductCategory);

    /**
     * 新增产品分类
     * 
     * @param tProductCategory 产品分类
     * @return 结果
     */
    public int insertTProductCategory(TProductCategory tProductCategory);

    /**
     * 修改产品分类
     * 
     * @param tProductCategory 产品分类
     * @return 结果
     */
    public int updateTProductCategory(TProductCategory tProductCategory);

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
}
