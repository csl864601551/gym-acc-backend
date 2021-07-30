package com.ztl.gym.quota.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 配额 对象 t_quota
 *
 * @author wujinhao
 * @date 2021-07-19
 */
public class Quota extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 参数名称 */
    @Excel(name = "参数名称")
    private String paramName;

    /** 参数键名 */
    @Excel(name = "参数键名")
    private String paramKey;

    /** 参数值 */
    @Excel(name = "参数值")
    private BigDecimal paramValue;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long companyId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }

    public String getParamName()
    {
        return paramName;
    }
    public void setParamKey(String paramKey)
    {
        this.paramKey = paramKey;
    }

    public String getParamKey()
    {
        return paramKey;
    }
    public void setParamValue(BigDecimal paramValue)
    {
        this.paramValue = paramValue;
    }

    public BigDecimal getParamValue()
    {
        return paramValue;
    }
    public void setCompanyId(Long companyId)
    {
        this.companyId = companyId;
    }

    public Long getCompanyId()
    {
        return companyId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("paramName", getParamName())
            .append("paramKey", getParamKey())
            .append("paramValue", getParamValue())
            .append("companyId", getCompanyId())
            .append("remark", getRemark())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
