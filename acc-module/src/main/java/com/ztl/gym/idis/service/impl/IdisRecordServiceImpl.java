package com.ztl.gym.idis.service.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;

import com.ztl.gym.common.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.idis.mapper.IdisRecordMapper;
import com.ztl.gym.idis.domain.IdisRecord;
import com.ztl.gym.idis.service.IdisRecordService;

/**
 * IDIS同步记录Service业务层处理
 *
 * @author zt_sly
 * @date 2021-07-21
 */
@Slf4j
@Service
public class IdisRecordServiceImpl implements IdisRecordService {

    @Autowired
    private IdisRecordMapper idisRecordMapper;


    private ExecutorService threadPool = ThreadPoolUtil.newFixedExecutor("idis-log", 8);


    /**
     * 查询IDIS同步记录
     *
     * @param id IDIS同步记录ID
     * @return IDIS同步记录
     */
    @Override
    public IdisRecord selectIdisRecordById(Long id) {
        return idisRecordMapper.selectIdisRecordById(id);
    }

    /**
     * 查询IDIS同步记录列表
     *
     * @param idisRecord IDIS同步记录
     * @return IDIS同步记录
     */
    @Override
    public List<IdisRecord> selectIdisRecordList(IdisRecord idisRecord) {
        return idisRecordMapper.selectIdisRecordList(idisRecord);
    }

    /**
     * 批量删除IDIS同步记录
     *
     * @param ids 需要删除的IDIS同步记录ID
     * @return 结果
     */
    @Override
    public int deleteIdisRecordByIds(Long[] ids) {
        return idisRecordMapper.deleteIdisRecordByIds(ids);
    }

    /**
     * 写入IDIS同步记录Wrapper, 发生异常不影响同步继续执行
     * @param record 单次请求记录
     */
    @Override
    public void writeIdisRecord(IdisRecord record) {
        try {
            idisRecordMapper.insertIdisRecord(record);
        } catch (Exception e) {
            log.error("写入请求记录失败", e);
        }
    }

    /**
     * 批量写入IDIS同步记录Wrapper, 发生异常不影响同步继续执行
     * @param recordList 多次请求记录
     */
    @Override
    public void batchWriteIdisRecord(List<IdisRecord> recordList) {
        try {
            idisRecordMapper.batchInsertIdisRecord(recordList);
        } catch (Exception e) {
            log.error("批量写入请求记录失败", e);
        }
    }

}
