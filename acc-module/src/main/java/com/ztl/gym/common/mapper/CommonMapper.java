package com.ztl.gym.common.mapper;

import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.domain.GeneratorBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommonMapper {
    /**
     * 根据企业查询当前最大自增数
     *
     * @param params
     * @return
     */
    long selectCurrentVal(Map<String, Object> params);

    /**
     * 根据企业查询下一自增数
     *
     * @param params
     * @return
     */
    long selectNextVal(Map<String, Object> params);

    /**
     * 查询该企业是否存在自增数据
     *
     * @param params
     * @return
     */
    int selectIsExist(Map<String, Object> params);

    /**
     * 为自增数插入新企业
     *
     * @param params
     */
    int insertCompany(Map<String, Object> params);

    /**
     * 更新企业自增数
     *
     * @param params
     */
    int updateVal(Map<String, Object> params);

    /**
     * 查询下级经销商用户列表 【自营】
     */
    List<SysUser> getTenantByParent(Map<String, Object> params);

    void insertPrintData(Map<String, Object> mapTemp);

    /**
     * 更新分布式id自增数
     *
     * @param params
     */
    int updateGeneratorVal(Map<String, Object> params);

    /**
     * 新增分布式id
     *
     * @param params
     */
    int insertGenerator(Map<String, Object> params);

    /**
     * 查询分布式id
     *
     * @param params
     */
    GeneratorBean selectIdGenerator(Map<String, Object> params);
}
