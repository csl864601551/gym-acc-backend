package com.ztl.gym.product.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 产品分类对象 t_product_category
 *
 * @author ruoyi
 * @date 2021-04-12
 */
public class ProductCategory extends BaseEntity {
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
     * 编号
     */
    @Excel(name = "编号")
    private String categoryNo;

    /**
     * 分类名称
     */
    @Excel(name = "分类名称")
    private String categoryName;

    /**
     * 分类级别
     */
    @Excel(name = "分类级别")
    private String categoryLevel;

    /**
     * 上级分类
     */
    @Excel(name = "上级分类")
    private Long pId;

    /**
     * 数量单位
     */
    @Excel(name = "数量单位")
    private String unit;

    /**
     * 分类图标
     */
    @Excel(name = "分类图标")
    private String icon;

    /**
     * 关键词
     */
    @Excel(name = "关键词")
    private String keyword;

    /**
     * 分类描述
     */
    @Excel(name = "分类描述")
    private String categoryIntroduce;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long sort;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private Long createUser;

    /**
     * 创建人
     */
    @Excel(name = "创建姓名")
    private String createUserName;

    /**
     * 更新人
     */
    @Excel(name = "更新人")
    private Long updateUser;


    /**
     * 状态
     */
    private Long status;


    /**
     * 状态
     */
    private Long productNum;

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

    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryLevel(String categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public String getCategoryLevel() {
        return categoryLevel;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public Long getpId() {
        return pId;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setCategoryIntroduce(String categoryIntroduce) {
        this.categoryIntroduce = categoryIntroduce;
    }

    public String getCategoryIntroduce() {
        return categoryIntroduce;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Long getSort() {
        return sort;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getProductNum() {
        return productNum;
    }

    public void setProductNum(Long productNum) {
        this.productNum = productNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("categoryNo", getCategoryNo())
                .append("categoryName", getCategoryName())
                .append("categoryLevel", getCategoryLevel())
                .append("pId", getpId())
                .append("unit", getUnit())
                .append("icon", getIcon())
                .append("keyword", getKeyword())
                .append("categoryIntroduce", getCategoryIntroduce())
                .append("sort", getSort())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .append("createUserName", getCreateUserName())
                .append("productNum", getProductNum())
                .toString();
    }
}
