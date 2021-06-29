package com.ztl.gym.product.service;

import com.ztl.gym.product.domain.Attr;

import java.util.List;
import java.util.Map;

/**
 * 规格属性Service接口
 * 
 * @author ruoyi
 * @date 2021-04-13
 */
public interface IAttrService 
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
     * 批量删除规格属性
     * 
     * @param ids 需要删除的规格属性ID
     * @return 结果
     */
    public int deleteAttrByIds(Long[] ids);

    /**
     * 删除规格属性信息
     * 
     * @param id 规格属性ID
     * @return 结果
     */
    public int deleteAttrById(Long id);

    List<Map<String,Object>> getAttrValuesById(Long id);

    /**
     * 根据名称查询有没有重复的
     *
     * @return 规格属性
     */
    public Attr selectAttrBySome(Long companyId,String attrNameCn);
}
