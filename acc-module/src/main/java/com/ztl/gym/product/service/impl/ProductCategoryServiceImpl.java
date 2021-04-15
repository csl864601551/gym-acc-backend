package com.ztl.gym.product.service.impl;

import java.util.List;
import java.util.Map;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.product.domain.ProductCategory;
import com.ztl.gym.product.mapper.ProductCategoryMapper;
import com.ztl.gym.product.service.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 产品分类Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-12
 */
@Service
public class ProductCategoryServiceImpl implements IProductCategoryService
{
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    /**
     * 查询产品分类
     *
     * @param id 产品分类ID
     * @return 产品分类
     */
    @Override
    public ProductCategory selectTProductCategoryById(Long id)
    {
        return productCategoryMapper.selectTProductCategoryById(id);
    }

    /**
     * 查询产品分类列表
     *
     * @param productCategory 产品分类
     * @return 产品分类
     */
    @Override
    public List<ProductCategory> selectTProductCategoryList(ProductCategory productCategory)
    {
        return productCategoryMapper.selectTProductCategoryList(productCategory);
    }

    /**
     * 新增产品分类
     *
     * @param productCategory 产品分类
     * @return 结果
     */
    @Override
    public int insertTProductCategory(ProductCategory productCategory)
    {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            productCategory.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        productCategory.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        productCategory.setCreateTime(DateUtils.getNowDate());
        return productCategoryMapper.insertTProductCategory(productCategory);
    }

    /**
     * 修改产品分类
     *
     * @param productCategory 产品分类
     * @return 结果
     */
    @Override
    public int updateTProductCategory(ProductCategory productCategory)
    {
        productCategory.setUpdateTime(DateUtils.getNowDate());
        return productCategoryMapper.updateTProductCategory(productCategory);
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
        return productCategoryMapper.deleteTProductCategoryByIds(ids);
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
        return productCategoryMapper.deleteTProductCategoryById(id);
    }

    @Override
    public List<Map<String, Object>> getCategoryDic() {
        return null;
    }
}
