package com.ztl.gym.storage.service;

import java.util.List;

import com.ztl.gym.storage.domain.StorageTransfer;

/**
 * 调货Service接口
 *
 * @author ruoyi
 * @date 2021-04-09
 */
public interface IStorageTransferService {
    /**
     * 查询调货
     *
     * @param id 调货ID
     * @return 调货
     */
    public StorageTransfer selectStorageTransferById(Long id);

    /**
     * 查询调货列表
     *
     * @param storageTransfer 调货
     * @return 调货集合
     */
    public List<StorageTransfer> selectStorageTransferList(StorageTransfer storageTransfer);

    /**
     * 新增调货
     *
     * @param storageTransfer 调货
     * @return 结果
     */
    public int insertStorageTransfer(StorageTransfer storageTransfer);

    /**
     * 修改调货
     *
     * @param storageTransfer 调货
     * @return 结果
     */
    public int updateStorageTransfer(StorageTransfer storageTransfer);

    /**
     * 批量删除调货
     *
     * @param ids 需要删除的调货ID
     * @return 结果
     */
    public int deleteStorageTransferByIds(Long[] ids);

    /**
     * 删除调货信息
     *
     * @param id 调货ID
     * @return 结果
     */
    public int deleteStorageTransferById(Long id);

    /**
     * 根据调拨单号查询调拨信息
     *
     * @param transferNo
     * @return
     */
    StorageTransfer selectStorageTransferByNo(String transferNo);
}
