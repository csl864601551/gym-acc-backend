package com.ztl.gym.code.service;


import com.ztl.gym.code.domain.Code;

import java.util.List;

/**
 * 码 Service接口
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public interface ICodeService {
    /**
     * 查询单个码
     *
     * @return
     */
    public Code selectCode(Code code);


    /**
     * 查询码
     *
     * @param codeIndex 码 ID
     * @return 码
     */
    public Code selectCodeById(Long codeIndex);

    /**
     * 查询码 列表
     *
     * @param code 码
     * @return 码 集合
     */
    public List<Code> selectCodeList(Code code);

    /**
     * 新增码
     *
     * @param code 码
     * @return 结果
     */
    public int insertCode(Code code);

    /**
     * 修改码
     *
     * @param code 码
     * @return 结果
     */
    public int updateCode(Code code);

    /**
     * 批量删除码
     *
     * @param codeIndexs 需要删除的码 ID
     * @return 结果
     */
    public int deleteCodeByIds(Long[] codeIndexs);

    /**
     * 删除码 信息
     *
     * @param codeIndex 码 ID
     * @return 结果
     */
    public int deleteCodeById(Long codeIndex);

    /**
     * 生码-普通单码
     *
     * @param companyId    企业id
     * @param codeRecordId 生码记录id
     * @param codeTotalNum 生码总数
     * @param pCode        箱码
     * @param codeAttrId   生码属性id
     * @return
     */
    int createCode(Long companyId, Long codeRecordId, Long codeTotalNum, String pCode, Long codeAttrId);

    /**
     * 根据属性id修改码状态
     *
     * @param companyId
     * @param codeAttrId
     * @param status
     * @param remark
     * @return
     */
    int updateStatusByAttrId(Long companyId, Long codeAttrId, int status);
}
