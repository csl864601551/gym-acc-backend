package com.ztl.gym.storage.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 扫码记录对象 t_scan_record
 *
 * @author ruoyi
 * @date 2021-04-28
 */
public class ScanRecord extends BaseEntity {
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
     * 码
     */
    @Excel(name = "码")
    private String code;

    /**
     * 经度
     */
    @Excel(name = "经度")
    private String longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    private String latitude;

    /**
     * 访问终端
     */
    @Excel(name = "访问终端")
    private String terminal;

    /**
     * 访问ip
     */
    @Excel(name = "访问ip")
    private String ip;

    /**
     * 访问地址
     */
    @Excel(name = "访问地址")
    private String address;

    /**
     * 访问省
     */
    @Excel(name = "访问省")
    private String province;

    /**
     * 访问市
     */
    @Excel(name = "访问市")
    private String city;

    /**
     * 访问区
     */
    @Excel(name = "访问区")
    private String area;

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

    /**
     * 来源
     */
    private String fromType;


    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;


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

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
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

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
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

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("code", getCode())
                .append("longitude", getLongitude())
                .append("latitude", getLatitude())
                .append("terminal", getTerminal())
                .append("ip", getIp())
                .append("address", getAddress())
                .append("province", getProvince())
                .append("city", getCity())
                .append("area", getArea())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .append("productId", getProductId())
                .append("productName", getProductName())
                .toString();
    }
}
