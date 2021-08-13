package com.ztl.gym.code.service.impl;

import java.util.List;
import com.ztl.gym.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.code.mapper.CodeAttrMapper;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.code.service.ICodeAttrService;

/**
 * 码属性Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-15
 */
@Service
public class CodeAttrServiceImpl implements ICodeAttrService
{
    @Autowired
    private CodeAttrMapper codeAttrMapper;

    /**
     * 查询码属性
     *
     * @param id 码属性ID
     * @return 码属性
     */
    @Override
    public CodeAttr selectCodeAttrById(Long id)
    {
        return codeAttrMapper.selectCodeAttrById(id);
    }

    /**
     * 根据生码记录id查询码
     *
     * @param recordId
     * @return
     */
    @Override
    public List<CodeAttr> selectCodeAttrByRecordId(Long recordId) {
        return codeAttrMapper.selectCodeAttrByRecordId(recordId);
    }
    /**
     * 根据生码记录id查询码
     *
     * @param singleId
     * @return
     */
    @Override
    public List<CodeAttr> selectCodeAttrBySingleId(Long singleId) {
        return codeAttrMapper.selectCodeAttrBySingleId(singleId);
    }
    /**
     * 查询码属性列表
     *
     * @param codeAttr 码属性
     * @return 码属性
     */
    @Override
    public List<CodeAttr> selectCodeAttrList(CodeAttr codeAttr)
    {
        return codeAttrMapper.selectCodeAttrList(codeAttr);
    }

    /**
     * 新增码属性
     *
     * @param codeAttr 码属性
     * @return 结果
     */
    @Override
    public Long insertCodeAttr(CodeAttr codeAttr)
    {
        codeAttr.setCreateTime(DateUtils.getNowDate());
        codeAttrMapper.insertCodeAttr(codeAttr);
        return codeAttr.getId();
    }

    /**
     * 修改码属性
     *
     * @param codeAttr 码属性
     * @return 结果
     */
    @Override
    public int updateCodeAttr(CodeAttr codeAttr)
    {
        codeAttr.setUpdateTime(DateUtils.getNowDate());
        return codeAttrMapper.updateCodeAttr(codeAttr);
    }

    /**
     * 批量删除码属性
     *
     * @param ids 需要删除的码属性ID
     * @return 结果
     */
    @Override
    public int deleteCodeAttrByIds(Long[] ids)
    {
        return codeAttrMapper.deleteCodeAttrByIds(ids);
    }

    /**
     * 删除码属性信息
     *
     * @param id 码属性ID
     * @return 结果
     */
    @Override
    public int deleteCodeAttrById(Long id)
    {
        return codeAttrMapper.deleteCodeAttrById(id);
    }
    /**
     * 批量修改码属性
     *
     * @param attrList 码属性
     * @return 结果
     */
    @Override
    public int updateCodeAttrBatch(List<CodeAttr> attrList) {
        return codeAttrMapper.updateCodeAttrBatch(attrList);
    }

    @Override
    public void insertCodeAttrBatch(List<CodeAttr> codeAttrs) {
        codeAttrMapper.insertCodeAttrBatch(codeAttrs);
    }
}
