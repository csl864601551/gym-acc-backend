package com.ztl.gym.payment.service.impl;

import java.util.Date;
import java.util.List;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.payment.domain.PriceConfig;
import com.ztl.gym.payment.mapper.PriceConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.payment.service.IPriceConfigService;

/**
 * 价格 平台设置码包价格Service业务层处理
 *
 * @author wujinhao
 * @date 2021-07-19
 */
@Service
public class PriceConfigServiceImpl implements IPriceConfigService
{
    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(PriceConfigServiceImpl.class);
    @Autowired
    private PriceConfigMapper priceConfigMapper;

    /**
     * 查询价格 平台设置码包价格
     *
     * @param id 价格 平台设置码包价格ID
     * @return 价格 平台设置码包价格
     */
    @Override
    public PriceConfig selectPriceConfigById(Long id)
    {
        logger.info("the method selectPriceConfigById enter");
        return priceConfigMapper.selectPriceConfigById(id);
    }

    /**
     * 查询价格 平台设置码包价格列表
     *
     * @param priceConfig 价格 平台设置码包价格
     * @return 价格 平台设置码包价格
     */
    @Override
    public List<PriceConfig> selectPriceConfigList(PriceConfig priceConfig)
    {
        logger.info("the method selectPriceConfigList enter");
        return priceConfigMapper.selectPriceConfigList(priceConfig);
    }

    /**
     * 新增价格 平台设置码包价格
     *
     * @param priceConfig 价格 平台设置码包价格
     * @return 结果
     */
    @Override
    public int insertPriceConfig(PriceConfig priceConfig) {
        logger.info("the method insertPriceConfig enter");
        long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        Date date = DateUtils.getNowDate();
        priceConfig.setCreateUser(userId);
        priceConfig.setCreateTime(date);
        priceConfig.setUpdateTime(date);
        priceConfig.setUpdateUser(userId);
        return priceConfigMapper.insertPriceConfig(priceConfig);
    }

    /**
     * 修改价格 平台设置码包价格
     *
     * @param priceConfig 价格 平台设置码包价格
     * @return 结果
     */
    @Override
    public int updatePriceConfig(PriceConfig priceConfig) {
        logger.info("the method updatePriceConfig enter");
        long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        priceConfig.setUpdateTime(DateUtils.getNowDate());
        priceConfig.setUpdateUser(userId);
        return priceConfigMapper.updatePriceConfig(priceConfig);
    }

    /**
     * 批量删除价格 平台设置码包价格
     *
     * @param ids 需要删除的价格 平台设置码包价格ID
     * @return 结果
     */
    @Override
    public int deletePriceConfigByIds(Long[] ids)
    {
        logger.info("the method deletePriceConfigByIds enter");
        return priceConfigMapper.deletePriceConfigByIds(ids);
    }

    /**
     * 删除价格 平台设置码包价格信息
     *
     * @param id 价格 平台设置码包价格ID
     * @return 结果
     */
    @Override
    public int deletePriceConfigById(Long id)
    {
        logger.info("the method deletePriceConfigById enter");
        return priceConfigMapper.deletePriceConfigById(id);
    }
}
