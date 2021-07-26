package com.ztl.gym.quota.service;

import java.util.List;
import com.ztl.gym.quota.domain.Quota;

/**
 * 配额 Service接口
 * 
 * @author wujinhao
 * @date 2021-07-19
 */
public interface IQuotaService 
{

    /**
     * 根据param查询配额
     *
     * @param key 配额 paramKey
     * @return 配额
     */
    public Quota selectQuotaByParamKey(String key);

    /**
     * 查询配额 
     * 
     * @param id 配额 ID
     * @return 配额 
     */
    public Quota selectQuotaById(Long id);

    /**
     * 查询配额 列表
     * 
     * @param quota 配额 
     * @return 配额 集合
     */
    public List<Quota> selectQuotaList(Quota quota);

    /**
     * 新增配额 
     * 
     * @param quota 配额 
     * @return 结果
     */
    public int insertQuota(Quota quota);

    /**
     * 修改配额 
     * 
     * @param quota 配额 
     * @return 结果
     */
    public int updateQuota(Quota quota);

    /**
     * 批量删除配额 
     * 
     * @param ids 需要删除的配额 ID
     * @return 结果
     */
    public int deleteQuotaByIds(Long[] ids);

    /**
     * 删除配额 信息
     * 
     * @param id 配额 ID
     * @return 结果
     */
    public int deleteQuotaById(Long id);
}
