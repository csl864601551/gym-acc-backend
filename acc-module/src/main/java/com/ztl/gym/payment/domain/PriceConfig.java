package com.ztl.gym.payment.domain;

import java.math.BigDecimal;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 价格 平台设置码包价格对象 t_price_config
 *
 * @author wujinhao
 * @date 2021-07-19
 */
public class PriceConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 数量 码量 */
    @Excel(name = "数量 码量")
    private Long count;

    /** 类型 1-码 */
    @Excel(name = "类型 1-码")
    private Long type;

    /** 是否启用 0-关闭1-开启 */
    @Excel(name = "是否启用 0-关闭1-开启")
    private String isOpen;

    /**
     * 用户名称
     */
    private String userName;

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
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
    public void setCount(Long count)
    {
        this.count = count;
    }

    public Long getCount()
    {
        return count;
    }
    public void setType(Long type)
    {
        this.type = type;
    }

    public Long getType()
    {
        return type;
    }
    public void setIsOpen(String isOpen)
    {
        this.isOpen = isOpen;
    }

    public String getIsOpen()
    {
        return isOpen;
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
                .append("name", getName())
                .append("price", getPrice())
                .append("count", getCount())
                .append("type", getType())
                .append("isOpen", getIsOpen())
                .append("remark", getRemark())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .append("userName", getUserName())
                .toString();
    }
}
