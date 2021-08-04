package com.ztl.gym.template.mapper;

import com.ztl.gym.template.domain.SecurityCodeTemplate;

import java.util.List;

/**
 * templateMapper接口
 * 
 * @author ruoyi
 * @date 2021-07-19
 */
public interface SecurityCodeTemplateMapper 
{
    /**
     * 查询template
     * 
     * @param id templateID
     * @return template
     */
    public SecurityCodeTemplate selectSecurityCodeTemplateById(Long id);

    /**
     * 查询template列表
     * 
     * @param securityCodeTemplate template
     * @return template集合
     */
    public List<SecurityCodeTemplate> selectSecurityCodeTemplateList(SecurityCodeTemplate securityCodeTemplate);


    /**
     * 查询template列表的总条数
     *
     * @param securityCodeTemplate template
     * @return template集合
     */
    public int selectSecurityCodeTemplateListCount(SecurityCodeTemplate securityCodeTemplate);

    /**
     * 新增template
     * 
     * @param securityCodeTemplate template
     * @return 结果
     */
    public int insertSecurityCodeTemplate(SecurityCodeTemplate securityCodeTemplate);

    /**
     * 修改template
     * 
     * @param securityCodeTemplate template
     * @return 结果
     */
    public int updateSecurityCodeTemplate(SecurityCodeTemplate securityCodeTemplate);

    /**
     * 删除template
     * 
     * @param id templateID
     * @return 结果
     */
    public int deleteSecurityCodeTemplateById(Long id);

    /**
     * 批量删除template
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSecurityCodeTemplateByIds(Long[] ids);
}
