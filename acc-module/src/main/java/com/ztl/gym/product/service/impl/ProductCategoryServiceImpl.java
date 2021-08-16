package com.ztl.gym.product.service.impl;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.product.domain.ProductCategory;
import com.ztl.gym.product.mapper.ProductCategoryMapper;
import com.ztl.gym.product.service.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public ProductCategory selectProductCategoryById(Long id)
    {
        return productCategoryMapper.selectProductCategoryById(id);
    }

    /**
     * 查询产品分类列表
     *
     * @param productCategory 产品分类
     * @return 产品分类
     */
    @Override
    public List<ProductCategory> selectProductCategoryList(ProductCategory productCategory)
    {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            productCategory.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        return productCategoryMapper.selectProductCategoryList(productCategory);
    }



    /**
     * 查询产品分类列表数量
     *
     * @param productCategory 产品分类
     * @return 产品分类
     */
    @Override
    public int selectProductCategoryListCount(ProductCategory productCategory)
    {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            productCategory.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        return productCategoryMapper.selectProductCategoryListCount(productCategory);
    }


    /**
     * 查询产品分类列表
     *
     * @param productCategory 产品分类
     * @return 产品分类
     */
    @Override
    public List<ProductCategory> selectProductCategoryOneList(ProductCategory productCategory)
    {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            productCategory.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        return productCategoryMapper.selectProductCategoryOneList(productCategory);
    }

    /**
     * 新增产品分类
     *
     * @param productCategory 产品分类
     * @return 结果
     */
    @Override
    public int insertProductCategory(ProductCategory productCategory)
    {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            productCategory.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        productCategory.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        productCategory.setCreateTime(DateUtils.getNowDate());
        return productCategoryMapper.insertProductCategory(productCategory);
    }

    /**
     * 修改产品分类
     *
     * @param productCategory 产品分类
     * @return 结果
     */
    @Override
    public int updateProductCategory(ProductCategory productCategory)
    {
        productCategory.setUpdateTime(DateUtils.getNowDate());
        return productCategoryMapper.updateProductCategory(productCategory);
    }

    /**
     * 批量删除产品分类
     *
     * @param ids 需要删除的产品分类ID
     * @return 结果
     */
    @Override
    public int deleteProductCategoryByIds(Long[] ids)
    {
        return productCategoryMapper.deleteProductCategoryByIds(ids);
    }

    /**
     * 删除产品分类信息
     *
     * @param id 产品分类ID
     * @return 结果
     */
    @Override
    public int deleteProductCategoryById(Long id)
    {
        return productCategoryMapper.deleteProductCategoryById(id);
    }

    @Override
    public List<Map<String, Object>> getCategoryDic(Long id) {
        Long temp= SecurityUtils.getLoginUserCompany().getDeptId();
        Long companyId=null;
        if(!temp.equals(AccConstants.ADMIN_DEPT_ID)){
            companyId=SecurityUtils.getLoginUserTopCompanyId();
        }
        return productCategoryMapper.getCategoryDic(id,companyId);
    }
}
