package com.ztl.gym.code.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 生码记录对象 t_code_acc_record
 *
 * @author ruoyi
 * @date 2021-07-22
 */
public class CodeAccRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long companyId;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 生码类型 */
    @Excel(name = "生码类型")
    private Integer type;

    /** 码数量 */
    @Excel(name = "码数量")
    private Long count;

    /** 首次查询模板 */
    @Excel(name = "首次查询模板")
    private Long onceTemplateId;

    /** 多次查询模板 */
    @Excel(name = "多次查询模板")
    private Long moreTemplateId;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createUser;

    /** 更新人 */
    @Excel(name = "更新人")
    private Long updateUser;

    /** 创建人名称 */
    @Excel(name = "创建人名称", sort = 9)
    private String creatorName;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCompanyId(Long companyId)
    {
        this.companyId = companyId;
    }

    public Long getCompanyId()
    {
        return companyId;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }
    public void setCount(Long count)
    {
        this.count = count;
    }

    public Long getCount()
    {
        return count;
    }
    public void setOnceTemplateId(Long onceTemplateId)
    {
        this.onceTemplateId = onceTemplateId;
    }

    public Long getOnceTemplateId()
    {
        return onceTemplateId;
    }
    public void setMoreTemplateId(Long moreTemplateId)
    {
        this.moreTemplateId = moreTemplateId;
    }

    public Long getMoreTemplateId()
    {
        return moreTemplateId;
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
            .append("companyId", getCompanyId())
            .append("status", getStatus())
            .append("type", getType())
            .append("count", getCount())
            .append("onceTemplateId", getOnceTemplateId())
            .append("moreTemplateId", getMoreTemplateId())
            .append("remark", getRemark())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
