package com.ztl.gym.area.service;

import java.util.List;
import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.area.domain.vo.CompanyAreaVo;

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
     * 查询经销商可选区域
     *
     * @param companyId 部门id
     * @return 经销商销售区域 集合
     */
    public List<CompanyArea> selectkyjxsdqList(Long companyId);


    public List<CompanyArea> selectCompanyAreaListV2(CompanyArea companyArea);

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

    public int deleteCompanyAreaByTenantId(Long tenantId);

    /**
     * 导出经销商销售区域 信息
     * @param companyArea
     * @return
     */
    List<CompanyAreaVo> selectCompanyAreaExport(CompanyArea companyArea);
}
