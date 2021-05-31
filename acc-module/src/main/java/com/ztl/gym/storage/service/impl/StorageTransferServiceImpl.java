package com.ztl.gym.storage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.storage.service.IStorageOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.StorageTransferMapper;
import com.ztl.gym.storage.domain.StorageTransfer;
import com.ztl.gym.storage.service.IStorageTransferService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 调货Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-22
 */
@Service
public class StorageTransferServiceImpl implements IStorageTransferService {
    @Autowired
    private StorageTransferMapper storageTransferMapper;
    @Autowired
    private IStorageOutService storageOutService;

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
        storageTransfer.setStatus(StorageTransfer.STATUS_WAIT);
        storageTransfer.setEnable(StorageTransfer.ENABLE_NO);
        storageTransfer.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        storageTransfer.setTenantId(SecurityUtils.getLoginUserTopCompanyId());
        storageTransfer.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
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

    /**
     * 启用/禁用 调拨单 【只有状态为待发货的调拨单才可以进行此可操作， 如果】
     *
     * @param transferId 调拨单id
     * @param enable     启用状态
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateEnable(long transferId, int enable) {
        int res = 0;
        StorageTransfer storageTransfer = storageTransferMapper.selectStorageTransferById(transferId);
        if (storageTransfer.getStatus() == StorageTransfer.STATUS_WAIT) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", transferId);
            params.put("enable", enable);
            res = storageTransferMapper.updateEnable(params);

            if (enable == StorageTransfer.ENABLE_YES) {
                //启用时对应经销商新增一条对应调拨单的出库单
                storageOutService.insertByTransfer(transferId);
            } else {
                //禁用时对应经销商删除对应调拨单的出库单
                storageOutService.deleteByTransfer(transferId);
            }
        } else {
            throw new CustomException("只有状态为待发货的调拨单才可进行此操作");
        }

        return res;
    }
}
