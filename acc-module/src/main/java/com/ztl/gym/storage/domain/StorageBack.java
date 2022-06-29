package com.ztl.gym.storage.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.code.domain.Code;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import com.ztl.gym.storage.domain.vo.ProductBackVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 退货对象 t_storage_back
 *
 * @author ruoyi
 * @date 2021-04-19
 */
public class StorageBack extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 状态-待退货
     */
    public final static int STATUS_WAIT = 0;
    /**
     * 状态-正常退货
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
     * 退货类型-普通退货
     */
    public final static int BACK_TYPE_NORMAL = 0;
    /**
     * 退货类型-调拨退货
     */
    public final static int BACK_TYPE_TRANSFER = 1;

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
     * 退货类型
     */
    private Integer backType;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Integer status;

    /**
     * 退货单号
     */
    @Excel(name = "退货单号")
    private String backNo;

    /**
     * 相关单号
     */
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
     * 退货数量
     */
    @Excel(name = "退货数量")
    private Long backNum;

    /**
     * 实际退货数量
     */
    @Excel(name = "实际退货数量")
    private Long actBackNum;

    /**
     * 退货单位
     */
    @Excel(name = "退货单位")
    private Long storageFrom;

    /**
     * 收货单位
     */
    @Excel(name = "收货单位")
    private Long storageTo;

    /**
     * 退货仓库
     */
    @Excel(name = "退货仓库")
    private Long fromStorageId;

    /**
     * 收入仓库
     */
    @Excel(name = "收入仓库")
    private Long toStorageId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outTime;

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
    /*------------------------------ 冗余字段 ------------------------------*/
    /**
     * 流水号范围
     */
    private String codeIndex;
    /**
     * 规格型号
     */
    private String productNo;
    /**
     * 物料名称
     */
    private String productName;
    /**
     * 货码【可能是箱码或单码】
     */
    private String codeStr;
    /**
     * 收入仓库名称
     */
    private String toStorageName;
    /**
     * 操作人昵称
     */
    private String nickName;
    /**
     * 退货单位名称
     */
    private String storageFromName;
    /**
     * 收货单位名称
     */
    private String storageToName;
    /**
     * 货码 【码或者调拨单号】
     */
    private String value;
    /**
     * 产品信息
     */
    private List<ProductBackVo> productBackVoList;
    /**
     * 码信息
     */
    private List<Code> codeList;


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

    public void setBackNo(String backNo) {
        this.backNo = backNo;
    }

    public String getBackNo() {
        return backNo;
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

    public void setBackNum(Long backNum) {
        this.backNum = backNum;
    }

    public Long getBackNum() {
        return backNum;
    }

    public void setActBackNum(Long actBackNum) {
        this.actBackNum = actBackNum;
    }

    public Long getActBackNum() {
        return actBackNum;
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

    public String getToStorageName() {
        return toStorageName;
    }

    public void setToStorageName(String toStorageName) {
        this.toStorageName = toStorageName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCodeStr() {
        return codeStr;
    }

    public void setCodeStr(String codeStr) {
        this.codeStr = codeStr;
    }

    public Integer getBackType() {
        return backType;
    }

    public void setBackType(Integer backType) {
        this.backType = backType;
    }

    public String getStorageFromName() {
        return storageFromName;
    }

    public void setStorageFromName(String storageFromName) {
        this.storageFromName = storageFromName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtraNo() {
        return extraNo;
    }

    public void setExtraNo(String extraNo) {
        this.extraNo = extraNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(String codeIndex) {
        this.codeIndex = codeIndex;
    }

    public List<ProductBackVo> getProductBackVoList() {
        return productBackVoList;
    }

    public void setProductBackVoList(List<ProductBackVo> productBackVoList) {
        this.productBackVoList = productBackVoList;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public List<Code> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Code> codeList) {
        this.codeList = codeList;
    }

    public String getStorageToName() {
        return storageToName;
    }

    public void setStorageToName(String storageToName) {
        this.storageToName = storageToName;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
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
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
