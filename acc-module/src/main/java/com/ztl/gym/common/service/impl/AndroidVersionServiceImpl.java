package com.ztl.gym.common.service.impl;

import java.util.List;
import com.ztl.gym.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.common.mapper.AndroidVersionMapper;
import com.ztl.gym.common.domain.AndroidVersion;
import com.ztl.gym.common.service.IAndroidVersionService;

/**
 * 安卓版本信息Service业务层处理
 *
 * @author ruoyi
 * @date 2021-07-06
 */
@Service
public class AndroidVersionServiceImpl implements IAndroidVersionService
{
    @Autowired
    private AndroidVersionMapper androidVersionMapper;

    /**
     * 查询安卓版本信息
     *
     * @param id 安卓版本信息ID
     * @return 安卓版本信息
     */
    @Override
    public AndroidVersion selectAndroidVersionById(Long id)
    {
        return androidVersionMapper.selectAndroidVersionById(id);
    }

    /**
     * 查询安卓版本信息列表
     *
     * @param androidVersion 安卓版本信息
     * @return 安卓版本信息
     */
    @Override
    public List<AndroidVersion> selectAndroidVersionList(AndroidVersion androidVersion)
    {
        return androidVersionMapper.selectAndroidVersionList(androidVersion);
    }

    /**
     * 新增安卓版本信息
     *
     * @param androidVersion 安卓版本信息
     * @return 结果
     */
    @Override
    public int insertAndroidVersion(AndroidVersion androidVersion)
    {
        androidVersion.setCreateTime(DateUtils.getNowDate());
        return androidVersionMapper.insertAndroidVersion(androidVersion);
    }

    /**
     * 修改安卓版本信息
     *
     * @param androidVersion 安卓版本信息
     * @return 结果
     */
    @Override
    public int updateAndroidVersion(AndroidVersion androidVersion)
    {
        return androidVersionMapper.updateAndroidVersion(androidVersion);
    }

    /**
     * 批量删除安卓版本信息
     *
     * @param ids 需要删除的安卓版本信息ID
     * @return 结果
     */
    @Override
    public int deleteAndroidVersionByIds(Long[] ids)
    {
        return androidVersionMapper.deleteAndroidVersionByIds(ids);
    }

    /**
     * 删除安卓版本信息信息
     *
     * @param id 安卓版本信息ID
     * @return 结果
     */
    @Override
    public int deleteAndroidVersionById(Long id)
    {
        return androidVersionMapper.deleteAndroidVersionById(id);
    }
}
