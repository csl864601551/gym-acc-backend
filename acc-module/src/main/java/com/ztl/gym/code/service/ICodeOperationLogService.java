package com.ztl.gym.code.service;


import com.ztl.gym.code.domain.CodeOperationLog;

import java.util.List;

/**
 * 操作日志Service接口
 * 
 * @author ruoyi
 * @date 2021-08-05
 */
public interface ICodeOperationLogService 
{
    /**
     * 查询操作日志
     * 
     * @param id 操作日志ID
     * @return 操作日志
     */
    public CodeOperationLog selectCodeOperationLogById(Long id);

    /**
     * 查询操作日志列表
     * 
     * @param codeOperationLog 操作日志
     * @return 操作日志集合
     */
    public List<CodeOperationLog> selectCodeOperationLogList(CodeOperationLog codeOperationLog);

    /**
     * 新增操作日志
     * 
     * @param codeOperationLog 操作日志
     * @return 结果
     */
    public int insertCodeOperationLog(CodeOperationLog codeOperationLog);

    /**
     * 修改操作日志
     * 
     * @param codeOperationLog 操作日志
     * @return 结果
     */
    public int updateCodeOperationLog(CodeOperationLog codeOperationLog);

    /**
     * 批量删除操作日志
     * 
     * @param ids 需要删除的操作日志ID
     * @return 结果
     */
    public int deleteCodeOperationLogByIds(Long[] ids);

    /**
     * 删除操作日志信息
     * 
     * @param id 操作日志ID
     * @return 结果
     */
    public int deleteCodeOperationLogById(Long id);
}
