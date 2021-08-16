package com.ztl.gym.payment.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.payment.domain.PurchaseRecord;
import com.ztl.gym.payment.mapper.PurchaseRecordMapper;
import com.ztl.gym.payment.service.IPurchaseRecordService;
import com.ztl.gym.quota.domain.Quota;
import com.ztl.gym.quota.service.IQuotaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消费记录 Service业务层处理
 *
 * @author wujinhao
 * @date 2021-07-19
 */
@Service
public class PurchaseRecordServiceImpl implements IPurchaseRecordService {
    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(PaymentRecordServiceImpl.class);
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;

    @Autowired
    private IQuotaService quotaService;

    /**
     ** 获取统计数值
     *获取充值总数和可用金额总数
     * @param purchaseRecord 充值记录
     * @return map
     */
    @Override
    public Map<String, Object> getStatistics(PurchaseRecord purchaseRecord) {
        logger.info("the method getStatistics enter");
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        Map<String, Object> total = null;
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            purchaseRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
            //如果是企业返回余额
            //查询配额表
            Quota query = new Quota();
            query.setCompanyId(purchaseRecord.getCompanyId());
            List<Quota> quotaList = quotaService.selectQuotaList(query);
            total = new HashMap<>(quotaList.size() + 1);
            if (CollectionUtil.isEmpty(quotaList)) {
                logger.info("该企业没有充值和购码使用记录");
                return total;
            }
            //遍历塞值  key-value
            for (Quota quota : quotaList) {
                total.put(quota.getParamKey(),quota.getParamValue());
            }
            //获取已用码量
            Long usedCodeSum = purchaseRecordMapper.totalUsedCodeSum(purchaseRecord.getCompanyId());
            total.put("usedCode",usedCodeSum);
        }
        return total;
    }

}
