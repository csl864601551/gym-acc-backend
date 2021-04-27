package com.ztl.gym.storage.mapper;

import java.util.List;
import java.util.Map;

import com.ztl.gym.storage.domain.StorageOut;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
    public int insertStorageOut(Map<String, Object> storageOut);

    /**
     * 新增出库
     *
     * @param storageOut 出库
     * @return 结果
     */
    public int insertStorageOutV2(StorageOut storageOut);

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

    int updateOutStatusByCode(Map<String, Object> map);

    List<Map<String, Object>> getCodeDetailById(@Param("companyId") Long companyId, @Param("id") Integer id);

    /**
     * 根据调拨单号删除出库单
     *
     * @param transferNo
     * @return
     */
    int deleteByTransfer(String transferNo);
}
