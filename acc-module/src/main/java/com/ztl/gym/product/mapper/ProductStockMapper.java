package com.ztl.gym.product.mapper;

import java.util.List;
import java.util.Map;

import com.ztl.gym.product.domain.ProductStock;
import org.springframework.stereotype.Repository;

/**
 * 库存统计 Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-25
 */
@Repository
public interface ProductStockMapper {
    /**
     * 查询库存统计
     *
     * @param id 库存统计 ID
     * @return 库存统计
     */
    public ProductStock selectProductStockById(Long id);

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
     * @param productStock 库存统计
     * @return 结果
     */
    public int insertProductStock(ProductStock productStock);

    /**
     * 修改库存统计
     *
     * @param productStock 库存统计
     * @return 结果
     */
    public int updateProductStock(ProductStock productStock);

    /**
     * 删除库存统计
     *
     * @param id 库存统计 ID
     * @return 结果
     */
    public int deleteProductStockById(Long id);

    /**
     * 批量删除库存统计
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductStockByIds(Long[] ids);

    /**
     * 根据条件查询产品库存信息
     *
     * @param params
     * @return
     */
    ProductStock selectProductStockByParam(Map<String, Object> params);

    int updateProductIdByIds(Map<String, Object> params);
}
