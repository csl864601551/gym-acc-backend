package com.ztl.gym.storage.mapper;

import com.ztl.gym.storage.domain.ScanRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 扫码记录Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-28
 */
public interface ScanRecordMapper
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
     * 修改扫码记录
     *
     * @param scanRecord 扫码记录
     * @return 结果
     */
    public int updateScanRecordByCode(ScanRecord scanRecord);

    /**
     * 删除扫码记录
     *
     * @param id 扫码记录ID
     * @return 结果
     */
    public int deleteScanRecordById(Long id);

    /**
     * 批量删除扫码记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteScanRecordByIds(Long[] ids);

    List<Map<String, Object>> selectFlowList(@Param("companyId")Long companyId,@Param("code") String code);

    /**
     * 查询顺时间物流信息
     * @return 结果
     */
    List<Map<String, Object>> selectFlowListAsc(@Param("companyId")Long companyId,@Param("code") String code);


    /**
     * 查询热力图扫码记录
     *
     * @return 扫码记录集合
     */
    public List<ScanRecord> selectRLTList(Map<String,Object> map);



    /**
     * 查询热力图扫码记录
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
     * 扫码总量
     *
     * @param map 部门信息
     * @return 结果
     */
    public int selectSecueityRecordNum(Map<String, Object> map);


    /**
     * 产品出货量 本周
     *
     * @param map
     * @return 结果
     */
    public List<Map<String,Object>> selectCountByTime(Map<String, Object> map);

    /**
     * 产品出货量 本周
     *
     * @param map
     * @return 结果
     */
    public List<Map<String,Object>> selectCountByDate(Map<String, Object> map);


    /**
     * 产品出货量 本周
     *
     * @param map
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
