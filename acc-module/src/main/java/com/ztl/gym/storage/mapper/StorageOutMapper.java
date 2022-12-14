package com.ztl.gym.storage.mapper;

import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.domain.StorageOutExport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 出库Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@Repository
public interface StorageOutMapper {
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
     * 删除出库
     *
     * @param id 出库ID
     * @return 结果
     */
    public int deleteStorageOutById(Long id);

    /**
     * 批量删除出库
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStorageOutByIds(Long[] ids);
    /**
     * 撤销出库
     *
     * @param id 需要撤销出库数据ID
     * @return 结果
     */
    public int backStorageOutById(@Param("id")Long id,@Param("status")int status);

    int updateOutStatusByCode(Map<String, Object> map);

    List<Map<String, Object>> getCodeDetailById(@Param("companyId") Long companyId, @Param("id") Integer id);

    /**
     * 根据调拨单号删除出库单
     *
     * @param transferNo
     * @return
     */
    int deleteByTransfer(String transferNo);

    List<Map<String,Object>> selectDayCount(Map<String, Object> map);


    /**
     * 产品出货量
     *
     * @param map 需要撤销出库dept
     * @return 结果
     */
    int selectCountByDept(Map<String, Object> map);

    List<Map<String, Object>> selectCountByWeek(Map<String, Object> map);

    /**
     * 导出出库信息
     * @param storageOut
     * @return
     */
    List<StorageOutExport> selectStorageOutExport(StorageOut storageOut);

    int updateStorageOutByErpCode(StorageOut storageOut);
}
