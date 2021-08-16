package com.ztl.gym.idis.mapper;

import java.util.List;
import com.ztl.gym.idis.domain.IdisRecord;

/**
 * IDIS同步日志Mapper接口
 * 
 * @author zt_sly
 * @date 2021-07-21
 */
public interface IdisRecordMapper {

    /**
     * 查询IDIS同步日志
     * 
     * @param id IDIS同步日志ID
     * @return IDIS同步日志
     */
    IdisRecord selectIdisRecordById(Long id);

    /**
     * 查询IDIS同步日志列表
     * 
     * @param idisRecord IDIS同步日志
     * @return IDIS同步日志集合
     */
    List<IdisRecord> selectIdisRecordList(IdisRecord idisRecord);

    /**
     * 批量删除IDIS同步日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteIdisRecordByIds(Long[] ids);

    /**
     * 写入IDIS同步记录
     *
     * @param idisRecord 单次同步记录
     */
    void insertIdisRecord(IdisRecord idisRecord);

    /**
     * 批量写入IDIS同步记录
     *
     * @param idisRecordList 多次同步记录
     */
    void batchInsertIdisRecord(List<IdisRecord> idisRecordList);

}
