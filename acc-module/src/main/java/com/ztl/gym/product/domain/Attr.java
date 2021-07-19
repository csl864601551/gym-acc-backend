package com.ztl.gym.product.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 规格属性对象 t_attr
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public class Attr extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业id */
    @Excel(name = "企业id")
    private Long companyId;

    /** 编号 */
    @Excel(name = "编号")
    private String attrNo;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 属性中文名 */
    @Excel(name = "属性中文名")
    private String attrNameCn;

    /** 属性英文名 */
    @Excel(name = "属性英文名")
    private String attrNameEn;

    /** 属性值 从下面的列表中选择（一行代表一个可选值）用逗号分隔开 */
    @Excel(name = "属性值 从下面的列表中选择", readConverterExp = "一=行代表一个可选值")
    private String attrValue;

    /** 录入方式 */
    @Excel(name = "录入方式")
    private Long inputType;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    private String choose_name;

    private String input_name;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createUser;

    /** 更新人 */
    @Excel(name = "更新人")
    private Long updateUser;

    /** 创建人名称 */
    @Excel(name = "创建人名称")
    private String createUserName;

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
    public void setAttrNo(String attrNo)
    {
        this.attrNo = attrNo;
    }

    public String getAttrNo()
    {
        return attrNo;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setAttrNameCn(String attrNameCn)
    {
        this.attrNameCn = attrNameCn;
    }

    public String getAttrNameCn()
    {
        return attrNameCn;
    }
    public void setAttrNameEn(String attrNameEn)
    {
        this.attrNameEn = attrNameEn;
    }

    public String getAttrNameEn()
    {
        return attrNameEn;
    }
    public void setAttrValue(String attrValue)
    {
        this.attrValue = attrValue;
    }

    public String getAttrValue()
    {
        return attrValue;
    }
    public void setInputType(Long inputType)
    {
        this.inputType = inputType;
    }

    public Long getInputType()
    {
        return inputType;
    }
    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
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

    public String getChoose_name() {
        return choose_name;
    }

    public void setChoose_name(String choose_name) {
        this.choose_name = choose_name;
    }

    public String getInput_name() {
        return input_name;
    }

    public void setInput_name(String input_name) {
        this.input_name = input_name;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("attrNo", getAttrNo())
            .append("status", getStatus())
            .append("attrNameCn", getAttrNameCn())
            .append("attrNameEn", getAttrNameEn())
            .append("attrValue", getAttrValue())
            .append("inputType", getInputType())
            .append("sort", getSort())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
                .append("createUserName",getCreateUserName())
            .toString();
    }
}
