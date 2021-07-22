package com.ztl.gym.payment.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.ztl.gym.common.utils.CommonUtil;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.payment.domain.PaymentRecord;
import com.ztl.gym.payment.mapper.PaymentRecordMapper;
import com.ztl.gym.quota.domain.Quota;
import com.ztl.gym.quota.domain.QuotaConstants;
import com.ztl.gym.quota.mapper.QuotaMapper;
import com.ztl.gym.quota.service.IQuotaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.payment.service.IPaymentRecordService;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private IQuotaService quotaService;

    /**
     * 查询充值记录
     *
     * @param id 充值记录 ID
     * @return 充值记录
     */
    @Override
    public PaymentRecord selectPaymentRecordById(Long id) {
        return paymentRecordMapper.selectPaymentRecordById(id);
    }

    /**
     * 查询充值记录 列表
     *
     * @param paymentRecord 充值记录
     * @return 充值记录
     */
    @Override
    public List<PaymentRecord> selectPaymentRecordList(PaymentRecord paymentRecord) {
        logger.info("the method selectPaymentRecordList enter");
        return paymentRecordMapper.selectPaymentRecordList(paymentRecord);
    }

    /**
     * 新增充值记录
     *
     * @param paymentRecord 充值记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPaymentRecord(PaymentRecord paymentRecord) {
        logger.info("the method insertPaymentRecord enter");
        //添加配额表,如果配额表没有记录新增，存在记录则修改
        Quota query = new Quota();
        query.setCompanyId(paymentRecord.getCompanyId());
        query.setParamKey(QuotaConstants.MONEY);
        List<Quota> quotaList = quotaService.selectQuotaList(query);
        if (CollectionUtil.isEmpty(quotaList)) {
            logger.info("配额表无记录，进行金额新增操作");
            quotaService.insertQuota(buildQuotaBean(paymentRecord));
        } else {
            logger.info("the method insertPaymentRecord end");
            Quota quota = quotaList.get(0);
            BigDecimal money = quota.getParamValue();
            quota.setParamValue(money.add(paymentRecord.getPayAmount()));
            logger.info("配额表有记录，进行金额更新操作");
            quotaService.updateQuota(quota);
        }
        paymentRecord.setCreateTime(DateUtils.getNowDate());
        paymentRecord.setOrderNo(CommonUtil.buildOrderNo());
        logger.info("the method insertPaymentRecord end");
        return paymentRecordMapper.insertPaymentRecord(paymentRecord);
    }

    /**
     * 修改充值记录
     *
     * @param paymentRecord 充值记录
     * @return 结果
     */
    @Override
    public int updatePaymentRecord(PaymentRecord paymentRecord) {
        logger.info("the method updatePaymentRecord enter");
        paymentRecord.setUpdateTime(DateUtils.getNowDate());
        return paymentRecordMapper.updatePaymentRecord(paymentRecord);
    }

    /**
     * 批量删除充值记录
     *
     * @param ids 需要删除的充值记录 ID
     * @return 结果
     */
    @Override
    public int deletePaymentRecordByIds(Long[] ids) {
        return paymentRecordMapper.deletePaymentRecordByIds(ids);
    }

    /**
     * 删除充值记录 信息
     *
     * @param id 充值记录 ID
     * @return 结果
     */
    @Override
    public int deletePaymentRecordById(Long id) {
        return paymentRecordMapper.deletePaymentRecordById(id);
    }

    /**
     * 构建配额对象
     *
     * @param paymentRecord 支付对象
     * @return Quota
     */
    public Quota buildQuotaBean(PaymentRecord paymentRecord) {
        Quota quota = new Quota();
        quota.setParamKey(QuotaConstants.MONEY);
        quota.setParamName(QuotaConstants.MONEY_NAME);
        quota.setParamValue(paymentRecord.getPayAmount());
        quota.setCompanyId(paymentRecord.getCompanyId());
        quota.setCreateBy(paymentRecord.getCreateBy());
        return quota;
    }

}
