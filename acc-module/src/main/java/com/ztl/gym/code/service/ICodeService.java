package com.ztl.gym.code.service;


import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.vo.CodeVo;
import com.ztl.gym.storage.domain.vo.FlowVo;

import java.util.List;
import java.util.Map;

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
     * @param boxCount     箱数
     * @param codeAttrId   生码属性id
     * @return
     */
    int createCode(Long companyId, Long codeRecordId, Long codeTotalNum, long boxCount, Long codeAttrId);

    /**
     * 根据属性id修改码状态
     *
     * @param companyId
     * @param codeAttrId
     * @param status
     * @return
     */
    int updateStatusByAttrId(Long companyId, Long codeAttrId, int status);

    /**
     * 批量新增码流转明细 【insertProvider形式】
     *
     * @param companyId   企业id
     * @param storageType 流转类型
     * @param list        流转码集合
     * @return
     */
    int insertCodeFlowForBatchSingle(long companyId, int storageType, List<FlowVo> list);

    /**
     * 根据物流流转信息查询码集合 【分批查询】
     *
     * @param companyId
     * @param storageType
     * @param storageRecordId
     * @return
     */
    List<String> selectCodeByStorage(long companyId, int storageType, long storageRecordId);

    List<Code> selectCodeListByCodeOrIndex(Map<String, Object> map);

    long getCodeCount(String code);
}
