package com.ztl.gym.storage.service.impl;

import java.util.List;
import com.ztl.gym.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.ScanRecordMapper;
import com.ztl.gym.storage.domain.ScanRecord;
import com.ztl.gym.storage.service.IScanRecordService;

/**
 * 扫码记录Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-28
 */
@Service
public class ScanRecordServiceImpl implements IScanRecordService
{
    @Autowired
    private ScanRecordMapper scanRecordMapper;

    /**
     * 查询扫码记录
     *
     * @param id 扫码记录ID
     * @return 扫码记录
     */
    @Override
    public ScanRecord selectScanRecordById(Long id)
    {
        return scanRecordMapper.selectScanRecordById(id);
    }

    /**
     * 查询扫码记录列表
     *
     * @param scanRecord 扫码记录
     * @return 扫码记录
     */
    @Override
    public List<ScanRecord> selectScanRecordList(ScanRecord scanRecord)
    {
        return scanRecordMapper.selectScanRecordList(scanRecord);
    }

    /**
     * 新增扫码记录
     *
     * @param scanRecord 扫码记录
     * @return 结果
     */
    @Override
    public int insertScanRecord(ScanRecord scanRecord)
    {
        scanRecord.setCreateTime(DateUtils.getNowDate());
        return scanRecordMapper.insertScanRecord(scanRecord);
    }

    /**
     * 修改扫码记录
     *
     * @param scanRecord 扫码记录
     * @return 结果
     */
    @Override
    public int updateScanRecord(ScanRecord scanRecord)
    {
        scanRecord.setUpdateTime(DateUtils.getNowDate());
        return scanRecordMapper.updateScanRecord(scanRecord);
    }

    /**
     * 批量删除扫码记录
     *
     * @param ids 需要删除的扫码记录ID
     * @return 结果
     */
    @Override
    public int deleteScanRecordByIds(Long[] ids)
    {
        return scanRecordMapper.deleteScanRecordByIds(ids);
    }

    /**
     * 删除扫码记录信息
     *
     * @param id 扫码记录ID
     * @return 结果
     */
    @Override
    public int deleteScanRecordById(Long id)
    {
        return scanRecordMapper.deleteScanRecordById(id);
    }
}
