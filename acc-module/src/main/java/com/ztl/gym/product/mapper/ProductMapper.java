package com.ztl.gym.product.mapper;

import java.util.List;

import com.ztl.gym.product.domain.TProduct;

/**
 * 产品Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-10
 */
public interface ProductMapper
{
    /**
     * 查询产品
     *
     * @param id 产品ID
     * @return 产品
     */
    public TProduct selectTProductById(Long id);

    /**
     * 查询产品列表
     *
     * @param tProduct 产品
     * @return 产品集合
     */
    public List<TProduct> selectTProductList(TProduct tProduct);

    /**
     * 新增产品
     *
     * @param tProduct 产品
     * @return 结果
     */
    public int insertTProduct(TProduct tProduct);

    /**
     * 修改产品
     *
     * @param tProduct 产品
     * @return 结果
     */
    public int updateTProduct(TProduct tProduct);

    /**
     * 删除产品
     *
     * @param id 产品ID
     * @return 结果
     */
    public int deleteTProductById(Long id);

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTProductByIds(Long[] ids);
}
