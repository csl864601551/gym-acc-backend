package com.ztl.gym.code.service;

import java.util.List;

import com.ztl.gym.code.domain.CodeAttr;

/**
 * 码属性Service接口
 *
 * @author ruoyi
 * @date 2021-04-15
 */
public interface ICodeAttrService {
    /**
     * 查询码属性
     *
     * @param id 码属性ID
     * @return 码属性
     */
    public CodeAttr selectCodeAttrById(Long id);

    /**
     * 根据生码记录id查询码
     *
     * @param recordId
     * @return
     */
    public List<CodeAttr> selectCodeAttrByRecordId(Long recordId);

    /**
     * 查询码属性列表
     *
     * @param codeAttr 码属性
     * @return 码属性集合
     */
    public List<CodeAttr> selectCodeAttrList(CodeAttr codeAttr);

    /**
     * 新增码属性
     *
     * @param codeAttr 码属性
     * @return 结果
     */
    public Long insertCodeAttr(CodeAttr codeAttr);

    /**
     * 修改码属性
     *
     * @param codeAttr 码属性
     * @return 结果
     */
    public int updateCodeAttr(CodeAttr codeAttr);

    /**
     * 批量删除码属性
     *
     * @param ids 需要删除的码属性ID
     * @return 结果
     */
    public int deleteCodeAttrByIds(Long[] ids);

    /**
     * 删除码属性信息
     *
     * @param id 码属性ID
     * @return 结果
     */
    public int deleteCodeAttrById(Long id);
    /**
     * 批量修改码属性
     *
     * @param attrList 码属性集合
     * @return 结果
     */
    public int updateCodeAttrBatch(List<CodeAttr> attrList);

    /**
     * 批量新增CodeAttr表数据
     * @param listCodeAttr
     * @return
     */
    public int insertCodeAttrAll(List<CodeAttr> listCodeAttr);
}
