package com.ztl.gym.code.mapper;


import com.ztl.gym.code.domain.CodeRecord;
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
public interface CodeRecordMapper {
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
     * 删除生码记录
     *
     * @param id 生码记录ID
     * @return 结果
     */
    public int deleteCodeRecordById(Long id);

    /**
     * 批量删除生码记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCodeRecordByIds(Long[] ids);

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
    int insertCodeRecordStatus(Map<String, Object> params);

    /**
     * 查询生码记录
     *
     * @param codeIndex 生码记录ID
     * @return 生码记录
     */
    public CodeRecord selectCodeRecordByIndex(Long codeIndex);
}
