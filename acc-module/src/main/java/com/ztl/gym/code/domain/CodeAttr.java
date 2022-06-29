package com.ztl.gym.code.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.domain.ProductBatch;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 码属性对象 t_code_attr
 *
 * @author ruoyi
 * @date 2021-04-15
 */
public class CodeAttr extends BaseEntity {
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
     * 当前所属经销商id
     */
    private Long tenantId;

    /**
     * 当前流转类型
     */
    private Integer storageType;

    /**
     * 当前流转记录id
     */
    private Long storageRecordId;

    /**
     * 生码记录id
     */
    @Excel(name = "生码记录id")
    private Long recordId;


    /**
     * 生码记录id
     */
    @Excel(name = "生码记录id")
    private Long singleId;


    /**
     * 起始流水号
     */
    @Excel(name = "起始流水号")
    private Long indexStart;

    /**
     * 截止流水号
     */
    @Excel(name = "截止流水号")
    private Long indexEnd;

    /**
     * 关联产品id
     */
    @Excel(name = "关联产品id")
    private Long productId;

    /**
     * 关联物料名称
     */
    private String productName;

    /**
     * 关联规格型号
     */
    @Excel(name = "关联规格型号")
    private String productNo;

    /**
     * 关联物料编码
     */
    @Excel(name = "关联物料编码")
    private String barCode;

    /**
     * 关联产品分类
     */
    @Excel(name = "关联产品分类")
    private String productCategory;

    /**
     * 关联产品单位
     */
    @Excel(name = "关联产品单位")
    private String productUnit;

    /**
     * 关联批次id
     */
    @Excel(name = "关联批次id")
    private Long batchId;

    /**
     * 关联批次编号
     */
    @Excel(name = "关联批次编号")
    private String batchNo;

    /**
     * 关联产品简介
     */
    @Excel(name = "关联产品简介")
    private String productIntroduce;

    /**
     * 赋值操作人
     */
    @Excel(name = "赋值操作人")
    private Long inputBy;

    /**
     * 赋值时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "赋值时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date inputTime;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private Long createUser;

    /**
     * 更新人
     */
    @Excel(name = "更新人")
    private Long updateUser;

    /*--------------------------------  冗余字段  --------------------------------*/
    /**
     * 产品信息
     */
    private Product product;
    /**
     * 产品批次
     */
    private ProductBatch productBatch;
    /**
     * 生码记录
     */
    private CodeRecord codeRecord;

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

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }

    public Long getStorageRecordId() {
        return storageRecordId;
    }

    public void setStorageRecordId(Long storageRecordId) {
        this.storageRecordId = storageRecordId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public void setIndexStart(Long indexStart) {
        this.indexStart = indexStart;
    }

    public Long getIndexStart() {
        return indexStart;
    }

    public void setIndexEnd(Long indexEnd) {
        this.indexEnd = indexEnd;
    }

    public Long getIndexEnd() {
        return indexEnd;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setProductIntroduce(String productIntroduce) {
        this.productIntroduce = productIntroduce;
    }

    public String getProductIntroduce() {
        return productIntroduce;
    }

    public void setInputBy(Long inputBy) {
        this.inputBy = inputBy;
    }

    public Long getInputBy() {
        return inputBy;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductBatch getProductBatch() {
        return productBatch;
    }

    public void setProductBatch(ProductBatch productBatch) {
        this.productBatch = productBatch;
    }

    public CodeRecord getCodeRecord() {
        return codeRecord;
    }

    public void setCodeRecord(CodeRecord codeRecord) {
        this.codeRecord = codeRecord;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSingleId() {
        return singleId;
    }

    public void setSingleId(Long singleId) {
        this.singleId = singleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("recordId", getRecordId())
                .append("indexStart", getIndexStart())
                .append("indexEnd", getIndexEnd())
                .append("productId", getProductId())
                .append("productNo", getProductNo())
                .append("barCode", getBarCode())
                .append("productCategory", getProductCategory())
                .append("productUnit", getProductUnit())
                .append("batchId", getBatchId())
                .append("batchNo", getBatchNo())
                .append("productIntroduce", getProductIntroduce())
                .append("remark", getRemark())
                .append("inputBy", getInputBy())
                .append("inputTime", getInputTime())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
