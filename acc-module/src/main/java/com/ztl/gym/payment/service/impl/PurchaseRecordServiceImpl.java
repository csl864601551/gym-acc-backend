package com.ztl.gym.payment.service.impl;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.CommonUtil;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.payment.domain.PaymentRecord;
import com.ztl.gym.payment.domain.PurchaseRecord;
import com.ztl.gym.payment.mapper.PurchaseRecordMapper;
import com.ztl.gym.quota.domain.Quota;
import com.ztl.gym.quota.domain.QuotaConstants;
import com.ztl.gym.quota.mapper.QuotaMapper;
import com.ztl.gym.quota.service.IQuotaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.payment.service.IPurchaseRecordService;
import org.springframework.transaction.annotation.Transactional;

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
     * 查询消费记录
     *
     * @param id 消费记录 ID
     * @return 消费记录
     */
    @Override
    public PurchaseRecord selectPurchaseRecordById(Long id) {
        return purchaseRecordMapper.selectPurchaseRecordById(id);
    }

    /**
     * 查询消费记录 列表
     *
     * @param purchaseRecord 消费记录
     * @return 消费记录
     */
    @Override
    public List<PurchaseRecord> selectPurchaseRecordList(PurchaseRecord purchaseRecord) {
        return purchaseRecordMapper.selectPurchaseRecordList(purchaseRecord);
    }

    /**
     * 新增消费记录
     *
     * @param purchaseRecord 消费记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPurchaseRecord(PurchaseRecord purchaseRecord) {
        logger.info("the method insertPurchaseRecord enter,param is {}", purchaseRecord);
        purchaseRecord.setCreateTime(DateUtils.getNowDate());
        //生成订单号
        purchaseRecord.setOrderNo(CommonUtil.buildOrderNo());
        //更新配额表里插入余额和码值
        logger.info("更新企业配额信息,码量和余额");
        updateQuotaRecord(purchaseRecord);
        logger.info("the method insertPurchaseRecord end");
        return insertPurchaseRecordOperation(purchaseRecord);
    }


    public int insertPurchaseRecordOperation(PurchaseRecord purchaseRecord) {
        logger.info("the method insertPurchaseRecordOperation enter,operation is insert");
        return purchaseRecordMapper.insertPurchaseRecord(purchaseRecord);
    }

    /**
     * 修改消费记录
     *
     * @param purchaseRecord 消费记录
     * @return 结果
     */
    @Override
    public int updatePurchaseRecord(PurchaseRecord purchaseRecord) {
        purchaseRecord.setUpdateTime(DateUtils.getNowDate());
        return purchaseRecordMapper.updatePurchaseRecord(purchaseRecord);
    }

    /**
     * 批量删除消费记录
     *
     * @param ids 需要删除的消费记录 ID
     * @return 结果
     */
    @Override
    public int deletePurchaseRecordByIds(Long[] ids) {
        return purchaseRecordMapper.deletePurchaseRecordByIds(ids);
    }

    /**
     * 删除消费记录 信息
     *
     * @param id 消费记录 ID
     * @return 结果
     */
    @Override
    public int deletePurchaseRecordById(Long id) {
        return purchaseRecordMapper.deletePurchaseRecordById(id);
    }

    /**
     * 跟新配额表
     *
     * @param purchaseRecord bean
     */
    public void updateQuotaRecord(PurchaseRecord purchaseRecord) {
        Quota query = new Quota();
        query.setCompanyId(purchaseRecord.getCompanyId());
        List<Quota> list = quotaService.selectQuotaList(query);
        List<Quota> moneyList = list.stream().filter(item -> item.getParamKey().equals(QuotaConstants.MONEY))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(moneyList)) {
            logger.error("没有余额无法购买");
            throw new CustomException("没有余额无法购买.", HttpStatus.ERROR);
        }
        Quota money = moneyList.get(0);
        BigDecimal balance = money.getParamValue();
        //如果配额表没有金额记录或者余额不足不允许购买
        if (balance.compareTo(purchaseRecord.getPurchaseAmount()) <= 0) {
            logger.error("余额不足无法购买");
            throw new CustomException("余额不足无法购买.", HttpStatus.ERROR);
        }
        money.setParamValue(balance.subtract(purchaseRecord.getPurchaseAmount()));
        logger.info("进行金额更新操作");
        quotaService.updateQuota(money);
        List<Quota> codeList = list.stream().
                filter(item -> item.getParamKey().equals(QuotaConstants.CODE))
                .collect(Collectors.toList());
        //如果配额没有码量则新增，有则更新
        if (CollectionUtil.isEmpty(codeList)) {
            logger.info("配额表无记录，进行码量新增操作");
            quotaService.insertQuota(buildQuotaBean(purchaseRecord));
        } else {
            Quota code = codeList.get(0);
            code.setParamValue(code.getParamValue().add(BigDecimal.valueOf(purchaseRecord.getCount())));
            logger.info("配额表有记录，进行码量更新操作");
            quotaService.updateQuota(code);
        }
    }

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
        }
        return total;
    }

    /**
     * 构建配额对象
     *
     * @param purchaseRecord 消费对象
     * @return Quota
     */
    public Quota buildQuotaBean(PurchaseRecord purchaseRecord) {
        Quota quota = new Quota();
        quota.setParamKey(QuotaConstants.CODE);
        quota.setParamName(QuotaConstants.CODE_NAME);
        quota.setParamValue(BigDecimal.valueOf(purchaseRecord.getCount()));
        quota.setCompanyId(purchaseRecord.getCompanyId());
        quota.setCreateBy(purchaseRecord.getCreateBy());
        return quota;
    }
}
