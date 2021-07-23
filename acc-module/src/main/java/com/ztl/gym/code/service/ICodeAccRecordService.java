package com.ztl.gym.code.service;

import java.util.List;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAcc;
import com.ztl.gym.code.domain.CodeAccRecord;

/**
 * 生码记录Service接口
 * 
 * @author ruoyi
 * @date 2021-07-22
 */
public interface ICodeAccRecordService 
{
    /**
     * 查询生码记录
     * 
     * @param id 生码记录ID
     * @return 生码记录
     */
    public CodeAccRecord selectCodeAccRecordById(Long id);

    /**
     * 查询生码记录列表
     * 
     * @param codeAccRecord 生码记录
     * @return 生码记录集合
     */
    public List<CodeAccRecord> selectCodeAccRecordList(CodeAccRecord codeAccRecord);

    /**
     * 新增生码记录
     * 
     * @param codeAccRecord 生码记录
     * @return 结果
     */
    public int insertCodeAccRecord(CodeAccRecord codeAccRecord);

    /**
     * 修改生码记录
     * 
     * @param codeAccRecord 生码记录
     * @return 结果
     */
    public int updateCodeAccRecord(CodeAccRecord codeAccRecord);

    /**
     * 批量删除生码记录
     * 
     * @param ids 需要删除的生码记录ID
     * @return 结果
     */
    public int deleteCodeAccRecordByIds(Long[] ids);

    /**
     * 删除生码记录信息
     * 
     * @param id 生码记录ID
     * @return 结果
     */
    public int deleteCodeAccRecordById(Long id);

    int createAccCodeSingle(long companyId, long count, String remark);

    List<CodeAcc> selectAccCodeListByRecord(Long loginUserTopCompanyId, Long id);
}
