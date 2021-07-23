package com.ztl.gym.payment.mapper;
import com.ztl.gym.payment.domain.PurchaseRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消费记录 Mapper接口
 * 
 * @author wujinhao
 * @date 2021-07-19
 */
@Repository
public interface PurchaseRecordMapper 
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
     * 删除消费记录 
     * 
     * @param id 消费记录 ID
     * @return 结果
     */
    public int deletePurchaseRecordById(Long id);

    /**
     * 批量删除消费记录 
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePurchaseRecordByIds(Long[] ids);

    /**
     * 统计已经使用的码数量，包含单码箱码防伪码
     * @param companyId
     * @return
     */
    public Long totalUsedCodeSum(Long companyId);
}
