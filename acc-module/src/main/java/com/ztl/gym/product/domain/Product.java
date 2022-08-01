package com.ztl.gym.product.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 产品对象 t_product
 *
 * @author ruoyi
 * @date 2021-04-10
 */
public class Product extends BaseEntity {
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
     * 规格型号
     */
    @Excel(name = "规格型号")
    private String productNo;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Long status;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String productName;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码")
    private String barCode;
    /**
     * 打印型号
     */
    @Excel(name = "打印型号")
    private String printNo;
    /**
     * 包装规格
     */
    @Excel(name = "包装规格")
    private String boxCount;
    /**
     * 产品系列
     */
    @Excel(name = "产品系列")
    private String productSeries;

    /**
     * 产品一级分类
     */
    @Excel(name = "产品一级分类")
    private Long categoryOne;

    /**
     * 产品二级分类
     */
    @Excel(name = "产品二级分类")
    private Long categoryTwo;

    /**
     * 产品二级分类名称
     */
    @Excel(name = "产品二级分类名称")
    @TableField(exist = false)
    private String categoryName;

    /**
     * 售价
     */
    @Excel(name = "售价")
    private BigDecimal salePrice;

    /**
     * 市场价
     */
    @Excel(name = "市场价")
    private BigDecimal marketPrice;

    /**
     * 产品介绍
     */
    @Excel(name = "产品介绍")
    private String productIntroduce;

    /**
     * 产品PC端详情
     */
    @Excel(name = "产品PC端详情")
    private String productDetailPc;

    /**
     * 产品移动端详情
     */
    @Excel(name = "产品移动端详情")
    private String productDetailMobile;

    /**
     * 所属仓库
     */
    @Excel(name = "所属仓库")
    private Long storageId;

    /**
     * 库存
     */
    @Excel(name = "库存")
    private Long stock;

    /**
     * 库存预警值
     */
    @Excel(name = "库存预警值")
    private Long stockLimit;

    /**
     * 计量单位
     */
    @Excel(name = "计量单位")
    private String unit;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long sort;

    /**
     * 销量
     */
    @Excel(name = "销量")
    private Long salesNum;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private Long createUser;


    /**
     * 创建人
     */
    private String createUserName;

    /**
     * 更新人
     */
    @Excel(name = "更新人")
    private Long updateUser;

    /**----------冗余字段-------------*/
    /**
     * 产品分类
     */
    private Long proaductCategory;

    /**
     * 产品属性
     */
    private List<Map<String, Object>> attributeList;

    /**----------防伪新增字段-------------*/
    /**
     * 防伪模板1
     */
    @Excel(name = "防伪模板1")
    private Long templateId1;

    /**
     * 防伪模板2
     */
    @Excel(name = "防伪模板2")
    private Long templateId2;

    /**
     * 防伪模板1内容
     */
    @Excel(name = "防伪模板1内容")
    private String templateContent1;

    /**
     * 防伪模板2内容
     */
    @Excel(name = "防伪模板2内容")
    private String templateContent2;

    /**
     * 单次查询内容
     */
    @Excel(name = "单次查询内容")
    private String content1;

    /**
     * 多次查询内容
     */
    @Excel(name = "多次查询内容")
    private String content2;

    private String photo;
    /**
     * 多次查询内容
     */
    private String updateType;

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

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStatus() {
        return status;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public Long getCategoryOne() {
        return categoryOne;
    }

    public void setCategoryOne(Long categoryOne) {
        this.categoryOne = categoryOne;
    }

    public Long getCategoryTwo() {
        return categoryTwo;
    }

    public void setCategoryTwo(Long categoryTwo) {
        this.categoryTwo = categoryTwo;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setProductIntroduce(String productIntroduce) {
        this.productIntroduce = productIntroduce;
    }

    public String getProductIntroduce() {
        return productIntroduce;
    }

    public void setProductDetailPc(String productDetailPc) {
        this.productDetailPc = productDetailPc;
    }

    public String getProductDetailPc() {
        return productDetailPc;
    }

    public void setProductDetailMobile(String productDetailMobile) {
        this.productDetailMobile = productDetailMobile;
    }

    public String getProductDetailMobile() {
        return productDetailMobile;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getStock() {
        return stock;
    }

    public void setStockLimit(Long stockLimit) {
        this.stockLimit = stockLimit;
    }

    public Long getStockLimit() {
        return stockLimit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Long getSort() {
        return sort;
    }

    public void setSalesNum(Long salesNum) {
        this.salesNum = salesNum;
    }

    public Long getSalesNum() {
        return salesNum;
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

    public Long getProaductCategory() {
        return proaductCategory;
    }

    public void setProaductCategory(Long proaductCategory) {
        this.proaductCategory = proaductCategory;
    }

    public List<Map<String, Object>> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Map<String, Object>> attributeList) {
        this.attributeList = attributeList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getTemplateId1() {
        return templateId1;
    }

    public void setTemplateId1(Long templateId1) {
        this.templateId1 = templateId1;
    }

    public Long getTemplateId2() {
        return templateId2;
    }

    public void setTemplateId2(Long templateId2) {
        this.templateId2 = templateId2;
    }

    public String getTemplateContent1() {
        return templateContent1;
    }

    public void setTemplateContent1(String templateContent1) {
        this.templateContent1 = templateContent1;
    }

    public String getTemplateContent2() {
        return templateContent2;
    }

    public void setTemplateContent2(String templateContent2) {
        this.templateContent2 = templateContent2;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrintNo() {
        return printNo;
    }

    public void setPrintNo(String printNo) {
        this.printNo = printNo;
    }

    public String getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(String boxCount) {
        this.boxCount = boxCount;
    }

    public String getProductSeries() {
        return productSeries;
    }

    public void setProductSeries(String productSeries) {
        this.productSeries = productSeries;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("productNo", getProductNo())
                .append("status", getStatus())
                .append("productName", getProductName())
                .append("barCode", getBarCode())
                .append("categoryOne", getCategoryOne())
                .append("categoryTwo", getCategoryTwo())
                .append("salePrice", getSalePrice())
                .append("marketPrice", getMarketPrice())
                .append("productIntroduce", getProductIntroduce())
                .append("productDetailPc", getProductDetailPc())
                .append("productDetailMobile", getProductDetailMobile())
                .append("storageId", getStorageId())
                .append("stock", getStock())
                .append("stockLimit", getStockLimit())
                .append("unit", getUnit())
                .append("sort", getSort())
                .append("salesNum", getSalesNum())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .append("createUserName", getCreateUserName())
                .append("templateId1", getTemplateId1())
                .append("templateId2", getTemplateId2())
                .append("templateContent1", getTemplateContent1())
                .append("templateContent2", getTemplateContent2())
                .append("content1", getContent1())
                .append("content2", getContent2())
                .append("updateType", getUpdateType())
                .append("photo", getPhoto())
                .toString();
    }
}
