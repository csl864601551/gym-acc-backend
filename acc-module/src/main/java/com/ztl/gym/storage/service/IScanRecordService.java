package com.ztl.gym.storage.service;

import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.storage.domain.ScanRecord;

import java.util.List;
import java.util.Map;

/**
 * 扫码记录Service接口
 * 
 * @author ruoyi
 * @date 2021-04-28
 */
public interface IScanRecordService 
{
    /**
     * 查询扫码记录
     * 
     * @param id 扫码记录ID
     * @return 扫码记录
     */
    public ScanRecord selectScanRecordById(Long id);

    /**
     * 查询扫码记录列表
     * 
     * @param scanRecord 扫码记录
     * @return 扫码记录集合
     */
    public List<ScanRecord> selectScanRecordList(ScanRecord scanRecord);

    /**
     * 新增扫码记录
     * 
     * @param scanRecord 扫码记录
     * @return 结果
     */
    public int insertScanRecord(ScanRecord scanRecord);

    /**
     * 修改扫码记录
     * 
     * @param scanRecord 扫码记录
     * @return 结果
     */
    public int updateScanRecord(ScanRecord scanRecord);

    /**
     * 批量删除扫码记录
     * 
     * @param ids 需要删除的扫码记录ID
     * @return 结果
     */
    public int deleteScanRecordByIds(Long[] ids);

    /**
     * 删除扫码记录信息
     * 
     * @param id 扫码记录ID
     * @return 结果
     */
    public int deleteScanRecordById(Long id);

    Map<String,Object> getScanRecordByCode(Long companyId, String code);


    List<Map<String,Object>> getFlowListByCode(Long companyId, String code);

    CompanyArea getIsMixInfo(CompanyArea area);


    /**
     * 查询热力图扫码记录
     *
     * @param map 扫码记录
     * @return 扫码记录集合
     */
    public List<ScanRecord> selectRLTList(Map<String,Object> map);


    /**
     * 查询扫码地图扫码记录
     *
     * @return 扫码记录集合
     */
    public List<Map<String,Object>> getScanRecordXx(ScanRecord scanRecord);


    /**
     * 扫码总量
     *
     * @param map 部门信息
     * @return 结果
     */
    public int selectScanRecordNum(Map<String, Object> map);


    /**
     * 查验总量
     *
     * @param map 部门信息
     * @return 结果
     */
    public int selectSecueityRecordNum(Map<String, Object> map);


    /**
     * 扫码统计数据
     *
     * @param map
     * @return 结果
     */
    public List<Map<String,Object>> selectCountByTime(Map<String, Object> map);


    /**
     * 扫码统计数据
     *
     * @param map
     * @return 结果
     */
    public List<Map<String,Object>> selectCountByDate(Map<String, Object> map);


    /**
     * 查验总量统计数据
     *
     * @param map 部门信息
     * @return 结果
     */
    public List<Map<String,Object>> selectSecueityRecordByDate(Map<String, Object> map);


    /**
     * 扫码数据top10
     *
     * @param map
     * @return 结果
     */
    public List<Map<String,Object>> selectSmTop10(Map<String, Object> map);
}
