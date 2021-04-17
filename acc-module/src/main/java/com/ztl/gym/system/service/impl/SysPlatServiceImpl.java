package com.ztl.gym.system.service.impl;

import java.util.List;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.system.mapper.SysPlatMapper;
import com.ztl.gym.system.domain.SysPlat;
import com.ztl.gym.system.service.ISysPlatService;

/**
 * 平台Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-17
 */
@Service
public class SysPlatServiceImpl implements ISysPlatService
{
    @Autowired
    private SysPlatMapper sysPlatMapper;

    /**
     * 查询平台
     *
     * @param id 平台ID
     * @return 平台
     */
    @Override
    public SysPlat selectSysPlatById(Long id)
    {

        return sysPlatMapper.selectSysPlatById(id);
    }

    /**
     * 查询平台列表
     *
     * @param sysPlat 平台
     * @return 平台
     */
    @Override
    public List<SysPlat> selectSysPlatList(SysPlat sysPlat)
    {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            sysPlat.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        return sysPlatMapper.selectSysPlatList(sysPlat);
    }

    /**
     * 新增平台
     *
     * @param sysPlat 平台
     * @return 结果
     */
    @Override
    public int insertSysPlat(SysPlat sysPlat)
    {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            sysPlat.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }else{
            sysPlat.setCompanyId(Long.valueOf(100));
        }
        sysPlat.setCreateTime(DateUtils.getNowDate());
        sysPlat.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        sysPlat.setCreateTime(DateUtils.getNowDate());
        return sysPlatMapper.insertSysPlat(sysPlat);
    }

    /**
     * 修改平台
     *
     * @param sysPlat 平台
     * @return 结果
     */
    @Override
    public int updateSysPlat(SysPlat sysPlat)
    {
        sysPlat.setUpdateTime(DateUtils.getNowDate());
        return sysPlatMapper.updateSysPlat(sysPlat);
    }

    /**
     * 批量删除平台
     *
     * @param ids 需要删除的平台ID
     * @return 结果
     */
    @Override
    public int deleteSysPlatByIds(Long[] ids)
    {
        return sysPlatMapper.deleteSysPlatByIds(ids);
    }

    /**
     * 删除平台信息
     *
     * @param id 平台ID
     * @return 结果
     */
    @Override
    public int deleteSysPlatById(Long id)
    {
        return sysPlatMapper.deleteSysPlatById(id);
    }
}
