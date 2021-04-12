package com.ztl.gym.storage.service.impl;

import java.util.List;

import com.ztl.gym.common.annotation.Curcompany;
import com.ztl.gym.common.annotation.DataScope;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.StorageInMapper;
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.service.IStorageInService;

/**
 * 入库Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@Service
public class StorageInServiceImpl implements IStorageInService
{
    @Autowired
    private StorageInMapper storageInMapper;

    /**
     * 查询入库
     *
     * @param id 入库ID
     * @return 入库
     */
    @Override
    public StorageIn selectStorageInById(Long id)
    {
        return storageInMapper.selectStorageInById(id);
    }

    /**
     * 查询入库列表
     *
     * @param storageIn 入库
     * @return 入库
     */
    @Override
    public List<StorageIn> selectStorageInList(StorageIn storageIn)
    {
        return storageInMapper.selectStorageInList(storageIn);
    }

    /**
     * 新增入库
     *
     * @param storageIn 入库
     * @return 结果
     */
    @Override
    public int insertStorageIn(StorageIn storageIn)
    {
        storageIn.setCreateTime(DateUtils.getNowDate());
        storageIn.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return storageInMapper.insertStorageIn(storageIn);
    }

    /**
     * 修改入库
     *
     * @param storageIn 入库
     * @return 结果
     */
    @Override
    public int updateStorageIn(StorageIn storageIn)
    {
        storageIn.setUpdateTime(DateUtils.getNowDate());
        return storageInMapper.updateStorageIn(storageIn);
    }

    /**
     * 批量删除入库
     *
     * @param ids 需要删除的入库ID
     * @return 结果
     */
    @Override
    public int deleteStorageInByIds(Long[] ids)
    {
        return storageInMapper.deleteStorageInByIds(ids);
    }

    /**
     * 删除入库信息
     *
     * @param id 入库ID
     * @return 结果
     */
    @Override
    public int deleteStorageInById(Long id)
    {
        return storageInMapper.deleteStorageInById(id);
    }
}
