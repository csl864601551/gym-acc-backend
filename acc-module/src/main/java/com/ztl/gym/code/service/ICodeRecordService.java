package com.ztl.gym.code.service;


import com.ztl.gym.code.domain.CodeRecord;

import java.util.List;

/**
 * 生码记录Service接口
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public interface ICodeRecordService {
    /**
     * 查询生码记录
     *
     * @param id 生码记录ID
     * @return 生码记录
     */
    public CodeRecord selectCodeRecordById(Long id);

    /**
     * 查询生码记录列表
     *
     * @param codeRecord 生码记录
     * @return 生码记录集合
     */
    public List<CodeRecord> selectCodeRecordList(CodeRecord codeRecord);

    /**
     * 新增生码记录
     *
     * @param codeRecord 生码记录
     * @return 结果
     */
    public int insertCodeRecord(CodeRecord codeRecord);

    /**
     * 修改生码记录
     *
     * @param codeRecord 生码记录
     * @return 结果
     */
    public int updateCodeRecord(CodeRecord codeRecord);

    /**
     * 批量删除生码记录
     *
     * @param ids 需要删除的生码记录ID
     * @return 结果
     */
    public int deleteCodeRecordByIds(Long[] ids);

    /**
     * 删除生码记录信息
     *
     * @param id 生码记录ID
     * @return 结果
     */
    public int deleteCodeRecordById(Long id);


    /**
     * 生码-普通单码
     *
     * @param companyId 企业id
     * @param num       生码数量
     * @param remark    备注详情
     * @return
     */
    int createCodeRecord(long companyId, long num, String remark);

    /**
     * 生码-套标
     *
     * @param companyId 企业id
     * @param num       每箱码数
     * @param remark    备注详情
     * @return
     */
    int createPCodeRecord(long companyId, long num, String remark);
}
