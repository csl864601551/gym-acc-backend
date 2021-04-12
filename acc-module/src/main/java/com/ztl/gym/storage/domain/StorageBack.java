package com.ztl.gym.storage.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 退货对象 t_storage_back
 *
 * @author ruoyi
 * @date 2021-04-09
 */
public class StorageBack extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long companyId;

    /** 经销商id */
    @Excel(name = "经销商id")
    private Long tenantId;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 退货单号 */
    @Excel(name = "退货单号")
    private String backNo;

    /** 产品id */
    @Excel(name = "产品id")
    private Long productId;

    /** 产品批次 */
    @Excel(name = "产品批次")
    private String batchNo;

    /** 退货数量 */
    @Excel(name = "退货数量")
    private Long backNum;

    /** 实际退货数量 */
    @Excel(name = "实际退货数量")
    private Long actBackNum;

    /** 退货单位 */
    @Excel(name = "退货单位")
    private String storageFrom;

    /** 收货单位 */
    @Excel(name = "收货单位")
    private String storageTo;

    /** 退货仓库 */
    @Excel(name = "退货仓库")
    private Long fromStorageId;

    /** 收入仓库 */
    @Excel(name = "收入仓库")
    private Long toStorageId;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createUser;

    /** 修改人 */
    @Excel(name = "修改人")
    private Long updateUser;

    /** 出库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Excel(name = "出库时间", width = 30, dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date outTime;

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
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setBackNo(String backNo)
    {
        this.backNo = backNo;
    }

    public String getBackNo()
    {
        return backNo;
    }
    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getProductId()
    {
        return productId;
    }
    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    public String getBatchNo()
    {
        return batchNo;
    }
    public void setBackNum(Long backNum)
    {
        this.backNum = backNum;
    }

    public Long getBackNum()
    {
        return backNum;
    }
    public void setActBackNum(Long actBackNum)
    {
        this.actBackNum = actBackNum;
    }

    public Long getActBackNum()
    {
        return actBackNum;
    }
    public void setStorageFrom(String storageFrom)
    {
        this.storageFrom = storageFrom;
    }

    public String getStorageFrom()
    {
        return storageFrom;
    }
    public void setStorageTo(String storageTo)
    {
        this.storageTo = storageTo;
    }

    public String getStorageTo()
    {
        return storageTo;
    }
    public void setFromStorageId(Long fromStorageId)
    {
        this.fromStorageId = fromStorageId;
    }

    public Long getFromStorageId()
    {
        return fromStorageId;
    }
    public void setToStorageId(Long toStorageId)
    {
        this.toStorageId = toStorageId;
    }

    public Long getToStorageId()
    {
        return toStorageId;
    }
    public void setOutTime(Date outTime)
    {
        this.outTime = outTime;
    }

    public Date getOutTime()
    {
        return outTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("tenantId", getTenantId())
            .append("status", getStatus())
            .append("backNo", getBackNo())
            .append("productId", getProductId())
            .append("batchNo", getBatchNo())
            .append("backNum", getBackNum())
            .append("actBackNum", getActBackNum())
            .append("storageFrom", getStorageFrom())
            .append("storageTo", getStorageTo())
            .append("fromStorageId", getFromStorageId())
            .append("toStorageId", getToStorageId())
            .append("remark", getRemark())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("outTime", getOutTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
