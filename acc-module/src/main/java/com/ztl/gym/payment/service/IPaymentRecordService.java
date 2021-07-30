package com.ztl.gym.payment.service;

import com.ztl.gym.payment.domain.PaymentRecord;

import java.math.BigDecimal;
import java.util.List;
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
     * 查询充值记录 
     * 
     * @param id 充值记录 ID
     * @return 充值记录 
     */
    public PaymentRecord selectPaymentRecordById(Long id);

    /**
     * 查询充值记录 列表
     * 
     * @param paymentRecord 充值记录 
     * @return 充值记录 集合
     */
    public List<PaymentRecord> selectPaymentRecordList(PaymentRecord paymentRecord);

    /**
     * 新增充值记录 
     * 
     * @param paymentRecord 充值记录 
     * @return 结果
     */
    public int insertPaymentRecord(PaymentRecord paymentRecord);

    /**
     * 修改充值记录 
     * 
     * @param paymentRecord 充值记录 
     * @return 结果
     */
    public int updatePaymentRecord(PaymentRecord paymentRecord);

    /**
     * 批量删除充值记录 
     * 
     * @param ids 需要删除的充值记录 ID
     * @return 结果
     */
    public int deletePaymentRecordByIds(Long[] ids);

    /**
     * 删除充值记录 信息
     * 
     * @param id 充值记录 ID
     * @return 结果
     */
    public int deletePaymentRecordById(Long id);

    /**
     ** 获取统计数值
     *获取充值总数和可用金额总数
     * @param paymentRecord 充值记录
     * @return map
     */
    public Map<String,Object> getStatistics(PaymentRecord paymentRecord);

    /**
     ** 累计充值金额
     * @param map 充值记录
     * @return map
     */
    public BigDecimal getAllAmountNum(Map<String, Object> map);
}
