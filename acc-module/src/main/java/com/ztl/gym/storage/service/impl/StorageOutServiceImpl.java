package com.ztl.gym.storage.service.impl;

import java.util.List;
import java.util.Map;

import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.storage.mapper.StorageInMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.StorageOutMapper;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.service.IStorageOutService;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private StorageInMapper storageInMapper;
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
     * @param map 出库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int insertStorageOut(Map<String, Object> map)
    {
        map.put("create_time",(DateUtils.getNowDate()));
        storageOutMapper.insertStorageOut(map);//插入t_storage_out出库表
        storageInMapper.insertStorageMoveRecord(map);//新增t_storage_move_record产品流转记录表
        storageInMapper.insertStorageMove(map);//新增t_storage_move产品流转明细表
        storageInMapper.insertStorageCode(map);//新增t_storage_code物流码明细表
        storageInMapper.insertPcodeMove(map);//新增t_pcode_move箱码流转记录表
        storageInMapper.insertCodeMove(map);//新增t_code_move单码流转记录表
        storageInMapper.updateProductStock(map);//更新t_product_stock库存统计表
        return 0;
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
