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
public class Product extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long companyId;

    /** 产品编号 */
    @Excel(name = "产品编号")
    private String productNo;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 产品条码 */
    @Excel(name = "产品条码")
    private String barCode;

    /** 产品一级分类 */
    @Excel(name = "产品一级分类")
    private Long categoryOne;

    /** 产品二级分类 */
    @Excel(name = "产品二级分类")
    private Long categoryTwo;

    /** 产品二级分类名称 */
    @Excel(name = "产品二级分类名称")
    @TableField(exist = false)
    private String categoryName;

    /** 售价 */
    @Excel(name = "售价")
    private BigDecimal salePrice;

    /** 市场价 */
    @Excel(name = "市场价")
    private BigDecimal marketPrice;

    /** 产品介绍 */
    @Excel(name = "产品介绍")
    private String productIntroduce;

    /** 产品PC端详情 */
    @Excel(name = "产品PC端详情")
    private String productDetailPc;

    /** 产品移动端详情 */
    @Excel(name = "产品移动端详情")
    private String productDetailMobile;

    /** 所属仓库 */
    @Excel(name = "所属仓库")
    private Long storageId;

    /** 库存 */
    @Excel(name = "库存")
    private Long stock;

    /** 库存预警值 */
    @Excel(name = "库存预警值")
    private Long stockLimit;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String unit;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 销量 */
    @Excel(name = "销量")
    private Long salesNum;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createUser;


    /** 创建人 */
    private String createUserName;

    /** 更新人 */
    @Excel(name = "更新人")
    private Long updateUser;

    /**----------冗余字段-------------*/
    /** 产品分类*/
    private Long proaductCategory;

    /** 产品属性*/
    private List<Map<String,Object>> attributeList;

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
    public void setProductNo(String productNo)
    {
        this.productNo = productNo;
    }

    public String getProductNo()
    {
        return productNo;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductName()
    {
        return productName;
    }
    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
    }

    public String getBarCode()
    {
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

    public BigDecimal getSalePrice()
    {
        return salePrice;
    }
    public void setMarketPrice(BigDecimal marketPrice)
    {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getMarketPrice()
    {
        return marketPrice;
    }
    public void setProductIntroduce(String productIntroduce)
    {
        this.productIntroduce = productIntroduce;
    }

    public String getProductIntroduce()
    {
        return productIntroduce;
    }
    public void setProductDetailPc(String productDetailPc)
    {
        this.productDetailPc = productDetailPc;
    }

    public String getProductDetailPc()
    {
        return productDetailPc;
    }
    public void setProductDetailMobile(String productDetailMobile)
    {
        this.productDetailMobile = productDetailMobile;
    }

    public String getProductDetailMobile()
    {
        return productDetailMobile;
    }
    public void setStorageId(Long storageId)
    {
        this.storageId = storageId;
    }

    public Long getStorageId()
    {
        return storageId;
    }
    public void setStock(Long stock)
    {
        this.stock = stock;
    }

    public Long getStock()
    {
        return stock;
    }
    public void setStockLimit(Long stockLimit)
    {
        this.stockLimit = stockLimit;
    }

    public Long getStockLimit()
    {
        return stockLimit;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }
    public void setSalesNum(Long salesNum)
    {
        this.salesNum = salesNum;
    }

    public Long getSalesNum()
    {
        return salesNum;
    }
    public void setCreateUser(Long createUser)
    {
        this.createUser = createUser;
    }

    public Long getCreateUser()
    {
        return createUser;
    }
    public void setUpdateUser(Long updateUser)
    {
        this.updateUser = updateUser;
    }

    public Long getUpdateUser()
    {
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
            .toString();
    }
}
