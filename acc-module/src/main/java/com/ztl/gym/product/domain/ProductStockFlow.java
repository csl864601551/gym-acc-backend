package com.ztl.gym.product.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 产品库存明细对象 t_product_stock_flow
 *
 * @author ruoyi
 * @date 2021-04-25
 */
public class ProductStockFlow extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 企业ID
     */
    @Excel(name = "企业ID")
    private Long companyId;

    /**
     * 产品库存id
     */
    @Excel(name = "产品库存id")
    private Long stockId;

    /**
     * 变动类型(1:新增2:减少)
     */
    @Excel(name = "变动类型(1:新增2:减少)")
    private Integer flowType;

    /**
     * 流转类型
     */
    @Excel(name = "流转类型")
    private Integer storageType;

    /**
     * 流转id
     */
    @Excel(name = "流转id")
    private Long storageRecordId;

    /**
     * 变动数量
     */
    @Excel(name = "变动数量")
    private int flowNum;

    /**
     * 变动前数量
     */
    @Excel(name = "变动前数量")
    private int flowBefore;

    /**
     * 变动后数量
     */
    @Excel(name = "变动后数量")
    private int flowAfter;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private Long createUser;

    private String createUserName;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getStockId() {
        return stockId;
    }

    public Integer getFlowType() {
        return flowType;
    }

    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageRecordId(Long storageRecordId) {
        this.storageRecordId = storageRecordId;
    }

    public Long getStorageRecordId() {
        return storageRecordId;
    }

    public int getFlowNum() {
        return flowNum;
    }

    public void setFlowNum(int flowNum) {
        this.flowNum = flowNum;
    }

    public int getFlowBefore() {
        return flowBefore;
    }

    public void setFlowBefore(int flowBefore) {
        this.flowBefore = flowBefore;
    }

    public int getFlowAfter() {
        return flowAfter;
    }

    public void setFlowAfter(int flowAfter) {
        this.flowAfter = flowAfter;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getCreateUser() {
        return createUser;
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
                .append("stockId", getStockId())
                .append("flowType", getFlowType())
                .append("storageType", getStorageType())
                .append("storageRecordId", getStorageRecordId())
                .append("flowNum", getFlowNum())
                .append("flowBefore", getFlowBefore())
                .append("flowAfter", getFlowAfter())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .toString();
    }
}
