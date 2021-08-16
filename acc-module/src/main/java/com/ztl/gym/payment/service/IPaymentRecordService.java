package com.ztl.gym.payment.service;


import java.math.BigDecimal;
import java.util.Map;

/**
 * 充值记录 Service接口
 * 
 * @author wujinhao
 * @date 2021-07-19
 */
public interface IPaymentRecordService 
{

    /**
     ** 累计充值金额
     * @param map 充值记录
     * @return map
     */
    public BigDecimal getAllAmountNum(Map<String, Object> map);
}
