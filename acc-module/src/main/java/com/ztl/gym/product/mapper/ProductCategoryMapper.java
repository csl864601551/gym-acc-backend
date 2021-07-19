package com.ztl.gym.product.mapper;

import com.ztl.gym.product.domain.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 产品分类Mapper接口
 * 
 * @author ruoyi
 * @date 2021-04-12
 */
public interface ProductCategoryMapper
{
    /**
     * 查询产品分类
     * 
     * @param id 产品分类ID
     * @return 产品分类
     */
    public ProductCategory selectProductCategoryById(Long id);

    /**
     * 查询产品分类列表
     * 
     * @param productCategory 产品分类
     * @return 产品分类集合
     */
    public List<ProductCategory> selectProductCategoryList(ProductCategory productCategory);


    /**
     * 查询产品分类列表
     *
     * @param productCategory 产品分类
     * @return 产品分类集合
     */
    public List<ProductCategory> selectProductCategoryOneList(ProductCategory productCategory);

    /**
     * 新增产品分类
     * 
     * @param productCategory 产品分类
     * @return 结果
     */
    public int insertProductCategory(ProductCategory productCategory);

    /**
     * 修改产品分类
     * 
     * @param productCategory 产品分类
     * @return 结果
     */
    public int updateProductCategory(ProductCategory productCategory);

    /**
     * 删除产品分类
     * 
     * @param id 产品分类ID
     * @return 结果
     */
    public int deleteProductCategoryById(Long id);

    /**
     * 批量删除产品分类
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductCategoryByIds(Long[] ids);

    List<Map<String, Object>> getCategoryDic(@Param("p_id")Long id, @Param("companyId")Long companyId);
}
