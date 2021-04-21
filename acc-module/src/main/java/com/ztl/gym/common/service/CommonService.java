package com.ztl.gym.common.service;

import com.ztl.gym.common.core.domain.entity.SysUser;

import java.util.List;

public interface CommonService {
    /**
     * 根据企业查询当前最新自增数
     *
     * @param companyId
     * @return
     */
    long selectCurrentVal(long companyId);

    /**
     * 根据企业查询下一个自增数
     *
     * @param companyId
     * @return
     */
    long selectNextVal(long companyId);

    /**
     * 更新企业自增数
     *
     * @param companyId
     * @param codeIndex
     */
    int updateVal(long companyId, long codeIndex);

    /**
     * 查询下级经销商用户列表
     */
    List<SysUser> getNextLevelUser();

    /**
     * 查询同级经销商用户列表
     */
    List<SysUser> getSameLevelUser();

    /**
     * 获取用于查询的tenantId
     *
     * @return
     */
    Long getTenantId();

    /**
     * 获取流转记录编号
     *
     * @return
     */
    String getStorageNo(int storageType);
}
