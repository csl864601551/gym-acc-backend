package com.ztl.gym.common.mapper;

import java.util.List;
import com.ztl.gym.common.domain.AndroidVersion;

/**
 * 安卓版本信息Mapper接口
 * 
 * @author ruoyi
 * @date 2021-07-06
 */
public interface AndroidVersionMapper 
{
    /**
     * 查询安卓版本信息
     * 
     * @param id 安卓版本信息ID
     * @return 安卓版本信息
     */
    public AndroidVersion selectAndroidVersionById(Long id);

    /**
     * 查询安卓版本信息列表
     * 
     * @param androidVersion 安卓版本信息
     * @return 安卓版本信息集合
     */
    public List<AndroidVersion> selectAndroidVersionList(AndroidVersion androidVersion);

    /**
     * 新增安卓版本信息
     * 
     * @param androidVersion 安卓版本信息
     * @return 结果
     */
    public int insertAndroidVersion(AndroidVersion androidVersion);

    /**
     * 修改安卓版本信息
     * 
     * @param androidVersion 安卓版本信息
     * @return 结果
     */
    public int updateAndroidVersion(AndroidVersion androidVersion);

    /**
     * 删除安卓版本信息
     * 
     * @param id 安卓版本信息ID
     * @return 结果
     */
    public int deleteAndroidVersionById(Long id);

    /**
     * 批量删除安卓版本信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAndroidVersionByIds(Long[] ids);
}
