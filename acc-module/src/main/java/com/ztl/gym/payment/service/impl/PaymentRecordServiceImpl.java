package com.ztl.gym.payment.service.impl;

import com.ztl.gym.payment.service.IPaymentRecordService;
import com.ztl.gym.payment.mapper.PaymentRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 充值记录 Service业务层处理
 *
 * @author wujinhao
 * @date 2021-07-19
 */
@Service
public class PaymentRecordServiceImpl implements IPaymentRecordService {
    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(PaymentRecordServiceImpl.class);

    @Autowired
    private PaymentRecordMapper paymentRecordMapper;



    /**
     ** 累计充值金额
     * @param map 充值记录
     * @return map
     */
    @Override
    public BigDecimal getAllAmountNum(Map<String, Object> map) {
        return paymentRecordMapper.getAllAmountNum(map);
    }

}
