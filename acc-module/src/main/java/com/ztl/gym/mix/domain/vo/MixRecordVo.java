package com.ztl.gym.mix.domain.vo;

import com.ztl.gym.common.annotation.Excel;

public class MixRecordVo {

    /** 批次编号 */
    @Excel(name = "批次编号")
    private String batchNo;

    /** 产品编号 */
    @Excel(name = "产品编号")
    private String productNo;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 码/流水号 */
    @Excel(name = "码/流水号")
    private String code;

    /** 窜货类型 */
    @Excel(name = "窜货类型")
    private String mixType;

    /** 经销商名称 */
    @Excel(name = "经销商名称")
    private String tenantName;

    /** 上级名称 */
    @Excel(name = "上级名称")
    private String companyName;

    /** 原地址 */
    @Excel(name = "销售地址")
    private String areaOld;

    /** 窜货区 */
    @Excel(name = "窜货地址")
    private String areaMix;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMixType() {
        return mixType;
    }

    public void setMixType(String mixType) {
        this.mixType = mixType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAreaOld() {
        return areaOld;
    }

    public void setAreaOld(String areaOld) {
        this.areaOld = areaOld;
    }

    public String getAreaMix() {
        return areaMix;
    }

    public void setAreaMix(String areaMix) {
        this.areaMix = areaMix;
    }
}
