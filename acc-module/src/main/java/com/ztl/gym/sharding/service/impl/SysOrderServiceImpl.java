package com.ztl.gym.sharding.service.impl;

import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.sharding.domain.SysOrder;
import com.ztl.gym.sharding.mapper.SysOrderMapper;
import com.ztl.gym.sharding.service.ISysOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysOrderServiceImpl implements ISysOrderService
{
    @Autowired
    private SysOrderMapper myShardingMapper;

    /**
     * 查询订单
     *
     * @param orderId 订单编号
     * @return 订单信息
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public SysOrder selectSysOrderById(Long orderId)
    {
        return myShardingMapper.selectSysOrderById(orderId);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public SysOrder selectSysOrder(long id)
    {
        return myShardingMapper.selectSysOrder(id);
    }

    /**
     * 查询订单列表
     *
     * @param sysOrder 订单信息
     * @return 订单列表
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<SysOrder> selectSysOrderList(SysOrder sysOrder)
    {
        return myShardingMapper.selectSysOrderList(sysOrder);
    }

    /**
     * 新增订单
     *
     * @param sysOrder 订单
     * @return 结果
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public int insertSysOrder(SysOrder sysOrder)
    {
        return myShardingMapper.insertSysOrder(sysOrder);
    }
}
