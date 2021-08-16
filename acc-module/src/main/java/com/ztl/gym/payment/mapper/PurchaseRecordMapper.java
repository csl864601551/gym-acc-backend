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
     * 统计已经使用的码数量，包含单码箱码防伪码
     * @param companyId
     * @return
     */
    public Long totalUsedCodeSum(Long companyId);
}
