package com.ztl.gym.code.mapper;


import com.ztl.gym.code.domain.SecurityCodeRecord;

import java.util.List;
import java.util.Map;

/**
 * 防伪记录 company_id字段分Mapper接口
 *
 * @author ruoyi
 * @date 2021-08-02
 */
public interface SecurityCodeRecordMapper
{
    /**
     * 查询防伪记录
     *
     * @param id 防伪记录
     * @return 防伪记录
     */
    public SecurityCodeRecord selectSecurityCodeRecordById(Long id);

    /**
     * 查询防伪记录列表
     *
     * @param securityCodeRecord 防伪记录
     * @return 防伪记录集合
     */
    public List<SecurityCodeRecord> selectSecurityCodeRecordList(SecurityCodeRecord securityCodeRecord);

    /**
     * 新增防伪记录
     *
     * @param securityCodeRecord 防伪记录
     * @return 结果
     */
    public int insertSecurityCodeRecord(SecurityCodeRecord securityCodeRecord);

    /**
     * 修改防伪记录
     *
     * @param securityCodeRecord 防伪记录
     * @return 结果
     */
    public int updateSecurityCodeRecord(SecurityCodeRecord securityCodeRecord);

    /**
     * 删除防伪记录
     *
     * @param id 防伪记录
     * @return 结果
     */
    public int deleteSecurityCodeRecordById(Long id);

    /**
     * 批量删除防伪记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSecurityCodeRecordByIds(Long[] ids);

    /**
     * 通过防伪码查询防伪记录列表
     *
     * @param accCode 防伪码
     * @return 防伪记录集合
     */
    public List<SecurityCodeRecord> selectRecordsByAccCode(String accCode);


    /**
     * 查询扫码地图
     *
     * @return 扫码记录集合
     */
    public List<Map<String,Object>> getSecurityCodeRecordXx(SecurityCodeRecord securityCodeRecord);
}
