package com.ztl.gym.code.mapper;


import com.ztl.gym.code.domain.CodeSingle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 生码记录Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-13
 */
@Repository
public interface CodeSingleMapper {
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
     * @param CodeSingle 生码记录
     * @return 结果
     */
    public int updateCodeSingle(CodeSingle CodeSingle);

    /**
     * 删除生码记录
     *
     * @param id 生码记录ID
     * @return 结果
     */
    public int deleteCodeSingleById(Long id);

    /**
     * 批量删除生码记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCodeSingleByIds(Long[] ids);

    /**
     * 更新生码记录起始、终止流水号
     *
     * @param params
     * @return
     */
    int updateCodeIndex(Map<String, Object> params);

    /**
     * 更新生码记录状态
     *
     * @param params
     * @return
     */
    int insertCodeSingleStatus(Map<String, Object> params);

    /**
     * 查询生码记录
     *
     * @param codeIndex 生码记录ID
     * @return 生码记录
     */
    public CodeSingle selectCodeSingleByIndex(@Param("codeIndex")  long codeIndex, @Param("companyId") long companyId);

    /**
     * 单码生码总量
     *
     * @param map 部门信息
     * @return 结果
     */
    int selectSingCodeNum(Map<String, Object> map);
}
