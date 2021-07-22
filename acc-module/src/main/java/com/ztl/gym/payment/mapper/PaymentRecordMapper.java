package com.ztl.gym.payment.mapper;

import java.util.List;

import com.ztl.gym.payment.domain.PaymentRecord;
import org.springframework.stereotype.Repository;

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
     * 删除充值记录 
     * 
     * @param id 充值记录 ID
     * @return 结果
     */
    public int deletePaymentRecordById(Long id);

    /**
     * 批量删除充值记录 
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePaymentRecordByIds(Long[] ids);
}
