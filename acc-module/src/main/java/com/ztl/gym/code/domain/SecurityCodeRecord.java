package com.ztl.gym.code.domain;

import java.math.BigDecimal;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

/**
 * 防伪记录  t_security_code_record
 *
 * @author wujinhao
 * @date 2021-08-02
 */
public class SecurityCodeRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 企业ID
     */
    @Excel(name = "企业ID")
    private Long companyId;

    /**
     * 防窜码
     */
    @Excel(name = "防窜码")
    private String code;

    /**
     * 防伪码
     */
    @Excel(name = "防伪码")
    private String codeAcc;

    /**
     * 批次ID
     */
    @Excel(name = "批次ID")
    private Long batchId;

    /**
     * 产品Id
     */
    @Excel(name = "产品Id")
    private Long productId;

    /**
     * 经度
     */
    @Excel(name = "经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    private BigDecimal latitude;

    /**
     * 区编码
     */
    @Excel(name = "区编码")
    private String districtCode;

    /**
     * 市编码
     */
    @Excel(name = "市编码")
    private String cityCode;

    /**
     * 省编码
     */
    @Excel(name = "省编码")
    private String provinceCode;

    /**
     * 省
     */
    @Excel(name = "省")
    private String province;

    /**
     * 市
     */
    @Excel(name = "市")
    private String city;

    /**
     * 区
     */
    @Excel(name = "区")
    private String district;

    /**
     * 是否异常 1.异常 0.正常
     */
    @Excel(name = "是否异常 1.异常 0.正常")
    private String isAbnormal;

    /**
     * 是否删除
     */
    @Excel(name = "是否删除")
    private String isDelete;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeAcc() {
        return codeAcc;
    }

    public void setCodeAcc(String codeAcc) {
        this.codeAcc = codeAcc;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public void setIsAbnormal(String isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsDelete() {
        return isDelete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("code", getCode())
                .append("codeAcc", getCodeAcc())
                .append("batchId", getBatchId())
                .append("productId", getProductId())
                .append("longitude", getLongitude())
                .append("latitude", getLatitude())
                .append("districtCode", getDistrictCode())
                .append("cityCode", getCityCode())
                .append("provinceCode", getProvinceCode())
                .append("province", getProvince())
                .append("city", getCity())
                .append("district", getDistrict())
                .append("isAbnormal", getIsAbnormal())
                .append("isDelete", getIsDelete())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
