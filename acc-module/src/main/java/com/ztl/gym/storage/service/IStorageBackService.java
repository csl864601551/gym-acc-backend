package com.ztl.gym.storage.service;

import java.util.List;
import com.ztl.gym.storage.domain.StorageBack;

/**
 * 退货Service接口
 * 
 * @author ruoyi
 * @date 2021-04-19
 */
public interface IStorageBackService 
{
    /**
     * 查询退货
     * 
     * @param id 退货ID
     * @return 退货
     */
    public StorageBack selectStorageBackById(Long id);

    /**
     * 查询退货列表
     * 
     * @param storageBack 退货
     * @return 退货集合
     */
    public List<StorageBack> selectStorageBackList(StorageBack storageBack);

    /**
     * 新增退货
     * 
     * @param storageBack 退货
     * @return 结果
     */
    public int insertStorageBack(StorageBack storageBack);

    /**
     * 修改退货
     * 
     * @param storageBack 退货
     * @return 结果
     */
    public int updateStorageBack(StorageBack storageBack);

    /**
     * 批量删除退货
     * 
     * @param ids 需要删除的退货ID
     * @return 结果
     */
    public int deleteStorageBackByIds(Long[] ids);

    /**
     * 删除退货信息
     * 
     * @param id 退货ID
     * @return 结果
     */
    public int deleteStorageBackById(Long id);
}
