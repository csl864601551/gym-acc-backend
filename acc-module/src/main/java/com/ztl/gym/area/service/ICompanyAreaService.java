package com.ztl.gym.area.service;

import java.util.List;
import com.ztl.gym.area.domain.CompanyArea;

/**
 * 经销商销售区域 Service接口
 * 
 * @author lujian
 * @date 2021-04-17
 */
public interface ICompanyAreaService 
{
    /**
     * 查询经销商销售区域 
     * 
     * @param id 经销商销售区域 ID
     * @return 经销商销售区域 
     */
    public CompanyArea selectCompanyAreaById(Long id);

    /**
     * 查询经销商销售区域 列表
     * 
     * @param companyArea 经销商销售区域 
     * @return 经销商销售区域 集合
     */
    public List<CompanyArea> selectCompanyAreaList(CompanyArea companyArea);

    /**
     * 新增经销商销售区域 
     * 
     * @param companyArea 经销商销售区域 
     * @return 结果
     */
    public int insertCompanyArea(CompanyArea companyArea);

    /**
     * 修改经销商销售区域 
     * 
     * @param companyArea 经销商销售区域 
     * @return 结果
     */
    public int updateCompanyArea(CompanyArea companyArea);

    /**
     * 批量删除经销商销售区域 
     * 
     * @param ids 需要删除的经销商销售区域 ID
     * @return 结果
     */
    public int deleteCompanyAreaByIds(Long[] ids);

    /**
     * 删除经销商销售区域 信息
     * 
     * @param id 经销商销售区域 ID
     * @return 结果
     */
    public int deleteCompanyAreaById(Long id);
}