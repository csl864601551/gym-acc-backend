package com.ztl.gym.storage.mapper;

import java.util.List;
import java.util.Map;

import com.ztl.gym.storage.domain.StorageIn;
import org.springframework.stereotype.Repository;

/**
 * 入库Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@Repository
public interface StorageInMapper
{
    /**
     * 查询入库
     *
     * @param id 入库ID
     * @return 入库
     */
    public StorageIn selectStorageInById(Long id);

    /**
     * 查询入库列表
     *
     * @param storageIn 入库
     * @return 入库集合
     */
    public List<StorageIn> selectStorageInList(StorageIn storageIn);

    /**
     * 新增入库
     *
     * @param storageIn 入库
     * @return 结果
     */
    public int insertStorageIn(Map<String, Object> storageIn);

    /**
     * 修改入库
     *
     * @param storageIn 入库
     * @return 结果
     */
    public int updateStorageIn(StorageIn storageIn);

    /**
     * 删除入库
     *
     * @param id 入库ID
     * @return 结果
     */
    public int deleteStorageInById(Long id);

    /**
     * 批量删除入库
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStorageInByIds(Long[] ids);

    void insertPcodeFlow(Map<String, Object> storageIn);

    void insertCodeFlow(Map<String, Object> storageIn);

    void updateProductStock(Map<String, Object> storageIn);

    StorageIn selectStorageCodeById(Long id);

    Map<String, Object> getCodeInfo(String code);

    List<Map<String, Object>> getCodeDetail(String code);
}
