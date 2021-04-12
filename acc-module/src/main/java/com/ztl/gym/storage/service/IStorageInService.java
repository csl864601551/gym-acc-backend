package com.ztl.gym.storage.service;

import java.util.List;
import com.ztl.gym.storage.domain.StorageIn;

/**
 * 入库Service接口
 * 
 * @author ruoyi
 * @date 2021-04-09
 */
public interface IStorageInService 
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
    public int insertStorageIn(StorageIn storageIn);

    /**
     * 修改入库
     * 
     * @param storageIn 入库
     * @return 结果
     */
    public int updateStorageIn(StorageIn storageIn);

    /**
     * 批量删除入库
     * 
     * @param ids 需要删除的入库ID
     * @return 结果
     */
    public int deleteStorageInByIds(Long[] ids);

    /**
     * 删除入库信息
     * 
     * @param id 入库ID
     * @return 结果
     */
    public int deleteStorageInById(Long id);
}
