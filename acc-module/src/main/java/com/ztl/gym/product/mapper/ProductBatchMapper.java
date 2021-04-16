package com.ztl.gym.product.mapper;

import java.util.List;
import java.util.Map;

import com.ztl.gym.product.domain.ProductBatch;

/**
 * 产品批次Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-14
 */
public interface ProductBatchMapper {
    /**
     * 查询产品批次
     *
     * @param id 产品批次ID
     * @return 产品批次
     */
    public ProductBatch selectProductBatchById(Long id);

    /**
     * 查询产品批次列表
     *
     * @param productBatch 产品批次
     * @return 产品批次集合
     */
    public List<Map<String, Object>> selectProductBatchList(Map<String, Object> param);

    public List<ProductBatch> selectProductBatchListV2(ProductBatch productBatch);

    /**
     * 新增产品批次
     *
     * @param productBatch 产品批次
     * @return 结果
     */
    public int insertProductBatch(ProductBatch productBatch);

    /**
     * 修改产品批次
     *
     * @param productBatch 产品批次
     * @return 结果
     */
    public int updateProductBatch(ProductBatch productBatch);

    /**
     * 删除产品批次
     *
     * @param id 产品批次ID
     * @return 结果
     */
    public int deleteProductBatchById(Long id);

    /**
     * 批量删除产品批次
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductBatchByIds(Long[] ids);
}
