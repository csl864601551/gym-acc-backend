package com.ztl.gym.system.service.impl;

import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.uuid.IdUtils;
import com.ztl.gym.system.domain.TWeixinConfig;
import com.ztl.gym.system.mapper.TWeixinConfigMapper;
import com.ztl.gym.system.service.ITWeixinConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * configService业务层处理
 * 
 * @author ruoyi
 * @date 2021-06-17
 */
@Service
public class TWeixinConfigServiceImpl implements ITWeixinConfigService
{
    @Autowired
    private TWeixinConfigMapper tWeixinConfigMapper;

    /**
     * 查询config
     * 
     * @param id configID
     * @return config
     */
    @Override
    public TWeixinConfig selectTWeixinConfigById(String id)
    {
        return tWeixinConfigMapper.selectTWeixinConfigById(id);
    }

    /**
     * 查询config列表
     * 
     * @param tWeixinConfig config
     * @return config
     */
    @Override
    public List<TWeixinConfig> selectTWeixinConfigList(TWeixinConfig tWeixinConfig)
    {
        return tWeixinConfigMapper.selectTWeixinConfigList(tWeixinConfig);
    }

    /**
     * 新增config
     * 
     * @param tWeixinConfig config
     * @return 结果
     */
    @Override
    public int insertTWeixinConfig(TWeixinConfig tWeixinConfig)
    {
        tWeixinConfig.setId(IdUtils.fastSimpleUUID());
        tWeixinConfig.setCreateTime(DateUtils.getNowDate());
        return tWeixinConfigMapper.insertTWeixinConfig(tWeixinConfig);
    }

    /**
     * 修改config
     * 
     * @param tWeixinConfig config
     * @return 结果
     */
    @Override
    public int updateTWeixinConfig(TWeixinConfig tWeixinConfig)
    {
        tWeixinConfig.setUpdateTime(DateUtils.getNowDate());
        return tWeixinConfigMapper.updateTWeixinConfig(tWeixinConfig);
    }

    /**
     * 批量删除config
     * 
     * @param ids 需要删除的configID
     * @return 结果
     */
    @Override
    public int deleteTWeixinConfigByIds(String[] ids)
    {
        return tWeixinConfigMapper.deleteTWeixinConfigByIds(ids);
    }

    /**
     * 删除config信息
     * 
     * @param id configID
     * @return 结果
     */
    @Override
    public int deleteTWeixinConfigById(String id)
    {
        return tWeixinConfigMapper.deleteTWeixinConfigById(id);
    }
}
