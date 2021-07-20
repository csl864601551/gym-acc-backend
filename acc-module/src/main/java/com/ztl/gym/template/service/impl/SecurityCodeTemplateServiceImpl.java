package com.ztl.gym.template.service.impl;

import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.template.domain.SecurityCodeTemplate;
import com.ztl.gym.template.mapper.SecurityCodeTemplateMapper;
import com.ztl.gym.template.service.ISecurityCodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * templateService业务层处理
 *
 * @author ruoyi
 * @date 2021-07-19
 */
@Service
public class SecurityCodeTemplateServiceImpl implements ISecurityCodeTemplateService
{
    @Autowired
    private SecurityCodeTemplateMapper securityCodeTemplateMapper;

    /**
     * 查询template
     *
     * @param id templateID
     * @return template
     */
    @Override
    public SecurityCodeTemplate selectSecurityCodeTemplateById(Long id)
    {
        return securityCodeTemplateMapper.selectSecurityCodeTemplateById(id);
    }

    /**
     * 查询template列表
     *
     * @param securityCodeTemplate template
     * @return template
     */
    @Override
    public List<SecurityCodeTemplate> selectSecurityCodeTemplateList(SecurityCodeTemplate securityCodeTemplate)
    {
        return securityCodeTemplateMapper.selectSecurityCodeTemplateList(securityCodeTemplate);
    }

    /**
     * 新增template
     *
     * @param securityCodeTemplate template
     * @return 结果
     */
    @Override
    public int insertSecurityCodeTemplate(SecurityCodeTemplate securityCodeTemplate)
    {
        securityCodeTemplate.setCreateTime(DateUtils.getNowDate());
        return securityCodeTemplateMapper.insertSecurityCodeTemplate(securityCodeTemplate);
    }

    /**
     * 修改template
     *
     * @param securityCodeTemplate template
     * @return 结果
     */
    @Override
    public int updateSecurityCodeTemplate(SecurityCodeTemplate securityCodeTemplate)
    {
        securityCodeTemplate.setUpdateTime(DateUtils.getNowDate());
        return securityCodeTemplateMapper.updateSecurityCodeTemplate(securityCodeTemplate);
    }

    /**
     * 批量删除template
     *
     * @param ids 需要删除的templateID
     * @return 结果
     */
    @Override
    public int deleteSecurityCodeTemplateByIds(Long[] ids)
    {
        return securityCodeTemplateMapper.deleteSecurityCodeTemplateByIds(ids);
    }

    /**
     * 删除template信息
     *
     * @param id templateID
     * @return 结果
     */
    @Override
    public int deleteSecurityCodeTemplateById(Long id)
    {
        return securityCodeTemplateMapper.deleteSecurityCodeTemplateById(id);
    }
}
