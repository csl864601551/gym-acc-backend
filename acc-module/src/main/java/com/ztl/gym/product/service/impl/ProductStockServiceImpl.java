package com.ztl.gym.product.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.product.domain.ProductStockFlow;
import com.ztl.gym.product.service.IProductStockFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.product.mapper.ProductStockMapper;
import com.ztl.gym.product.domain.ProductStock;
import com.ztl.gym.product.service.IProductStockService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存统计 Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-25
 */
@Service
public class ProductStockServiceImpl implements IProductStockService {
    @Autowired
    private ProductStockMapper productStockMapper;
    @Autowired
    private IProductStockFlowService productStockFlowService;

    /**
     * 查询库存统计
     *
     * @param id 库存统计 ID
     * @return 库存统计
     */
    @Override
    public ProductStock selectProductStockById(Long id) {
        return productStockMapper.selectProductStockById(id);
    }

    /**
     * 根据仓库与产品查询库存信息
     *
     * @param storageId
     * @param productId
     * @return
     */
    @Override
    public ProductStock selectProductStockByParam(long storageId, long productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("storageId", storageId);
        params.put("productId", productId);
        return productStockMapper.selectProductStockByParam(params);
    }

    /**
     * 查询库存统计 列表
     *
     * @param productStock 库存统计
     * @return 库存统计
     */
    @Override
    public List<ProductStock> selectProductStockList(ProductStock productStock) {
        return productStockMapper.selectProductStockList(productStock);
    }

    /**
     * 新增库存统计
     *
     * @param storageId       仓库id
     * @param productId       产品id
     * @param storageType     流转类型
     * @param storageRecordId 流转记录id
     * @param flowNum         变动数量
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertProductStock(long tenantId,long storageId, long productId, int storageType, long storageRecordId, int flowNum) {
        long companyId = 0;
        int flowBefore = 0;
        int flowAfter = 0;
        int res = 0;

        if (SecurityUtils.getLoginUserCompany().getDeptId() != AccConstants.ADMIN_DEPT_ID) {
            companyId = SecurityUtils.getLoginUserTopCompanyId();
        } else {
            throw new CustomException("平台无库存操作需要");
        }
        if (companyId > 0) {
            //判断当前仓库 当前产品是否存在库存信息
            Map<String, Object> params = new HashMap<>();
            params.put("storageId", storageId);
            params.put("productId", productId);
            ProductStock stockInfo = productStockMapper.selectProductStockByParam(params);
            if (stockInfo == null) {
                stockInfo = new ProductStock();
                stockInfo.setCompanyId(companyId);
                stockInfo.setTenantId(tenantId);
                if (companyId == tenantId) {
                    stockInfo.setStockLevel(AccConstants.STOCK_LEVEL_COMPANY);
                } else {
                    stockInfo.setStockLevel(AccConstants.STOCK_LEVEL_TENANT);
                }
                stockInfo.setStorageId(storageId);
                stockInfo.setProductId(productId);
                //该仓库新建该产品库存信息
                if (storageType == AccConstants.STORAGE_TYPE_IN) {
                    flowBefore = 0;
                    flowAfter = flowNum;
                    stockInfo.setInNum(flowNum);
                    stockInfo.setRemainNum(flowNum);
                } else {
                    throw new CustomException("库存第一次变更对应流转类型异常：不是入库类型");
                }
                stockInfo.setUpdateTime(new Date());
                res = productStockMapper.insertProductStock(stockInfo);
            } else {
                flowBefore = stockInfo.getRemainNum();
                //该仓库更新该产品库存信息
                if (storageType == AccConstants.STORAGE_TYPE_IN) {
                    stockInfo.setInNum(stockInfo.getInNum() + flowNum);
                    stockInfo.setRemainNum(stockInfo.getRemainNum() + flowNum);
                } else if (storageType == AccConstants.STORAGE_TYPE_OUT) {
                    stockInfo.setOutNum(stockInfo.getOutNum() + flowNum);
                    stockInfo.setRemainNum(stockInfo.getRemainNum() - flowNum);
                } else if (storageType == AccConstants.STORAGE_TYPE_BACK) {
                    stockInfo.setBackNum(stockInfo.getBackNum() + flowNum);
                    stockInfo.setRemainNum(stockInfo.getRemainNum() - flowNum);
                }
                flowAfter = stockInfo.getRemainNum();
                stockInfo.setUpdateTime(new Date());
                res = productStockMapper.updateProductStock(stockInfo);
            }

            //新增库存明细
            if (res > 0) {
                ProductStockFlow stockFlow = new ProductStockFlow();
                long stockId = stockInfo.getId();
                stockFlow.setCompanyId(companyId);
                stockFlow.setStockId(stockId);
                if (storageType == AccConstants.STORAGE_TYPE_IN) {
                    stockFlow.setFlowType(AccConstants.STOCK_TYPE_ADD);
                } else if (storageType == AccConstants.STORAGE_TYPE_OUT || storageType == AccConstants.STORAGE_TYPE_BACK) {
                    stockFlow.setFlowType(AccConstants.STOCK_TYPE_REDUCE);
                }
                stockFlow.setStorageType(storageType);
                stockFlow.setStorageRecordId(storageRecordId);
                stockFlow.setFlowNum(flowNum);
                stockFlow.setFlowBefore(flowBefore);
                stockFlow.setFlowAfter(flowAfter);
                stockFlow.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
                stockFlow.setCreateTime(new Date());
                productStockFlowService.insertProductStockFlow(stockFlow);
            }
        } else {
            throw new CustomException("企业经销商id异常");
        }
        return res;
    }

    /**
     * 修改库存统计
     *
     * @param productStock 库存统计
     * @return 结果
     */
    @Override
    public int updateProductStock(ProductStock productStock) {
        productStock.setUpdateTime(DateUtils.getNowDate());
        return productStockMapper.updateProductStock(productStock);
    }

    /**
     * 批量删除库存统计
     *
     * @param ids 需要删除的库存统计 ID
     * @return 结果
     */
    @Override
    public int deleteProductStockByIds(Long[] ids) {
        return productStockMapper.deleteProductStockByIds(ids);
    }

    /**
     * 删除库存统计 信息
     *
     * @param id 库存统计 ID
     * @return 结果
     */
    @Override
    public int deleteProductStockById(Long id) {
        return productStockMapper.deleteProductStockById(id);
    }
}
