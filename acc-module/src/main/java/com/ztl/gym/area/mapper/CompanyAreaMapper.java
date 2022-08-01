package com.ztl.gym.area.mapper;

import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.area.domain.vo.CompanyAreaVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 经销商销售区域 Mapper接口
 *
 * @author lujian
 * @date 2021-04-17
 */
@Repository
public interface CompanyAreaMapper {
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

    public List<CompanyArea> selectCompanyAreaListV2(CompanyArea companyArea);


    /**
     * 查询经销商可选区域列表列表
     *
     * @param companyId 部门id
     * @return 经销商销售区域 集合
     */
    public List<CompanyArea> selectkyjxsdqList(Long companyId);

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
     * 删除经销商销售区域
     *
     * @param id 经销商销售区域 ID
     * @return 结果
     */
    public int deleteCompanyAreaById(Long id);

    public int deleteCompanyAreaByTenantId(Long tenantId);

    /**
     * 批量删除经销商销售区域
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCompanyAreaByIds(Long[] ids);

    /**
     * 批量导出经销商销售区域
     *
     * @param companyArea
     * @return
     */
    List<CompanyAreaVo> selectCompanyAreaExport(CompanyArea companyArea);
}
