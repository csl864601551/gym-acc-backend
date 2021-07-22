package com.ztl.gym.payment.service;

import java.util.List;

import com.ztl.gym.payment.domain.PurchaseRecord;

/**
 * 消费记录 Service接口
 * 
 * @author wujinhao
 * @date 2021-07-19
 */
public interface IPurchaseRecordService 
{
    /**
     * 查询消费记录 
     * 
     * @param id 消费记录 ID
     * @return 消费记录 
     */
    public PurchaseRecord selectPurchaseRecordById(Long id);

    /**
     * 查询消费记录 列表
     * 
     * @param purchaseRecord 消费记录 
     * @return 消费记录 集合
     */
    public List<PurchaseRecord> selectPurchaseRecordList(PurchaseRecord purchaseRecord);

    /**
     * 新增消费记录 
     * 
     * @param purchaseRecord 消费记录 
     * @return 结果
     */
    public int insertPurchaseRecord(PurchaseRecord purchaseRecord);

    /**
     * 修改消费记录 
     * 
     * @param purchaseRecord 消费记录 
     * @return 结果
     */
    public int updatePurchaseRecord(PurchaseRecord purchaseRecord);

    /**
     * 批量删除消费记录 
     * 
     * @param ids 需要删除的消费记录 ID
     * @return 结果
     */
    public int deletePurchaseRecordByIds(Long[] ids);

    /**
     * 删除消费记录 信息
     * 
     * @param id 消费记录 ID
     * @return 结果
     */
    public int deletePurchaseRecordById(Long id);
}
