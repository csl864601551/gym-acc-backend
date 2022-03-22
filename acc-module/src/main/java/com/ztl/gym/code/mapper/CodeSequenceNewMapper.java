package com.ztl.gym.code.mapper;

import java.util.List;

import com.ztl.gym.code.domain.CodeSequenceNew;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-22
 */
public interface CodeSequenceNewMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CodeSequenceNew selectCodeSequenceNewById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param codeSequenceNew 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CodeSequenceNew> selectCodeSequenceNewList(CodeSequenceNew codeSequenceNew);

    /**
     * 新增【请填写功能名称】
     * 
     * @param codeSequenceNew 【请填写功能名称】
     * @return 结果
     */
    public int insertCodeSequenceNew(CodeSequenceNew codeSequenceNew);

    /**
     * 修改【请填写功能名称】
     * 
     * @param codeSequenceNew 【请填写功能名称】
     * @return 结果
     */
    public int updateCodeSequenceNew(CodeSequenceNew codeSequenceNew);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCodeSequenceNewById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCodeSequenceNewByIds(Long[] ids);
}
