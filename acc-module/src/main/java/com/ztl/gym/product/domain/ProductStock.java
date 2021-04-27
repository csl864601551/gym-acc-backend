package com.ztl.gym.product.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 库存统计 对象 t_product_stock
 *
 * @author ruoyi
 * @date 2021-04-25
 */
public class ProductStock extends BaseEntity {
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
     * 经销商id
     */
    private Long tenantId;

    /**
     * 产品库存类型 【见AccConstants常量】
     */
    private int stockLevel;

    /**
     * 仓库id
     */
    @Excel(name = "仓库id")
    private Long storageId;

    /**
     * 产品id
     */
    @Excel(name = "产品id")
    private Long productId;

    /**
     * 入库数量
     */
    @Excel(name = "入库数量")
    private int inNum;

    /**
     * 出库数量
     */
    @Excel(name = "出库数量")
    private int outNum;

    /**
     * 退货数量
     */
    @Excel(name = "退货数量")
    private int backNum;

    /**
     * 零售数量
     */
    @Excel(name = "零售数量")
    private int saleNum;

    /**
     * 剩余数量
     */
    @Excel(name = "剩余数量")
    private int remainNum;

    /*---------------------------- 冗余字段 -----------------------------*/
    /**
     * 产品编号
     */
    private String productNo;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 仓库编号
     */
    private String storageNo;
    /**
     * 仓库名称
     */
    private String storageName;

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

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getInNum() {
        return inNum;
    }

    public void setInNum(int inNum) {
        this.inNum = inNum;
    }

    public int getOutNum() {
        return outNum;
    }

    public void setOutNum(int outNum) {
        this.outNum = outNum;
    }

    public int getBackNum() {
        return backNum;
    }

    public void setBackNum(int backNum) {
        this.backNum = backNum;
    }

    public int getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(int saleNum) {
        this.saleNum = saleNum;
    }

    public int getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(int remainNum) {
        this.remainNum = remainNum;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStorageNo() {
        return storageNo;
    }

    public void setStorageNo(String storageNo) {
        this.storageNo = storageNo;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("storageId", getStorageId())
                .append("productId", getProductId())
                .append("inNum", getInNum())
                .append("outNum", getOutNum())
                .append("backNum", getBackNum())
                .append("saleNum", getSaleNum())
                .append("remainNum", getRemainNum())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
