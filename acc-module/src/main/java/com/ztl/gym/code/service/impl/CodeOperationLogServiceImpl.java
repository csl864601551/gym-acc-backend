package com.ztl.gym.code.service.impl;

import java.util.List;

import com.ztl.gym.code.domain.CodeOperationLog;
import com.ztl.gym.code.mapper.CodeOperationLogMapper;
import com.ztl.gym.code.service.ICodeOperationLogService;
import com.ztl.gym.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 操作日志Service业务层处理
 *
 * @author ruoyi
 * @date 2021-08-05
 */
@Service
public class CodeOperationLogServiceImpl implements ICodeOperationLogService
{
    @Autowired
    private CodeOperationLogMapper codeOperationLogMapper;

    /**
     * 查询操作日志
     *
     * @param id 操作日志ID
     * @return 操作日志
     */
    @Override
    public CodeOperationLog selectCodeOperationLogById(Long id)
    {
        return codeOperationLogMapper.selectCodeOperationLogById(id);
    }

    /**
     * 查询操作日志列表
     *
     * @param codeOperationLog 操作日志
     * @return 操作日志
     */
    @Override
    public List<CodeOperationLog> selectCodeOperationLogList(CodeOperationLog codeOperationLog)
    {
        return codeOperationLogMapper.selectCodeOperationLogList(codeOperationLog);
    }

    /**
     * 新增操作日志
     *
     * @param codeOperationLog 操作日志
     * @return 结果
     */
    @Override
    public int insertCodeOperationLog(CodeOperationLog codeOperationLog)
    {
        codeOperationLog.setCreateTime(DateUtils.getNowDate());
        return codeOperationLogMapper.insertCodeOperationLog(codeOperationLog);
    }

    /**
     * 修改操作日志
     *
     * @param codeOperationLog 操作日志
     * @return 结果
     */
    @Override
    public int updateCodeOperationLog(CodeOperationLog codeOperationLog)
    {
        codeOperationLog.setUpdateTime(DateUtils.getNowDate());
        return codeOperationLogMapper.updateCodeOperationLog(codeOperationLog);
    }

    /**
     * 批量删除操作日志
     *
     * @param ids 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteCodeOperationLogByIds(Long[] ids)
    {
        return codeOperationLogMapper.deleteCodeOperationLogByIds(ids);
    }

    /**
     * 删除操作日志信息
     *
     * @param id 操作日志ID
     * @return 结果
     */
    @Override
    public int deleteCodeOperationLogById(Long id)
    {
        return codeOperationLogMapper.deleteCodeOperationLogById(id);
    }
}
