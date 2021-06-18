package com.ztl.gym.system.service;

import com.ztl.gym.system.domain.TWeixinConfig;

import java.util.List;

/**
 * configService接口
 * 
 * @author ruoyi
 * @date 2021-06-17
 */
public interface ITWeixinConfigService 
{
    /**
     * 查询config
     * 
     * @param id configID
     * @return config
     */
    public TWeixinConfig selectTWeixinConfigById(String id);

    /**
     * 查询config列表
     * 
     * @param tWeixinConfig config
     * @return config集合
     */
    public List<TWeixinConfig> selectTWeixinConfigList(TWeixinConfig tWeixinConfig);

    /**
     * 新增config
     * 
     * @param tWeixinConfig config
     * @return 结果
     */
    public int insertTWeixinConfig(TWeixinConfig tWeixinConfig);

    /**
     * 修改config
     * 
     * @param tWeixinConfig config
     * @return 结果
     */
    public int updateTWeixinConfig(TWeixinConfig tWeixinConfig);

    /**
     * 批量删除config
     * 
     * @param ids 需要删除的configID
     * @return 结果
     */
    public int deleteTWeixinConfigByIds(String[] ids);

    /**
     * 删除config信息
     * 
     * @param id configID
     * @return 结果
     */
    public int deleteTWeixinConfigById(String id);
}
