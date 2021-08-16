package com.ztl.gym.payment.mapper;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 充值记录 Mapper接口
 * 
 * @author wujinhao
 * @date 2021-07-19
 */
@Repository
public interface PaymentRecordMapper 
{

    /**
     ** 累计充值金额
     * @param map 充值记录
     * @return map
     */
    public BigDecimal getAllAmountNum(Map<String, Object> map);
}
