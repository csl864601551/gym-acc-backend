package com.ztl.gym.sharding.domain;


import com.ztl.gym.common.core.domain.BaseEntity;

/**
 * 订单对象 tb_order
 *
 * @author ruoyi
 */
public class SysOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单编号 */
    private Long orderId;

    /** 用户编号 */
    private Long userId;

    /** 状态 */
    private String status;

    /** 订单编号 */
    private String orderNo;

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }
}

