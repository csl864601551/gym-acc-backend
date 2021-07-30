package com.ztl.gym.template.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * template对象 t_security_code_template
 *
 * @author ruoyi
 * @date 2021-07-19
 */
public class SecurityCodeTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 模板名称 */
    @Excel(name = "模板名称")
    private String name;

    /** 防伪模板内容 */
    @Excel(name = "防伪模板内容")
    private String content;

    /** 类型 */
    @Excel(name = "类型")
    private Integer type;

    /** 企业id */
    @Excel(name = "企业id")
    private Long companyId;

    /** 显示场景 */
    @Excel(name = "显示场景")
    private String scenario;

    /** 是否启用 */
    @Excel(name = "是否启用")
    private String isOpen;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createUser;

    /** 更新人 */
    @Excel(name = "更新人")
    private Long updateUser;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }
    public void setCompanyId(Long companyId)
    {
        this.companyId = companyId;
    }

    public Long getCompanyId()
    {
        return companyId;
    }
    public void setScenario(String scenario)
    {
        this.scenario = scenario;
    }

    public String getScenario()
    {
        return scenario;
    }
    public void setIsOpen(String isOpen)
    {
        this.isOpen = isOpen;
    }

    public String getIsOpen()
    {
        return isOpen;
    }
    public void setCreateUser(Long createUser)
    {
        this.createUser = createUser;
    }

    public Long getCreateUser()
    {
        return createUser;
    }
    public void setUpdateUser(Long updateUser)
    {
        this.updateUser = updateUser;
    }

    public Long getUpdateUser()
    {
        return updateUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("content", getContent())
            .append("type", getType())
            .append("companyId", getCompanyId())
            .append("scenario", getScenario())
            .append("isOpen", getIsOpen())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
