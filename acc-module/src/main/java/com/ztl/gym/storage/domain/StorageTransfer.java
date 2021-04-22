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
 * @date 2021-04-09
 */
public class StorageTransfer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long company_id;

    /** 经销商id */
    @Excel(name = "经销商id")
    private Long tenant_id;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 调拨单号 */
    @Excel(name = "调拨单号")
    private String transfer_no;

    /** 产品id */
    @Excel(name = "产品id")
    private Long productId;

    /** 产品批次 */
    @Excel(name = "产品批次")
    private String batchNo;

    /** 调拨数量 */
    @Excel(name = "调拨数量")
    private Long transferNum;

    /** 实际调拨数量 */
    @Excel(name = "实际调拨数量")
    private Long actTransferNum;

    /** 调出单位 */
    @Excel(name = "调出单位")
    private String storageFrom;

    /** 调入单位 */
    @Excel(name = "调入单位")
    private Long storageTo;

    /** 出货仓库 */
    @Excel(name = "出货仓库")
    private Long fromStorageId;

    /** 收货仓库 */
    @Excel(name = "收货仓库")
    private Long toStorageId;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public Long getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(Long tenant_id) {
        this.tenant_id = tenant_id;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getTransfer_no() {
        return transfer_no;
    }

    public void setTransfer_no(String transfer_no) {
        this.transfer_no = transfer_no;
    }

    public Long getStorageTo() {
        return storageTo;
    }

    public void setStorageTo(Long storageTo) {
        this.storageTo = storageTo;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Long getTransferNum() {
        return transferNum;
    }

    public void setTransferNum(Long transferNum) {
        this.transferNum = transferNum;
    }

    public Long getActTransferNum() {
        return actTransferNum;
    }

    public void setActTransferNum(Long actTransferNum) {
        this.actTransferNum = actTransferNum;
    }

    public String getStorageFrom() {
        return storageFrom;
    }

    public void setStorageFrom(String storageFrom) {
        this.storageFrom = storageFrom;
    }

    public Long getFromStorageId() {
        return fromStorageId;
    }

    public void setFromStorageId(Long fromStorageId) {
        this.fromStorageId = fromStorageId;
    }

    public Long getToStorageId() {
        return toStorageId;
    }

    public void setToStorageId(Long toStorageId) {
        this.toStorageId = toStorageId;
    }
}
