package com.ztl.gym.idis.service;

import java.util.List;
import com.ztl.gym.idis.domain.IdisRecord;


/**
 * IDIS同步记录Service接口
 * 
 * @author zt_sly
 * @date 2021-07-21
 */
public interface IdisRecordService {


    /**
     * 查询IDIS同步记录
     * 
     * @param id IDIS同步记录ID
     * @return IDIS同步记录
     */
    IdisRecord selectIdisRecordById(Long id);

    /**
     * 查询IDIS同步记录列表
     * 
     * @param idisRecord IDIS同步记录
     * @return IDIS同步记录集合
     */
    List<IdisRecord> selectIdisRecordList(IdisRecord idisRecord);

    /**
     * 批量删除IDIS同步记录
     * 
     * @param ids 需要删除的IDIS同步记录ID
     * @return 结果
     */
    int deleteIdisRecordByIds(Long[] ids);

    /**
     * 写入IDIS同步记录
     *
     * @param idisRecord 单次同步记录
     */
    void writeIdisRecord(IdisRecord idisRecord);

    /**
     * 批量写入IDIS同步记录
     *
     * @param idisRecordList 多次同步记录
     */
    void batchWriteIdisRecord(List<IdisRecord> idisRecordList);

}
