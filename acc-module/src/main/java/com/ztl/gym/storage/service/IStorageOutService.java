package com.ztl.gym.storage.service;

import java.util.List;
import java.util.Map;

import com.ztl.gym.storage.domain.StorageOut;

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
    public int insertStorageOut(Map<String, Object> storageOut);

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
     * 删除出库信息
     *
     * @param id 出库ID
     * @return 结果
     */
    public int deleteStorageOutById(Long id);

    int updateOutStatusByCode(Map<String, Object> map);

    List<Map<String,Object>> getCodeDetailById(Long companyId,Integer id);
}
