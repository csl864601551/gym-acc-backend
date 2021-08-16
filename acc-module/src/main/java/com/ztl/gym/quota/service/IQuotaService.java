package com.ztl.gym.quota.service;

import com.ztl.gym.quota.domain.Quota;

import java.util.List;

/**
 * 配额 Service接口
 * 
 * @author wujinhao
 * @date 2021-07-19
 */
public interface IQuotaService 
{

    /**
     * 查询配额 列表
     * 
     * @param quota 配额 
     * @return 配额 集合
     */
    public List<Quota> selectQuotaList(Quota quota);

}
