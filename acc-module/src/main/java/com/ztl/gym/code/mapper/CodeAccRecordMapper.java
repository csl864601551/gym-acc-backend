package com.ztl.gym.code.mapper;

import java.util.List;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAcc;
import com.ztl.gym.code.domain.CodeAccRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 生码记录Mapper接口
 * 
 * @author ruoyi
 * @date 2021-07-22
 */
public interface CodeAccRecordMapper 
{
    /**
     * 查询生码记录
     * 
     * @param id 生码记录ID
     * @return 生码记录
     */
    public CodeAccRecord selectCodeAccRecordById(Long id);

    /**
     * 查询生码记录列表
     * 
     * @param codeAccRecord 生码记录
     * @return 生码记录集合
     */
    public List<CodeAccRecord> selectCodeAccRecordList(CodeAccRecord codeAccRecord);

    /**
     * 新增生码记录
     * 
     * @param codeAccRecord 生码记录
     * @return 结果
     */
    public int insertCodeAccRecord(CodeAccRecord codeAccRecord);

    /**
     * 修改生码记录
     * 
     * @param codeAccRecord 生码记录
     * @return 结果
     */
    public int updateCodeAccRecord(CodeAccRecord codeAccRecord);

    /**
     * 删除生码记录
     * 
     * @param id 生码记录ID
     * @return 结果
     */
    public int deleteCodeAccRecordById(Long id);

    /**
     * 批量删除生码记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCodeAccRecordByIds(Long[] ids);

    List<CodeAcc> selectAccCodeListByRecord(@Param("companyId") Long companyId, @Param("recordId")  Long recordId);

    /**
     * 根据防伪码查找防伪码生成记录
     * @param codeAcc 防伪码
     * @return 防伪记录
     */
    List<CodeAccRecord> selectRecordByAccCode(@Param("codeAcc")String codeAcc);
}
