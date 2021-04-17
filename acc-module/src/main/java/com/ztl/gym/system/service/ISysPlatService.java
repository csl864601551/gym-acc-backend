package com.ztl.gym.system.service;

import java.util.List;
import com.ztl.gym.system.domain.SysPlat;

/**
 * 平台Service接口
 * 
 * @author ruoyi
 * @date 2021-04-17
 */
public interface ISysPlatService 
{
    /**
     * 查询平台
     * 
     * @param id 平台ID
     * @return 平台
     */
    public SysPlat selectSysPlatById(Long id);

    /**
     * 查询平台列表
     * 
     * @param sysPlat 平台
     * @return 平台集合
     */
    public List<SysPlat> selectSysPlatList(SysPlat sysPlat);

    /**
     * 新增平台
     * 
     * @param sysPlat 平台
     * @return 结果
     */
    public int insertSysPlat(SysPlat sysPlat);

    /**
     * 修改平台
     * 
     * @param sysPlat 平台
     * @return 结果
     */
    public int updateSysPlat(SysPlat sysPlat);

    /**
     * 批量删除平台
     * 
     * @param ids 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysPlatByIds(Long[] ids);

    /**
     * 删除平台信息
     * 
     * @param id 平台ID
     * @return 结果
     */
    public int deleteSysPlatById(Long id);
}
