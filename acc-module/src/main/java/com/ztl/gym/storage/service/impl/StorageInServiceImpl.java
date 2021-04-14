package com.ztl.gym.storage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
//import com.ztl.gym.common.annotation.Curcompany;
//import com.ztl.gym.common.annotation.DataScope;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.StorageInMapper;
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.service.IStorageInService;
import org.springframework.transaction.annotation.Transactional;

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
    public Map<String, Object> selectStorageInById(Long id)
    {
        Map<String, Object> map=new HashMap<>();
        map= storageInMapper.selectStorageInById(id);//查询t_storage_in明细
        List<Map<String, Object>> listMap=storageInMapper.selectStorageCodeById(id);//查询单码或箱码明细
        map.put("listMap",listMap);
        return map;
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
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int insertStorageIn(Map<String, Object> map)
    {
        map.put("create_time",DateUtils.getNowDate());
        map.put("create_user",SecurityUtils.getLoginUser().getUser().getUserId());
        storageInMapper.insertStorageIn(map);//新增t_storage_in入库表
        storageInMapper.insertStorageMoveRecord(map);//新增t_storage_move_record产品流转记录表
        storageInMapper.insertStorageMove(map);//新增t_storage_move产品流转明细表
        storageInMapper.insertStorageCode(map);//新增t_storage_code物流码明细表
        storageInMapper.insertPcodeMove(map);//新增t_pcode_move箱码流转记录表
        storageInMapper.insertCodeMove(map);//新增t_code_move单码流转记录表
        storageInMapper.updateProductStock(map);//更新t_product_stock库存统计表
        return 0;
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

    @Override
    public Map<String, Object> getCodeInfo(Long id) {
        Map<String, Object> map=new HashMap<>();
        map=storageInMapper.getCodeInfo(id);//获取码产品信息
        List<Map<String, Object>> listMap=storageInMapper.getCodeDetail(id);//获取码产品明细
        map.put("listMap",listMap);
        return map;
    }
}
