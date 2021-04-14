package com.ztl.gym.code.service;

import com.ztl.gym.code.domain.Code;

import java.util.List;

public interface CodeTestService {
    /**
     * 查询码
     *
     * @param index 流水号
     * @return 订单信息
     */
    public Code selectCodeById(long deptId, Long index);

    public Code selectCode(long deptId, String code);

    /**
     * 查询码列表
     *
     * @param companyId 企业id
     * @return 订单列表
     */
    public List<Code> selectCodeListByCompanyId(long companyId);

    /**
     * 新增码
     *
     * @param code 码
     * @return 结果
     */
    public int insertCode(Code code);

    /**
     * 查看该企业是否存在t_code表
     *
     * @return
     */
    boolean checkCompanyTableExist(long companyId, String companyPrefix);

}
