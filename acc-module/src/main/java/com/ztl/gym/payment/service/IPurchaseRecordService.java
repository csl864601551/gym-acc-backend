package com.ztl.gym.payment.service;

import com.ztl.gym.payment.domain.PurchaseRecord;

import java.util.List;
import java.util.Map;

/**
 * 消费记录 Service接口
 * 
 * @author wujinhao
 * @date 2021-07-19
 */
public interface IPurchaseRecordService 
{

    /**
     ** 获取统计数值
     *获取可用金额总数,总码量，已用码量
     * @param purchaseRecord 充值记录
     * @return map
     */
    public Map<String,Object> getStatistics(PurchaseRecord purchaseRecord);
}
