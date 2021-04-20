package com.ztl.gym.code.mapper;

import java.util.List;
import java.util.Map;

import com.ztl.gym.code.domain.Code;
import org.springframework.stereotype.Repository;

/**
 * 码 Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-14
 */
@Repository
public interface CodeMapper
{
    /**
     * 查询单个码
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

}
