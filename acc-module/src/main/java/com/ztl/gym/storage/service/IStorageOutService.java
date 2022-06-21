package com.ztl.gym.storage.service;

import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.domain.StorageOutExport;

import java.util.List;
import java.util.Map;

/**
 * 出库Service接口
 *
 * @author ruoyi
 * @date 2021-04-09
 */
public interface IStorageOutService {
    /**
     * 查询出库
     *
     * @param id 出库ID
     * @return 出库
     */
    public StorageOut selectStorageOutById(Long id);

    /**
     * 查询出库列表
     *
     * @param storageOut 出库
     * @return 出库集合
     */
    public List<StorageOut> selectStorageOutList(StorageOut storageOut);

    /**
     * 新增出库
     *
     * @param storageOut 出库
     * @return 结果
     */
    public int insertStorageOut(StorageOut storageOut);

    /**
     * 修改出库
     *
     * @param storageOut 出库
     * @return 结果
     */
    public int updateStorageOut(StorageOut storageOut);

    /**
     * 批量删除出库
     *
     * @param ids 需要删除的出库ID
     * @return 结果
     */
    public int deleteStorageOutByIds(Long[] ids);
    /**
     * 撤销出库
     *
     * @param id 需要撤销出库ID
     * @return 结果
     */
    public int backStorageOutById(Long id,int status);

    /**
     * 删除出库信息
     *
     * @param id 出库ID
     * @return 结果
     */
    public int deleteStorageOutById(Long id);

    int updateOutStatusByCode(Map<String, Object> map);

    List<Map<String, Object>> getCodeDetailById(Long companyId, Integer id);

    /**
     * 根据调拨单新增一条出库单
     *
     * @param transferId
     * @return
     */
    int insertByTransfer(long transferId);

    /**
     * 根据调拨单删除对应的出库单
     *
     * @param transferId
     * @return
     */
    int deleteByTransfer(long transferId);

    List<Map<String,Object>> selectDayCount(Map<String, Object> map);

    /**
     * 产品出货量
     *
     * @param map 需要撤销出库dept
     * @return 结果
     */
    public int selectCountByDept(Map<String, Object> map);

    /**
     * 产品出货量 本周
     *
     * @param map
     * @return 结果
     */
    List<Map<String, Object>> selectCountByWeek(Map<String, Object> map);

    /**
     * 导出出库信息
     * @param storageOut
     * @return
     */
    List<StorageOutExport> selectStorageOutExport(StorageOut storageOut);
}
