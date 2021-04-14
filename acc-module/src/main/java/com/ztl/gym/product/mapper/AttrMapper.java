package com.ztl.gym.product.mapper;

import java.util.List;
import java.util.Map;

import com.ztl.gym.product.domain.Attr;

/**
 * 规格属性Mapper接口
 * 
 * @author ruoyi
 * @date 2021-04-13
 */
public interface AttrMapper 
{
    /**
     * 查询规格属性
     * 
     * @param id 规格属性ID
     * @return 规格属性
     */
    public Attr selectAttrById(Long id);

    /**
     * 查询规格属性列表
     * 
     * @param attr 规格属性
     * @return 规格属性集合
     */
    public List<Attr> selectAttrList(Attr attr);

    /**
     * 新增规格属性
     * 
     * @param attr 规格属性
     * @return 结果
     */
    public int insertAttr(Attr attr);

    /**
     * 修改规格属性
     * 
     * @param attr 规格属性
     * @return 结果
     */
    public int updateAttr(Attr attr);

    /**
     * 删除规格属性
     * 
     * @param id 规格属性ID
     * @return 结果
     */
    public int deleteAttrById(Long id);

    /**
     * 批量删除规格属性
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAttrByIds(Long[] ids);

}
