package com.ztl.gym.payment.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 消费记录 对象 t_purchase_record
 *
 * @author wujinhao
 * @date 2021-07-19
 */
@Valid
public class PurchaseRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 购买类型 默认0-购码 */
    @Excel(name = "购买类型 默认0-购码")
    private Long type;

    /** 购买数量 */
    @Excel(name = "购买数量")
    @Min(value = 1, message = "购买数量大于0")
    private Long count;

    /**
     * 订单号
     */
    private String orderNo;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long companyId;

    /** 金额 */
    @Excel(name = "消费金额")
    @NotNull(message = "消费金额不能为空")
    @DecimalMin(value = "0.00", message = "消费金额不能小于0.00元")
    @Digits(integer = 32, fraction=2, message = "消费金额格式不正确")
    private BigDecimal purchaseAmount;

    /** 类型 0-消费，1-退款 */
    @Excel(name = "类型 0-消费，1-退款")
    private Long purchaseType;

    private String userName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setType(Long type)
    {
        this.type = type;
    }

    public Long getType()
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
    public void setCompanyId(Long companyId)
    {
        this.companyId = companyId;
    }

    public Long getCompanyId()
    {
        return companyId;
    }
    public void setPurchaseAmount(BigDecimal purchaseAmount)
    {
        this.purchaseAmount = purchaseAmount;
    }

    public BigDecimal getPurchaseAmount()
    {
        return purchaseAmount;
    }
    public void setPurchaseType(Long purchaseType)
    {
        this.purchaseType = purchaseType;
    }

    public Long getPurchaseType()
    {
        return purchaseType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("type", getType())
                .append("count", getCount())
                .append("orderNo", getOrderNo())
                .append("companyId", getCompanyId())
                .append("purchaseAmount", getPurchaseAmount())
                .append("purchaseType", getPurchaseType())
                .append("remark", getRemark())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .append("userName",getUserName())
                .toString();
    }
}
