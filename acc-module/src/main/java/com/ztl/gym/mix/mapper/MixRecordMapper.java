package com.ztl.gym.mix.mapper;

import com.ztl.gym.mix.domain.MixRecord;
import com.ztl.gym.mix.domain.vo.MixRecordVo;

import java.util.List;
import java.util.Map;

/**
 * 窜货记录Mapper接口
 * 
 * @author ruoyi
 * @date 2021-04-28
 */
public interface MixRecordMapper 
{
    /**
     * 查询窜货记录
     * 
     * @param id 窜货记录ID
     * @return 窜货记录
     */
    public MixRecord selectMixRecordById(Long id);

    /**
     * 查询窜货记录列表
     * 
     * @param mixRecord 窜货记录
     * @return 窜货记录集合
     */
    public List<MixRecord> selectMixRecordList(MixRecord mixRecord);

    /**
     * 新增窜货记录
     * 
     * @param mixRecord 窜货记录
     * @return 结果
     */
    public int insertMixRecord(MixRecord mixRecord);

    /**
     * 修改窜货记录
     * 
     * @param mixRecord 窜货记录
     * @return 结果
     */
    public int updateMixRecord(MixRecord mixRecord);

    /**
     * 删除窜货记录
     * 
     * @param id 窜货记录ID
     * @return 结果
     */
    public int deleteMixRecordById(Long id);

    /**
     * 批量删除窜货记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMixRecordByIds(Long[] ids);

    /**
     * 窜货总量
     *
     * @param map 部门信息
     * @return 结果
     */
    public int selectmixnum(Map<String, Object> map);

    /**
     * 窜货记录导出
     *
     * @param mixRecord
     * @return
     */
    List<MixRecordVo> selectMixRecordExport(MixRecord mixRecord);
}
