package com.ztl.gym.payment.service;

import java.util.List;

import com.ztl.gym.payment.domain.PriceConfig;

/**
 * 价格 平台设置码包价格Service接口
 * 
 * @author wujinhao
 * @date 2021-07-19
 */
public interface IPriceConfigService 
{
    /**
     * 查询价格 平台设置码包价格
     * 
     * @param id 价格 平台设置码包价格ID
     * @return 价格 平台设置码包价格
     */
    public PriceConfig selectPriceConfigById(Long id);

    /**
     * 查询价格 平台设置码包价格列表
     * 
     * @param priceConfig 价格 平台设置码包价格
     * @return 价格 平台设置码包价格集合
     */
    public List<PriceConfig> selectPriceConfigList(PriceConfig priceConfig);

    /**
     * 新增价格 平台设置码包价格
     * 
     * @param priceConfig 价格 平台设置码包价格
     * @return 结果
     */
    public int insertPriceConfig(PriceConfig priceConfig);

    /**
     * 修改价格 平台设置码包价格
     * 
     * @param priceConfig 价格 平台设置码包价格
     * @return 结果
     */
    public int updatePriceConfig(PriceConfig priceConfig);

    /**
     * 批量删除价格 平台设置码包价格
     * 
     * @param ids 需要删除的价格 平台设置码包价格ID
     * @return 结果
     */
    public int deletePriceConfigByIds(Long[] ids);

    /**
     * 删除价格 平台设置码包价格信息
     * 
     * @param id 价格 平台设置码包价格ID
     * @return 结果
     */
    public int deletePriceConfigById(Long id);
}
