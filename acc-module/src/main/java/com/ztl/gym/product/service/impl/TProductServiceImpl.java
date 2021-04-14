package com.ztl.gym.product.service.impl;

import java.util.List;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.product.domain.TProduct;
import com.ztl.gym.product.domain.TProductCategory;
import com.ztl.gym.product.mapper.TProductMapper;
import com.ztl.gym.product.service.ITProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 产品Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-10
 */
@Service
public class TProductServiceImpl implements ITProductService
{
    @Autowired
    private TProductMapper tProductMapper;

    /**
     * 查询产品
     *
     * @param id 产品ID
     * @return 产品
     */
    @Override
    public TProduct selectTProductById(Long id)
    {
        return tProductMapper.selectTProductById(id);
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
        return tProductMapper.selectTProductList(tProduct);
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
        return tProductMapper.insertTProduct(tProduct);
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
        return tProductMapper.updateTProduct(tProduct);
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
        return tProductMapper.deleteTProductByIds(ids);
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
        return tProductMapper.deleteTProductById(id);
    }
}
