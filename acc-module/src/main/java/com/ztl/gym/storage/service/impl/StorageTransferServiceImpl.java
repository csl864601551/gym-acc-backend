package com.ztl.gym.storage.service.impl;

import java.util.List;

import com.ztl.gym.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.StorageTransferMapper;
import com.ztl.gym.storage.domain.StorageTransfer;
import com.ztl.gym.storage.service.IStorageTransferService;

/**
 * 调货Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@Service
public class StorageTransferServiceImpl implements IStorageTransferService {
    @Autowired
    private StorageTransferMapper storageTransferMapper;

    /**
     * 查询调货
     *
     * @param id 调货ID
     * @return 调货
     */
    @Override
    public StorageTransfer selectStorageTransferById(Long id) {
        return storageTransferMapper.selectStorageTransferById(id);
    }

    /**
     * 根据调拨单号查询调拨信息
     *
     * @param transferNo
     * @return
     */
    @Override
    public StorageTransfer selectStorageTransferByNo(String transferNo) {
        return storageTransferMapper.selectStorageTransferByNo(transferNo);
    }

    /**
     * 查询调货列表
     *
     * @param storageTransfer 调货
     * @return 调货
     */
    @Override
    public List<StorageTransfer> selectStorageTransferList(StorageTransfer storageTransfer) {
        return storageTransferMapper.selectStorageTransferList(storageTransfer);
    }

    /**
     * 新增调货
     *
     * @param storageTransfer 调货
     * @return 结果
     */
    @Override
    public int insertStorageTransfer(StorageTransfer storageTransfer) {
        storageTransfer.setCreateTime(DateUtils.getNowDate());
        return storageTransferMapper.insertStorageTransfer(storageTransfer);
    }

    /**
     * 修改调货
     *
     * @param storageTransfer 调货
     * @return 结果
     */
    @Override
    public int updateStorageTransfer(StorageTransfer storageTransfer) {
        storageTransfer.setUpdateTime(DateUtils.getNowDate());
        return storageTransferMapper.updateStorageTransfer(storageTransfer);
    }

    /**
     * 批量删除调货
     *
     * @param ids 需要删除的调货ID
     * @return 结果
     */
    @Override
    public int deleteStorageTransferByIds(Long[] ids) {
        return storageTransferMapper.deleteStorageTransferByIds(ids);
    }

    /**
     * 删除调货信息
     *
     * @param id 调货ID
     * @return 结果
     */
    @Override
    public int deleteStorageTransferById(Long id) {
        return storageTransferMapper.deleteStorageTransferById(id);
    }
}
