package com.ztl.gym.product.mapper;

import java.util.List;
import com.ztl.gym.product.domain.ProductStockFlow;
import org.springframework.stereotype.Repository;

/**
 *  产品库存明细Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-25
 */
@Repository
public interface ProductStockFlowMapper
{
    /**
     * 查询 产品库存明细
     *
     * @param id  产品库存明细ID
     * @return  产品库存明细
     */
    public ProductStockFlow selectProductStockFlowById(Long id);

    /**
     * 查询 产品库存明细列表
     *
     * @param productStockFlow  产品库存明细
     * @return  产品库存明细集合
     */
    public List<ProductStockFlow> selectProductStockFlowList(ProductStockFlow productStockFlow);

    /**
     * 新增 产品库存明细
     *
     * @param productStockFlow  产品库存明细
     * @return 结果
     */
    public int insertProductStockFlow(ProductStockFlow productStockFlow);

    /**
     * 修改 产品库存明细
     *
     * @param productStockFlow  产品库存明细
     * @return 结果
     */
    public int updateProductStockFlow(ProductStockFlow productStockFlow);

    /**
     * 删除 产品库存明细
     *
     * @param id  产品库存明细ID
     * @return 结果
     */
    public int deleteProductStockFlowById(Long id);

    /**
     * 批量删除 产品库存明细
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductStockFlowByIds(Long[] ids);
}
