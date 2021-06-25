package com.ztl.gym.common.service;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.common.core.domain.entity.SysUser;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface

CommonService {
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
     * 查询下级经销商用户列表 【自营】
     */
    List<SysUser> getTenantByParent(Map<String, Object> params);

    /**
     * 查询上级部门id
     *
     * @return
     */
    Long getParentDeptId(long deptId);

    /**
     * 获取用于查询的企业/经销商id
     *
     * @return
     */
    Long getTenantId();

    /**
     * 生成流转记录编号
     *
     * @param storageType 流转类型 【见AccConstants】
     * @return
     */
    String getStorageNo(int storageType);

    /**
     * 查询码当前操作是否合法 【输入流转操作输入货码时调用】
     *
     * @param companyId   企业id
     * @param storageType 当前流转操作类型 【见AccConstants】
     * @param queryValue  查询值
     * @return
     */
    boolean judgeStorageIsIllegalByValue(long companyId, Integer storageType, String queryValue);

    /**
     * 根据流转信息查询码集合
     *
     * @param companyId
     * @param storageType
     * @param storageRecordId
     * @return
     */
    List<Code> selectCodeByStorage(long companyId, int storageType, long storageRecordId);

    /**
     * 根据流转信息查询码集合,返回查询条件 【分页查询】
     *
     * @param companyId
     * @param storageType
     * @param storageRecordId
     * @return
     */
    List<Code> selectCodeByStorageForPage(long companyId, int storageType, long storageRecordId);

    void downloadTXT(String fileName, String content, HttpServletResponse response);
}
