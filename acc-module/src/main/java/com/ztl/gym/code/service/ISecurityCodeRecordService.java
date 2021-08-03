package com.ztl.gym.code.service;

import java.util.List;

import com.ztl.gym.code.domain.SecurityCodeRecord;
import com.ztl.gym.code.domain.vo.ScanSecurityCodeOutBean;

/**
 * 防伪记录 company_id字段分Service接口
 * 
 * @author ruoyi
 * @date 2021-08-02
 */
public interface ISecurityCodeRecordService 
{
    /**
     * 查询防伪记录
     * 
     * @param id 防伪记录 ID
     * @return 防伪记录
     */
    public SecurityCodeRecord selectSecurityCodeRecordById(Long id);

    /**
     * 查询防伪记录 company_id字段分列表
     * 
     * @param securityCodeRecord 防伪记录
     * @return 防伪记录集合
     */
    public List<SecurityCodeRecord> selectSecurityCodeRecordList(SecurityCodeRecord securityCodeRecord);

    /**
     * 新增防伪记录 company_id字段分
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
     * 批量删除防伪记录
     * 
     * @param ids 需要删除的防伪记录
     * @return 结果
     */
    public int deleteSecurityCodeRecordByIds(Long[] ids);

    /**
     * 删除防伪记录
     * 
     * @param id 防伪记录
     * @return 结果
     */
    public int deleteSecurityCodeRecordById(Long id);

    /**
     * 扫描防伪码操作
     * @param securityCodeRecord 防伪记录
     * @return 响应
     */
    public ScanSecurityCodeOutBean getSecurityCodeInfo(SecurityCodeRecord securityCodeRecord);

    /**
     * 扫描标识码获取防伪校验
     * @param securityCodeRecord 防伪记录
     * @return 响应
     */
    public ScanSecurityCodeOutBean getSecurityCodeInfoByCode(SecurityCodeRecord securityCodeRecord);
}
