package com.ztl.gym.quota.service.impl;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.quota.domain.Quota;
import com.ztl.gym.quota.mapper.QuotaMapper;
import com.ztl.gym.quota.service.IQuotaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
