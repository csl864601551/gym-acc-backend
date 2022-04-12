package com.ztl.gym.code.mapper;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.storage.domain.vo.FlowVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 码 Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-14
 */
@Repository
public interface CodeMapper {
    /**
     * 查询单个码
     *
     * @param code
     * @return
     */
    Code selectCode(Code code);

    /**
     * 查询码
     *
     * @param codeIndex 码 ID
     * @return 码
     */
    public Code selectCodeById(Long codeIndex);

    /**
     * 查询码
     *
     * @param code 码
     * @return 码
     */
    public Code selectCodeByCode(String code);

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
     * 批量插入码
     *
     * @param list
     * @return
     */
    int insertCodeForBatch(List<Code> list);

    /**
     * 修改码
     *
     * @param code 码
     * @return 结果
     */
    public int updateCode(Code code);

    /**
     * 删除码
     *
     * @param codeIndex 码 ID
     * @return 结果
     */
    public int deleteCodeById(Long codeIndex);

    /**
     * 批量删除码
     *
     * @param codeIndexs 需要删除的数据ID
     * @return 结果
     */
    public int deleteCodeByIds(Long[] codeIndexs);

    /**
     * 根据属性id修改码状态
     *
     * @param params
     * @return
     */
    int updateStatusByAttrId(Map<String, Object> params);

    /**
     * 批量新增单码流转明细-入库 【xml形式】
     *
     * @param list
     * @return
     */
    int insertInCodeFlowForBatchSingleV2(@Param("companyId") long companyId, @Param("list") List<FlowVo> list);

    /**
     * 批量新增单码流转明细-出库 【xml形式】
     *
     * @param list
     * @return
     */
    int insertOutCodeFlowForBatchSingleV2(@Param("companyId") long companyId, @Param("list") List<FlowVo> list);

    /**
     * 批量新增单码流转明细-调拨 【xml形式】
     *
     * @param list
     * @return
     */
    int insertTransferCodeFlowForBatchSingleV2(@Param("companyId") long companyId, @Param("list") List<FlowVo> list);

    /**
     * 批量新增单码流转明细-退货 【xml形式】
     *
     * @param list
     * @return
     */
    int insertBackCodeFlowForBatchSingleV2(@Param("companyId") long companyId, @Param("list") List<FlowVo> list);

    /**
     * 根据入库流转信息查询单码
     *
     * @param params
     * @return
     */
    List<String> selectInCodeByStorage(Map<String, Object> params);

    /**
     * 根据出库流转信息查询单码
     *
     * @param params
     * @return
     */
    List<String> selectOutCodeByStorage(Map<String, Object> params);

    /**
     * 根据调拨流转信息查询单码
     *
     * @param params
     * @return
     */
    List<String> selectTransferCodeByStorage(Map<String, Object> params);

    /**
     * 根据退货流转信息查询单码
     *
     * @param params
     * @return
     */
    List<String> selectBackCodeByStorage(Map<String, Object> params);

    List<Code> selectCodeListByCodeOrIndex(Map<String, Object> map);

    /**
     * 根据生码记录查询码
     *
     * @param companyId
     * @param recordId
     * @return
     */
    List<Code> selectCodeListByRecord(@Param("companyId") long companyId, @Param("recordId") long recordId);

    List<Code> selectCodeListBySingle(@Param("companyId") long companyId, @Param("singleId") long singleId);

    void updatePCodeByCode(Map<String, Object> params);

    List<Code> selectCodes(Map<String, Object> codeParam);

    void updateCodeStorageByPCode(Code codeTemp);

    int updateCodeStorageByCode(Code codeRes);

    void updateCodeAttrIdByPCode(Map<String, Object> params);


    int insertAccCodeForBatch(List<Map> codeList);

    /**
     * 查询防伪码绑定记录
     *
     * @param codeParam 入参
     * @return 响应
     */
    List<Code> selectCodeRecordBySecurityCode(Map<String, Object> codeParam);

    void unBindCodeByAttrId(@Param("companyId") Long companyId, @Param("codeAttrId") Long codeAttrId);

    void deletePCodeByAttrId(@Param("companyId") Long companyId, @Param("codeAttrId") Long attrId);

    int insertCodeAll(@Param("listCode") List<Code> listCode, @Param("companyId") Long companyId);
}
