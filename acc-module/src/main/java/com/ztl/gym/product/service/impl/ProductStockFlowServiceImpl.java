package com.ztl.gym.product.service.impl;

import java.util.List;
import com.ztl.gym.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.product.mapper.ProductStockFlowMapper;
import com.ztl.gym.product.domain.ProductStockFlow;
import com.ztl.gym.product.service.IProductStockFlowService;

/**
 *  产品库存明细Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-25
 */
@Service
public class ProductStockFlowServiceImpl implements IProductStockFlowService
{
    @Autowired
    private ProductStockFlowMapper productStockFlowMapper;

    /**
     * 查询 产品库存明细
     *
     * @param id  产品库存明细ID
     * @return  产品库存明细
     */
    @Override
    public ProductStockFlow selectProductStockFlowById(Long id)
    {
        return productStockFlowMapper.selectProductStockFlowById(id);
    }

    /**
     * 查询 产品库存明细列表
     *
     * @param productStockFlow  产品库存明细
     * @return  产品库存明细
     */
    @Override
    public List<ProductStockFlow> selectProductStockFlowList(ProductStockFlow productStockFlow)
    {
        return productStockFlowMapper.selectProductStockFlowList(productStockFlow);
    }

    /**
     * 新增 产品库存明细
     *
     * @param productStockFlow  产品库存明细
     * @return 结果
     */
    @Override
    public int insertProductStockFlow(ProductStockFlow productStockFlow)
    {
        productStockFlow.setCreateTime(DateUtils.getNowDate());
        return productStockFlowMapper.insertProductStockFlow(productStockFlow);
    }

    /**
     * 修改 产品库存明细
     *
     * @param productStockFlow  产品库存明细
     * @return 结果
     */
    @Override
    public int updateProductStockFlow(ProductStockFlow productStockFlow)
    {
        return productStockFlowMapper.updateProductStockFlow(productStockFlow);
    }

    /**
     * 批量删除 产品库存明细
     *
     * @param ids 需要删除的 产品库存明细ID
     * @return 结果
     */
    @Override
    public int deleteProductStockFlowByIds(Long[] ids)
    {
        return productStockFlowMapper.deleteProductStockFlowByIds(ids);
    }

    /**
     * 删除 产品库存明细信息
     *
     * @param id  产品库存明细ID
     * @return 结果
     */
    @Override
    public int deleteProductStockFlowById(Long id)
    {
        return productStockFlowMapper.deleteProductStockFlowById(id);
    }


    @Override
    public int unBindProductStockFlowByInId(Long companyId,Long inId) {
        ProductStockFlow productStockFlow=new ProductStockFlow();
        productStockFlow.setCompanyId(companyId);
        productStockFlow.setStorageRecordId(inId);
        List<ProductStockFlow> productStockFlowList= selectProductStockFlowList(productStockFlow);
        Long stockId=productStockFlowList.get(0).getStockId();
        Long stockFlowId=productStockFlowList.get(0).getId();
        Integer flowNum=productStockFlowList.get(0).getFlowNum();
        productStockFlowMapper.updateStockNum(stockId,flowNum);//更新库存
        productStockFlowMapper.deleteProductStockFlowById(stockFlowId);//删除库存明细
        return 0;
    }
}
