package com.ztl.gym.code.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 生码记录对象 t_code_record
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public class CodeRecord extends BaseEntity
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

    /** 起始流水号 */
    @Excel(name = "起始流水号")
    private Long indexStart;

    /** 终止流水号 */
    @Excel(name = "终止流水号")
    private Long indexEnd;

    /** 关联产品id */
    @Excel(name = "关联产品id")
    private Long productId;

    /** 关联批次id */
    @Excel(name = "关联批次id")
    private Long batchId;

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
    public void setIndexStart(Long indexStart)
    {
        this.indexStart = indexStart;
    }

    public Long getIndexStart()
    {
        return indexStart;
    }
    public void setIndexEnd(Long indexEnd)
    {
        this.indexEnd = indexEnd;
    }

    public Long getIndexEnd()
    {
        return indexEnd;
    }
    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getProductId()
    {
        return productId;
    }
    public void setBatchId(Long batchId)
    {
        this.batchId = batchId;
    }

    public Long getBatchId()
    {
        return batchId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("status", getStatus())
            .append("type", getType())
            .append("count", getCount())
            .append("indexStart", getIndexStart())
            .append("indexEnd", getIndexEnd())
            .append("productId", getProductId())
            .append("batchId", getBatchId())
            .append("remark", getRemark())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
