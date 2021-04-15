package com.ztl.gym.product.service.impl;

import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.product.domain.TProduct;
import com.ztl.gym.product.mapper.ProductMapper;
import com.ztl.gym.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-10
 */
@Service
public class ProductServiceImpl implements IProductService
{
    @Autowired
    private ProductMapper productMapper;

    /**
     * 查询产品
     *
     * @param id 产品ID
     * @return 产品
     */
    @Override
    public TProduct selectTProductById(Long id)
    {
        return productMapper.selectTProductById(id);
    }

    /**
     * 查询产品列表
     *
     * @param tProduct 产品
     * @return 产品
     */
    @Override
    public List<TProduct> selectTProductList(TProduct tProduct)
    {
        return productMapper.selectTProductList(tProduct);
    }

    /**
     * 新增产品
     *
     * @param tProduct 产品
     * @return 结果
     */
    @Override
    public int insertTProduct(TProduct tProduct)
    {
        tProduct.setCreateTime(DateUtils.getNowDate());
        return productMapper.insertTProduct(tProduct);
    }

    /**
     * 修改产品
     *
     * @param tProduct 产品selectTProductList
     * @return 结果
     */
    @Override
    public int updateTProduct(TProduct tProduct)
    {
        tProduct.setUpdateTime(DateUtils.getNowDate());
        return productMapper.updateTProduct(tProduct);
    }

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的产品ID
     * @return 结果
     */
    @Override
    public int deleteTProductByIds(Long[] ids)
    {
        return productMapper.deleteTProductByIds(ids);
    }

    /**
     * 删除产品信息
     *
     * @param id 产品ID
     * @return 结果
     */
    @Override
    public int deleteTProductById(Long id)
    {
        return productMapper.deleteTProductById(id);
    }
}
