package com.ztl.gym.storage.domain;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 出库对象 t_storage_out
 *
 * @author ruoyi
 * @date 2021-04-09
 */
public class StorageOut extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 状态-待出库
     */
    public final static int STATUS_WAIT = 0;
    /**
     * 状态-正常出库
     */
    public final static int STATUS_NORMAL = 1;
    /**
     * 状态-已撤销
     */
    public final static int STATUS_CANCEL = 8;
    /**
     * 状态-已删除
     */
    public final static int STATUS_DELETE = 9;

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
     * 出库单号
     */
    @Excel(name = "出库单号")
    private String outNo;

    /**
     * 产品id
     */
    @Excel(name = "产品id")
    private Long productId;

    /** 相关单号 */
    @Excel(name = "相关单号")
    private String extraNo;

    /**
     * 产品批次
     */
    @Excel(name = "产品批次")
    private String batchNo;

    /**
     * 出库数量
     */
    @Excel(name = "出库数量")
    private Long outNum;

    /**
     * 实际出库数量
     */
    @Excel(name = "实际出库数量")
    private Long actOutNum;

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
     * 创建人
     */
    @Excel(name = "创建人")
    private Long createUser;

    /**
     * 修改人
     */
    @Excel(name = "修改人")
    private Long updateUser;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    @TableField(exist = false)
    private String createUserName;

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
     * 收货单位
     */
    @Excel(name = "收货单位")
    @TableField(exist = false)
    private String storageToName;
    /**
     * 出库仓库
     */
    @Excel(name = "出库仓库")
    @TableField(exist = false)
    private String fromStorageIdName;
    /**
     * 出库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出库时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date outTime;

    @TableField(exist = false)
    private String thirdPartyFlag;

    @TableField(exist = false)
    private String code;

    @TableField(exist = false)
    private List<String> codes;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /*-------------------------------- 冗余 --------------------------------*/
    /**
     * 产品编号
     */
    private String productNo;
    /**
     * 产品名称
     */
    private String productName;

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

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public String getOutNo() {
        return outNo;
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

    public void setOutNum(Long outNum) {
        this.outNum = outNum;
    }

    public Long getOutNum() {
        return outNum;
    }

    public void setActOutNum(Long actOutNum) {
        this.actOutNum = actOutNum;
    }

    public Long getActOutNum() {
        return actOutNum;
    }

    public String getExtraNo() {
        return extraNo;
    }

    public void setExtraNo(String extraNo) {
        this.extraNo = extraNo;
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

    public String getFromStorageIdName() {
        return fromStorageIdName;
    }

    public void setFromStorageIdName(String fromStorageIdName) {
        this.fromStorageIdName = fromStorageIdName;
    }

    public String getStorageToName() {
        return storageToName;
    }

    public void setStorageToName(String storageToName) {
        this.storageToName = storageToName;
    }

    public String getThirdPartyFlag() {
        return thirdPartyFlag;
    }

    public void setThirdPartyFlag(String thirdPartyFlag) {
        this.thirdPartyFlag = thirdPartyFlag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("tenantId", getTenantId())
                .append("status", getStatus())
                .append("outNo", getOutNo())
                .append("productId", getProductId())
                .append("batchNo", getBatchNo())
                .append("outNum", getOutNum())
                .append("actOutNum", getActOutNum())
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
