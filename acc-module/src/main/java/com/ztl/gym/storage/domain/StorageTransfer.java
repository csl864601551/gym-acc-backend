package com.ztl.gym.storage.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 调货对象 t_storage_transfer
 *
 * @author ruoyi
 * @date 2021-04-22
 */
public class StorageTransfer extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 状态-待发货
     */
    public final static int STATUS_WAIT = 0;
    /**
     * 状态-发货中
     */
    public final static int STATUS_DEALING = 1;
    /**
     * 状态-发货完成
     */
    public final static int STATUS_FINISH = 2;
    /**
     * 状态-撤销
     */
    public final static int STATUS_CANCEL = 8;
    /**
     * 状态-已删除
     */
    public final static int STATUS_DELETE = 9;


    /**
     * 是否启用-否
     */
    public final static int ENABLE_NO = 0;
    /**
     * 是否启用-是
     */
    public final static int ENABLE_YES = 1;

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
    @Excel(name = "经销商id")
    private Long tenantId;

    /**
     * 产品库存id
     */
    private Long stockId;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Integer status;

    /**
     * 是否启用
     */
    private Integer enable;

    /**
     * 调拨单号
     */
    @Excel(name = "调拨单号")
    private String transferNo;

    /**
     * 产品id
     */
    @Excel(name = "产品id")
    private Long productId;

    /**
     * 产品批次
     */
    @Excel(name = "产品批次")
    private String batchNo;

    /**
     * 调拨数量
     */
    @Excel(name = "调拨数量")
    private Long transferNum;

    /**
     * 实际调拨数量
     */
    @Excel(name = "实际调拨数量")
    private Long actTransferNum;

    /**
     * 调出单位
     */
    @Excel(name = "调出单位")
    private Long storageFrom;

    /**
     * 调入单位
     */
    @Excel(name = "调入单位")
    private Long storageTo;

    /**
     * 出货仓库
     */
    @Excel(name = "出货仓库")
    private Long fromStorageId;

    /**
     * 收货仓库
     */
    @Excel(name = "收货仓库")
    private Long toStorageId;

    /**
     * 出库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date outTime;


    /*------------------------ 冗余字段 ------------------------*/
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 调出单位
     */
    private String storageFromName;
    /**
     * 调入单位
     */
    private String storageToName;

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

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setTransferNo(String transferNo) {
        this.transferNo = transferNo;
    }

    public String getTransferNo() {
        return transferNo;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setTransferNum(Long transferNum) {
        this.transferNum = transferNum;
    }

    public Long getTransferNum() {
        return transferNum;
    }

    public void setActTransferNum(Long actTransferNum) {
        this.actTransferNum = actTransferNum;
    }

    public Long getActTransferNum() {
        return actTransferNum;
    }

    public Long getStorageFrom() {
        return storageFrom;
    }

    public void setStorageFrom(Long storageFrom) {
        this.storageFrom = storageFrom;
    }

    public Long getStorageTo() {
        return storageTo;
    }

    public void setStorageTo(Long storageTo) {
        this.storageTo = storageTo;
    }

    public void setFromStorageId(Long fromStorageId) {
        this.fromStorageId = fromStorageId;
    }

    public Long getFromStorageId() {
        return fromStorageId;
    }

    public void setToStorageId(Long toStorageId) {
        this.toStorageId = toStorageId;
    }

    public Long getToStorageId() {
        return toStorageId;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStorageFromName() {
        return storageFromName;
    }

    public void setStorageFromName(String storageFromName) {
        this.storageFromName = storageFromName;
    }

    public String getStorageToName() {
        return storageToName;
    }

    public void setStorageToName(String storageToName) {
        this.storageToName = storageToName;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("tenantId", getTenantId())
                .append("status", getStatus())
                .append("transferNo", getTransferNo())
                .append("productId", getProductId())
                .append("batchNo", getBatchNo())
                .append("transferNum", getTransferNum())
                .append("actTransferNum", getActTransferNum())
                .append("storageFrom", getStorageFrom())
                .append("storageTo", getStorageTo())
                .append("fromStorageId", getFromStorageId())
                .append("toStorageId", getToStorageId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("outTime", getOutTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
