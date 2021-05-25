package com.ztl.gym.area.service.impl;

import java.util.List;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.area.mapper.CompanyAreaMapper;
import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.area.service.ICompanyAreaService;

/**
 * 经销商销售区域 Service业务层处理
 *
 * @author lujian
 * @date 2021-04-17
 */
@Service
public class CompanyAreaServiceImpl implements ICompanyAreaService
{
    @Autowired
    private CompanyAreaMapper companyAreaMapper;

    /**
     * 查询经销商销售区域 
     *
     * @param id 经销商销售区域 ID
     * @return 经销商销售区域 
     */
    @Override
    public CompanyArea selectCompanyAreaById(Long id)
    {
        return companyAreaMapper.selectCompanyAreaById(id);
    }

    /**
     * 查询经销商销售区域 列表
     *
     * @param companyArea 经销商销售区域 
     * @return 经销商销售区域 
     */
    @Override
    public List<CompanyArea> selectCompanyAreaList(CompanyArea companyArea)
    {
        return companyAreaMapper.selectCompanyAreaList(companyArea);
    }
    @Override
    public List<CompanyArea> selectCompanyAreaListV2(CompanyArea companyArea)
    {
        return companyAreaMapper.selectCompanyAreaListV2(companyArea);
    }

    /**
     * 新增经销商销售区域 
     *
     * @param companyArea 经销商销售区域 
     * @return 结果
     */
    @Override
    public int insertCompanyArea(CompanyArea companyArea)
    {
        return companyAreaMapper.insertCompanyArea(companyArea);
    }

    /**
     * 修改经销商销售区域 
     *
     * @param companyArea 经销商销售区域 
     * @return 结果
     */
    @Override
    public int updateCompanyArea(CompanyArea companyArea)
    {
        companyArea.setUpdateTime(DateUtils.getNowDate());
        return companyAreaMapper.updateCompanyArea(companyArea);
    }

    /**
     * 批量删除经销商销售区域 
     *
     * @param ids 需要删除的经销商销售区域 ID
     * @return 结果
     */
    @Override
    public int deleteCompanyAreaByIds(Long[] ids)
    {
        return companyAreaMapper.deleteCompanyAreaByIds(ids);
    }

    /**
     * 删除经销商销售区域 信息
     *
     * @param id 经销商销售区域 ID
     * @return 结果
     */
    @Override
    public int deleteCompanyAreaById(Long id)
    {
        return companyAreaMapper.deleteCompanyAreaById(id);
    }
}
