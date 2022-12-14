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
public class CodeSingle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID", sort = 1)
    private Long companyId;

    /** 状态 */
    private Integer status;

    /** 生码类型 */
    private Integer type;

    /** 箱数量 */
    @Excel(name = "箱数量", sort = 6)
    private Long boxCount;
    /** 每箱码数量 */
    @Excel(name = "每箱码数量", sort = 6)
    private Long singleCount;
    /** 码数量 */
    @Excel(name = "码数量", sort = 6)
    private Long count;

    /** 起始流水号 */
    @Excel(name = "起始流水号", sort = 2)
    private Long indexStart;

    /** 终止流水号 */
    @Excel(name = "终止流水号", sort = 3)
    private Long indexEnd;

    /*---------------------- 冗余字段 ----------------------*/
    /** 创建人名称 */
    @Excel(name = "创建人名称", sort = 9)
    private String creatorName;
    @Excel(name = "状态", sort = 4)
    private String statusName;
    @Excel(name = "生码类型", sort = 5)
    private String typeName;

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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(Long boxCount) {
        this.boxCount = boxCount;
    }

    public Long getSingleCount() {
        return singleCount;
    }

    public void setSingleCount(Long singleCount) {
        this.singleCount = singleCount;
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
            .append("remark", getRemark())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
