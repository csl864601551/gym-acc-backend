package com.ztl.gym.storage.service.impl;

import java.util.List;
import com.ztl.gym.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.StorageOutMapper;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.service.IStorageOutService;

/**
 * 出库Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@Service
public class StorageOutServiceImpl implements IStorageOutService
{
    @Autowired
    private StorageOutMapper storageOutMapper;

    /**
     * 查询出库
     *
     * @param id 出库ID
     * @return 出库
     */
    @Override
    public StorageOut selectStorageOutById(Long id)
    {
        return storageOutMapper.selectStorageOutById(id);
    }

    /**
     * 查询出库列表
     *
     * @param storageOut 出库
     * @return 出库
     */
    @Override
    public List<StorageOut> selectStorageOutList(StorageOut storageOut)
    {
        return storageOutMapper.selectStorageOutList(storageOut);
    }

    /**
     * 新增出库
     *
     * @param storageOut 出库
     * @return 结果
     */
    @Override
    public int insertStorageOut(StorageOut storageOut)
    {
        storageOut.setCreateTime(DateUtils.getNowDate());
        return storageOutMapper.insertStorageOut(storageOut);
    }

    /**
     * 修改出库
     *
     * @param storageOut 出库
     * @return 结果
     */
    @Override
    public int updateStorageOut(StorageOut storageOut)
    {
        storageOut.setUpdateTime(DateUtils.getNowDate());
        return storageOutMapper.updateStorageOut(storageOut);
    }

    /**
     * 批量删除出库
     *
     * @param ids 需要删除的出库ID
     * @return 结果
     */
    @Override
    public int deleteStorageOutByIds(Long[] ids)
    {
        return storageOutMapper.deleteStorageOutByIds(ids);
    }

    /**
     * 删除出库信息
     *
     * @param id 出库ID
     * @return 结果
     */
    @Override
    public int deleteStorageOutById(Long id)
    {
        return storageOutMapper.deleteStorageOutById(id);
    }
}
