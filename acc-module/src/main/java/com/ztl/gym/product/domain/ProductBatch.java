package com.ztl.gym.product.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;

/**
 * 产品批次对象 t_product_batch
 *
 * @author ruoyi
 * @date 2021-04-14
 */
public class ProductBatch extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long companyId;

    /** 编号 */
    @Excel(name = "编号")
    private String batchNo;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 批次标题 */
    @Excel(name = "批次标题")
    private String batchTitle;

    /** 批次日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "批次日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date batchDate;

    /** 产品id */
    @Excel(name = "产品id")
    private Long productId;

    /*---------------------- 冗余字段 ----------------------*/
    private String creatorName;
    private String productName;
    private String productNo;
    private String barCode;
    private String storageName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getBatchTitle() {
        return batchTitle;
    }

    public void setBatchTitle(String batchTitle) {
        this.batchTitle = batchTitle;
    }

    public Date getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(Date batchDate) {
        this.batchDate = batchDate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    @Override
    public String toString() {
        return "ProductBatch{" +
                "id=" + id +
                ", company_id=" + companyId +
                ", batch_no='" + batchNo + '\'' +
                ", status=" + status +
                ", batch_title='" + batchTitle + '\'' +
                ", batch_date=" + batchDate +
                ", product_id=" + productId +
                '}';
    }
}
