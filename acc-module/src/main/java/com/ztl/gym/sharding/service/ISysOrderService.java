package com.ztl.gym.sharding.service;


import com.ztl.gym.sharding.domain.SysOrder;

import java.util.List;

/**
 * 订单Service接口
 *
 * @author ruoyi
 */
public interface ISysOrderService
{
    /**
     * 查询订单
     *
     * @param orderId 订单编号
     * @return 订单信息
     */
    public SysOrder selectSysOrderById(Long orderId);

    /**
     * 查询订单列表
     *
     * @param sysOrder 订单信息
     * @return 订单列表
     */
    public List<SysOrder> selectSysOrderList(SysOrder sysOrder);

    /**
     * 新增订单
     *
     * @param sysOrder 订单
     * @return 结果
     */
    public int insertSysOrder(SysOrder sysOrder);
}
