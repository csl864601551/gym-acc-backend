package com.ztl.gym.code.mapper;

import java.util.List;
import java.util.Map;

import com.ztl.gym.code.domain.CodeAttr;
import org.springframework.stereotype.Repository;

/**
 * 码属性Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-15
 */
@Repository
public interface CodeAttrMapper {
    /**
     * 查询码属性
     *
     * @param id 码属性ID
     * @return 码属性
     */
    public CodeAttr selectCodeAttrById(Long id);

    /**
     * 根据生码记录id查询码
     *
     * @param recordId
     * @return
     */
    List<CodeAttr> selectCodeAttrByRecordId(Long recordId);

    /**
     * 查询码属性列表
     *
     * @param codeAttr 码属性
     * @return 码属性集合
     */
    public List<CodeAttr> selectCodeAttrList(CodeAttr codeAttr);

    /**
     * 新增码属性
     *
     * @param codeAttr 码属性
     * @return 结果
     */
    public Long insertCodeAttr(CodeAttr codeAttr);

    /**
     * 修改码属性
     *
     * @param codeAttr 码属性
     * @return 结果
     */
    public int updateCodeAttr(CodeAttr codeAttr);

    /**
     * 删除码属性
     *
     * @param id 码属性ID
     * @return 结果
     */
    public int deleteCodeAttrById(Long id);

    /**
     * 批量删除码属性
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCodeAttrByIds(Long[] ids);
    /**
     * 更新生码记录起始、终止流水号
     *
     * @param params
     * @return
     */
    int updateCodeIndex(Map<String, Object> params);

    int updateCodeAttrBatch(List<CodeAttr> attrList);
}
