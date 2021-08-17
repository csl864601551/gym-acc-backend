package com.ztl.gym.code.service;


import com.ztl.gym.code.domain.CodeSingle;

import java.util.List;
import java.util.Map;

/**
 * 生码记录Service接口
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public interface ICodeSingleService {
    /**
     * 查询生码记录
     *
     * @param id 生码记录ID
     * @return 生码记录
     */
    public CodeSingle selectCodeSingleById(Long id);

    /**
     * 查询生码记录列表
     *
     * @param codeSingle 生码记录
     * @return 生码记录集合
     */
    public List<CodeSingle> selectCodeSingleList(CodeSingle codeSingle);

    /**
     * 新增生码记录
     *
     * @param codeSingle 生码记录
     * @return 结果
     */
    public int insertCodeSingle(CodeSingle codeSingle);

    /**
     * 修改生码记录
     *
     * @param codeSingle 生码记录
     * @return 结果
     */
    public int updateCodeSingle(CodeSingle codeSingle);

    /**
     * 更新生码记录状态
     *
     * @param singleId 生码记录
     * @return 结果
     */
    public int updateCodeSingleStatusBySingleId(long singleId,Long indexEnd, boolean flag);

    /**
     * 批量删除生码记录
     *
     * @param ids 需要删除的生码记录ID
     * @return 结果
     */
    public int deleteCodeSingleByIds(Long[] ids);

    /**
     * 删除生码记录信息
     *
     * @param id 生码记录ID
     * @return 结果
     */
    public int deleteCodeSingleById(Long id);


    /**
     * 生码-普通单码
     *
     * @param companyId 企业id
     * @param num       生码数量
     * @param remark    备注详情
     * @return
     */
    int createCodeSingle(long companyId, int isAcc, long num, String remark);


    /**
     * 查询生码记录
     *
     * @param codeIndex 生码记录ID
     * @return 生码记录
     */
    public CodeSingle selectCodeSingleByIndex(long codeIndex,long companyId);

    int createAccCodeSingle(long companyId,int isAcc, long count, String remark);


    /**
     * 单码生码总量
     *
     * @param map 部门信息
     * @return 结果
     */
    public int selectSingCodeNum(Map<String, Object> map);



    /**
     * 查询单码生码记录
     *
     * @param codeIndex 生码记录ID
     * @return 生码记录
     */
    public CodeSingle selectSingCodeRecordByIndex(long codeIndex, long companyId);
}
