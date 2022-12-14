package com.ztl.gym.storage.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 入库对象 t_storage_in
 *
 * @author ruoyi
 * @date 2021-04-09
 */
public class StorageIn extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 状态-待入库
     */
    public final static int STATUS_WAIT = 0;
    /**
     * 状态-已确认收货
     */
    public final static int STATUS_NORMAL = 1;
    /**
     * 状态-已发货 【针对一键发货】
     */
    public final static int STATUS_OUT = 2;
    /**
     * 状态-已撤销
     */
    public final static int STATUS_CANCEL = 8;
    /**
     * 状态-已删除
     */
    public final static int STATUS_DELETE = 9;

    /**
     * 普通入库
     */
    public final static int IN_TYPE_COMMON = 1;
    /**
     * 退货入库
     */
    public final static int IN_TYPE_BACK = 2;


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
     * 状态
     */
    @Excel(name = "状态")
    private Integer status;

    /**
     * 入库类型
     */
    private int inType;

    /**
     * 入库单号
     */
    @Excel(name = "入库单号")
    private String inNo;

    /**
     * 相关单号
     */
    @Excel(name = "相关单号")
    private String extraNo;

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
     * 入库数量
     */
    @Excel(name = "入库数量")
    private Long inNum;

    /**
     * 实际入库数量
     */
    @Excel(name = "实际入库数量")
    private Long actInNum;

    /**
     * 发货单位
     */
    @Excel(name = "发货单位")
    private Long storageFrom;

    /**
     * 收货单位
     */
    @Excel(name = "收货单位")
    private Long storageTo;

    /**
     * 出库仓库
     */
    @Excel(name = "出库仓库")
    private Long fromStorageId;

    /**
     * 入库仓库
     */
    @Excel(name = "入库仓库")
    private Long toStorageId;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    @TableField(exist = false)
    private String createUserName;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改人
     */
    @Excel(name = "修改人")
    @TableField(exist = false)
    private String updateUserName;
    /**
     * 发货单位
     */
    @Excel(name = "发货单位")
    @TableField(exist = false)
    private String storageFromName;
    /**
     * 入库仓库
     */
    @Excel(name = "入库仓库")
    @TableField(exist = false)
    private String toStorageIdName;


    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /*-------------------------------- 冗余 --------------------------------*/
    /**
     * 规格型号
     */
    private String productNo;
    /**
     * 物料名称
     */
    private String productName;

    /**
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入库时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date inTime;

    public int getInType() {
        return inType;
    }

    public void setInType(int inType) {
        this.inType = inType;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setInNo(String inNo) {
        this.inNo = inNo;
    }

    public String getInNo() {
        return inNo;
    }

    public String getExtraNo() {
        return extraNo;
    }

    public void setExtraNo(String extraNo) {
        this.extraNo = extraNo;
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

    public void setInNum(Long inNum) {
        this.inNum = inNum;
    }

    public Long getInNum() {
        return inNum;
    }

    public void setActInNum(Long actInNum) {
        this.actInNum = actInNum;
    }

    public Long getActInNum() {
        return actInNum;
    }

    public void setStorageFrom(Long storageFrom) {
        this.storageFrom = storageFrom;
    }

    public Long getStorageFrom() {
        return storageFrom;
    }

    public void setStorageTo(Long storageTo) {
        this.storageTo = storageTo;
    }

    public Long getStorageTo() {
        return storageTo;
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

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getInTime() {
        return inTime;
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

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getStorageFromName() {
        return storageFromName;
    }

    public void setStorageFromName(String storageFromName) {
        this.storageFromName = storageFromName;
    }

    public String getToStorageIdName() {
        return toStorageIdName;
    }

    public void setToStorageIdName(String toStorageIdName) {
        this.toStorageIdName = toStorageIdName;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("tenantId", getTenantId())
                .append("status", getStatus())
                .append("inNo", getInNo())
                .append("productId", getProductId())
                .append("batchNo", getBatchNo())
                .append("inNum", getInNum())
                .append("actInNum", getActInNum())
                .append("storageFrom", getStorageFrom())
                .append("storageTo", getStorageTo())
                .append("fromStorageId", getFromStorageId())
                .append("toStorageId", getToStorageId())
                .append("remark", getRemark())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("inTime", getInTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
