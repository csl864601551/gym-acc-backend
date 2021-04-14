package com.ztl.gym.common.mapper;

import java.util.Map;

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
    void insertCompany(Map<String, Object> params);

    /**
     * 更新企业自增数
     *
     * @param params
     */
    void updateVal(Map<String, Object> params);
}
