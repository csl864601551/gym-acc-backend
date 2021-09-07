package com.ztl.gym.product.service.impl;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.product.domain.ProductBatch;
import com.ztl.gym.product.mapper.ProductBatchMapper;
import com.ztl.gym.product.service.IProductBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 产品批次Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-14
 */
@Service
public class ProductBatchServiceImpl implements IProductBatchService
{
    @Autowired
    private ProductBatchMapper productBatchMapper;

    /**
     * 查询产品批次
     *
     * @param id 产品批次ID
     * @return 产品批次
     */
    @Override
    public ProductBatch selectProductBatchById(Long id)
    {
        return productBatchMapper.selectProductBatchById(id);
    }

    /**
     * 查询产品批次列表
     *
     * @return 产品批次
     */
    @Override
    public List<Map<String,Object>> selectProductBatchList(Map<String, Object> param)
    {
        Long company_id=SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            param.put("companyId",SecurityUtils.getLoginUserTopCompanyId());
        }
        return productBatchMapper.selectProductBatchList(param);
    }
    @Override
    public List<ProductBatch> selectProductBatchList(ProductBatch productBatch)
    {
        return productBatchMapper.selectProductBatchListV2(productBatch);
    }

    /**
     * 新增产品批次
     *
     * @param productBatch 产品批次
     * @return 结果
     */
    @Override
    public int insertProductBatch(ProductBatch productBatch)
    {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            productBatch.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        productBatch.setCreateTime(DateUtils.getNowDate());
        productBatch.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return productBatchMapper.insertProductBatch(productBatch);
    }



    /**
     * 新增产品批次
     *
     * @param productBatch 产品批次
     * @return 结果
     */
    @Override
    public int insertProductBatchOne(ProductBatch productBatch)
    {
        return productBatchMapper.insertProductBatch(productBatch);
    }

    /**
     * 修改产品批次
     *
     * @param productBatch 产品批次
     * @return 结果
     */
    @Override
    public int updateProductBatch(ProductBatch productBatch)
    {
        productBatch.setUpdateTime(DateUtils.getNowDate());
        return productBatchMapper.updateProductBatch(productBatch);
    }

    /**
     * 批量删除产品批次
     *
     * @param ids 需要删除的产品批次ID
     * @return 结果
     */
    @Override
    public int deleteProductBatchByIds(Long[] ids)
    {
        return productBatchMapper.deleteProductBatchByIds(ids);
    }

    /**
     * 删除产品批次信息
     *
     * @param id 产品批次ID
     * @return 结果
     */
    @Override
    public int deleteProductBatchById(Long id)
    {
        return productBatchMapper.deleteProductBatchById(id);
    }
}
