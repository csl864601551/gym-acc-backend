package com.ztl.gym.payment.domain;

import java.math.BigDecimal;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 充值记录 对象 t_payment_record
 *
 * @author wujinhao
 * @date 2021-07-19
 */
@Valid
public class PaymentRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单号
     */
    @Excel(name = "订单号")
    private String orderNo;

    /**
     * 支付金额
     */
    @Excel(name = "充值金额")
    @NotNull(message = "充值金额不能为空")
    @DecimalMin(value = "0.00", message = "充值金额不能小于0.00元")
    @Digits(integer = 32, fraction=2, message = "充值金额格式不正确")
    private BigDecimal payAmount;

    /**
     * 币种 默认0-人民币
     */
    @Excel(name = "币种 默认0-人民币")
    private Long currency;

    /**
     * 支付状态 0:待支付、1:已支付、2:取消
     */
    @Excel(name = "支付状态 0:待支付、1:已支付、2:取消")
    private Integer payStatus;

    /**
     * 支付方式 0:线下、1:支付宝、2:微信、3:银联、4:其他
     */
    @Excel(name = "支付方式 0:线下、1:支付宝、2:微信、3:银联、4:其他")
    private Integer payType;

    /**
     * 企业ID
     */
    @Excel(name = "企业ID")
    @NotNull()
    private Long companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 用户名称
     */
    private String userName;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

    public Long getCurrency() {
        return currency;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
                .append("orderNo", getOrderNo())
                .append("payAmount", getPayAmount())
                .append("currency", getCurrency())
                .append("payStatus", getPayStatus())
                .append("payType", getPayType())
                .append("companyId", getCompanyId())
                .append("companyName", getCompanyName())
                .append("userName", getUserName())
                .append("remark", getRemark())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
