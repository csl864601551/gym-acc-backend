package com.ztl.gym.product.service;

import java.util.List;

import com.ztl.gym.product.domain.ProductStock;

/**
 * 库存统计 Service接口
 *
 * @author ruoyi
 * @date 2021-04-25
 */
public interface IProductStockService {
    /**
     * 查询库存统计
     *
     * @param id 库存统计 ID
     * @return 库存统计
     */
    public ProductStock selectProductStockById(Long id);

    /**
     * 根据仓库与产品查询库存信息
     *
     * @param storageId
     * @param productId
     * @return
     */
    public ProductStock selectProductStockByParam(long storageId, long productId);

    /**
     * 查询库存统计 列表
     *
     * @param productStock 库存统计
     * @return 库存统计 集合
     */
    public List<ProductStock> selectProductStockList(ProductStock productStock);

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
    public int insertProductStock(long tenantId,long storageId, long productId, int storageType, long storageRecordId, int flowNum);

    /**
     * 修改库存统计
     *
     * @param productStock 库存统计
     * @return 结果
     */
    public int updateProductStock(ProductStock productStock);

    /**
     * 批量删除库存统计
     *
     * @param ids 需要删除的库存统计 ID
     * @return 结果
     */
    public int deleteProductStockByIds(Long[] ids);

    /**
     * 删除库存统计 信息
     *
     * @param id 库存统计 ID
     * @return 结果
     */
    public int deleteProductStockById(Long id);

    int updateProductIdByRecordIds(long productId, List<Long> storageRecordIds);
}
