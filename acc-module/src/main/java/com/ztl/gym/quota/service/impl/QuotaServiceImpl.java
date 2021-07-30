package com.ztl.gym.quota.service.impl;

import java.util.List;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.payment.service.impl.PaymentRecordServiceImpl;
import com.ztl.gym.quota.domain.Quota;
import com.ztl.gym.quota.mapper.QuotaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.quota.service.IQuotaService;

/**
 * 配额 Service业务层处理
 *
 * @author wujinhao
 * @date 2021-07-19
 */
@Service
public class QuotaServiceImpl implements IQuotaService
{
    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(QuotaServiceImpl.class);
    @Autowired
    private QuotaMapper quotaMapper;

    /**
     * 根据param查询配额
     *
     * @param key 配额 paramKey
     * @return 配额
     */
    @Override
    public Quota selectQuotaByParamKey(String key) {
        logger.info("the method selectQuotaByParamKey enter ");
        Quota quota = new Quota();
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            quota.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        } else {
            quota.setCompanyId(companyId);
        }
        quota.setParamKey(key);
        return quotaMapper.selectQuotaByParam(quota);
    }

    /**
     * 查询配额 
     *
     * @param id 配额 ID
     * @return 配额 
     */
    @Override
    public Quota selectQuotaById(Long id)
    {
        return quotaMapper.selectQuotaById(id);
    }

    /**
     * 查询配额 列表
     *
     * @param quota 配额 
     * @return 配额 
     */
    @Override
    public List<Quota> selectQuotaList(Quota quota)
    {
        return quotaMapper.selectQuotaList(quota);
    }

    /**
     * 新增配额 
     *
     * @param quota 配额 
     * @return 结果
     */
    @Override
    public int insertQuota(Quota quota)
    {
        logger.info("the method insertQuota enter,param is {}", quota);
        quota.setCreateTime(DateUtils.getNowDate());
        return quotaMapper.insertQuota(quota);
    }

    /**
     * 修改配额 
     *
     * @param quota 配额 
     * @return 结果
     */
    @Override
    public int updateQuota(Quota quota)
    {
        logger.info("the method updateQuota enter,param is {}", quota);
        quota.setUpdateTime(DateUtils.getNowDate());
        return quotaMapper.updateQuota(quota);
    }

    /**
     * 批量删除配额 
     *
     * @param ids 需要删除的配额 ID
     * @return 结果
     */
    @Override
    public int deleteQuotaByIds(Long[] ids)
    {
        return quotaMapper.deleteQuotaByIds(ids);
    }

    /**
     * 删除配额 信息
     *
     * @param id 配额 ID
     * @return 结果
     */
    @Override
    public int deleteQuotaById(Long id)
    {
        return quotaMapper.deleteQuotaById(id);
    }

    @Override
    public int updateQuotaByAddCode(Long codeNum) {

        return quotaMapper.updateQuotaByAddCode(codeNum,SecurityUtils.getLoginUserCompany().getDeptId());
    }
}
