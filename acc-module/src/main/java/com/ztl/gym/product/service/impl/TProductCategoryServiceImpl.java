package com.ztl.gym.product.service.impl;

import java.util.List;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.product.domain.TProductCategory;
import com.ztl.gym.product.mapper.TProductCategoryMapper;
import com.ztl.gym.product.service.ITProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 产品分类Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-12
 */
@Service
public class TProductCategoryServiceImpl implements ITProductCategoryService
{
    @Autowired
    private TProductCategoryMapper tProductCategoryMapper;

    /**
     * 查询产品分类
     *
     * @param id 产品分类ID
     * @return 产品分类
     */
    @Override
    public TProductCategory selectTProductCategoryById(Long id)
    {
        return tProductCategoryMapper.selectTProductCategoryById(id);
    }

    /**
     * 查询产品分类列表
     *
     * @param tProductCategory 产品分类
     * @return 产品分类
     */
    @Override
    public List<TProductCategory> selectTProductCategoryList(TProductCategory tProductCategory)
    {
        return tProductCategoryMapper.selectTProductCategoryList(tProductCategory);
    }

    /**
     * 新增产品分类
     *
     * @param tProductCategory 产品分类
     * @return 结果
     */
    @Override
    public int insertTProductCategory(TProductCategory tProductCategory)
    {
        tProductCategory.setCreateTime(DateUtils.getNowDate());
        return tProductCategoryMapper.insertTProductCategory(tProductCategory);
    }

    /**
     * 修改产品分类
     *
     * @param tProductCategory 产品分类
     * @return 结果
     */
    @Override
    public int updateTProductCategory(TProductCategory tProductCategory)
    {
        tProductCategory.setUpdateTime(DateUtils.getNowDate());
        return tProductCategoryMapper.updateTProductCategory(tProductCategory);
    }

    /**
     * 批量删除产品分类
     *
     * @param ids 需要删除的产品分类ID
     * @return 结果
     */
    @Override
    public int deleteTProductCategoryByIds(Long[] ids)
    {
        return tProductCategoryMapper.deleteTProductCategoryByIds(ids);
    }

    /**
     * 删除产品分类信息
     *
     * @param id 产品分类ID
     * @return 结果
     */
    @Override
    public int deleteTProductCategoryById(Long id)
    {
        return tProductCategoryMapper.deleteTProductCategoryById(id);
    }
}
