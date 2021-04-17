package com.ztl.gym.product.service;

import java.util.List;

import com.ztl.gym.product.domain.Product;

/**
 * 产品Service接口
 *
 * @author ruoyi
 * @date 2021-04-10
 */
public interface IProductService
{
    /**
     * 查询产品
     *
     * @param id 产品ID
     * @return 产品
     */
    public Product selectTProductById(Long id);

    /**
     * 查询产品列表
     *
     * @param product 产品
     * @return 产品集合
     */
    public List<Product> selectTProductList(Product product);

    /**
     * 新增产品
     *
     * @param product 产品
     * @return 结果
     */
    public int insertTProduct(Product product);

    /**
     * 修改产品
     *
     * @param product 产品
     * @return 结果
     */
    public int updateTProduct(Product product);

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的产品ID
     * @return 结果
     */
    public int deleteTProductByIds(Long[] ids);

    public int deleteTProductTrueByIds(Long[] ids);

    /**
     * 删除产品信息
     *
     * @param id 产品ID
     * @return 结果
     */
    public int deleteTProductById(Long id);
}
