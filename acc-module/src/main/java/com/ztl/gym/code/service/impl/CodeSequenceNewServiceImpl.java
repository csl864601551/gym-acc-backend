package com.ztl.gym.code.service.impl;

import java.util.List;

import com.ztl.gym.code.domain.CodeSequenceNew;
import com.ztl.gym.code.mapper.CodeSequenceNewMapper;
import com.ztl.gym.code.service.ICodeSequenceNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-03-22
 */
@Service
public class CodeSequenceNewServiceImpl implements ICodeSequenceNewService
{
    @Autowired
    private CodeSequenceNewMapper codeSequenceNewMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CodeSequenceNew selectCodeSequenceNewById(Long id)
    {
        return codeSequenceNewMapper.selectCodeSequenceNewById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param codeSequenceNew 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CodeSequenceNew> selectCodeSequenceNewList(CodeSequenceNew codeSequenceNew)
    {
        return codeSequenceNewMapper.selectCodeSequenceNewList(codeSequenceNew);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param codeSequenceNew 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCodeSequenceNew(CodeSequenceNew codeSequenceNew)
    {
        return codeSequenceNewMapper.insertCodeSequenceNew(codeSequenceNew);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param codeSequenceNew 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCodeSequenceNew(CodeSequenceNew codeSequenceNew)
    {
        return codeSequenceNewMapper.updateCodeSequenceNew(codeSequenceNew);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCodeSequenceNewByIds(Long[] ids)
    {
        return codeSequenceNewMapper.deleteCodeSequenceNewByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCodeSequenceNewById(Long id)
    {
        return codeSequenceNewMapper.deleteCodeSequenceNewById(id);
    }
}
