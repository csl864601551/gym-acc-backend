package com.ztl.gym.code.mapper;

import java.util.List;
import java.util.Map;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.vo.CodeVo;
import com.ztl.gym.code.service.impl.CodeServiceImpl;
import com.ztl.gym.storage.domain.vo.FlowVo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

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
     * 新增单码流转明细
     *
     * @param flowVo
     * @return
     */
    int insertCodeFlowForSingle(FlowVo flowVo);

    /**
     * 新增箱码流转明细
     *
     * @param flowVo
     * @return
     */
    int insertCodeFlowForBox(FlowVo flowVo);

    /**
     * 批量新增单码流转明细 【insertProvider形式】
     *
     * @param list
     * @return
     */
    @InsertProvider(type = CodeServiceImpl.class, method = "buildInsertBatchCodeFlowSql")
    int insertCodeFlowForBatchSingle(long companyId, List<FlowVo> list);

    /**
     * 批量新增单码流转明细 【xml形式】
     *
     * @param list
     * @return
     */
    int insertCodeFlowForBatchSingleV2(@Param("companyId") long companyId, @Param("list") List<FlowVo> list);

    /**
     * 根据物流流转信息查询箱码
     *
     * @param params
     * @return
     */
    String selectPcodeByStorage(Map<String, Object> params);

    /**
     * 根据物流流转信息查询单码
     *
     * @param params
     * @return
     */
    List<String> selectCodeByStorage(Map<String, Object> params);

    /**
     * 根据物流流转信息查询单码
     *
     * @param params
     * @return
     */
    List<CodeVo> selectCodeByStorageForComplex(Map<String, Object> params );
}
