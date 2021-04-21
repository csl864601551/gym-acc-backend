package com.ztl.gym.common.service;

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
}
