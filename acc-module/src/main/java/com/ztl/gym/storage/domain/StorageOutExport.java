package com.ztl.gym.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;

import java.util.Date;

public class StorageOutExport extends BaseEntity {
    /**
     * 收货单位
     */
    @Excel(name = "收货单位")
    private String deptName;

    /**
     * 产品型号
     */
    @Excel(name = "产品型号")
    private String productNo;

    /**
     * 产品名称
     */
    @Excel(name = "产品名称")
    private String productName;

    /**
     * 应发数量
     */
    @Excel(name = "应发数量")
    private Long outNum;

    /**
     * 实发数量
     */
    @Excel(name = "实发数量")
    private Long actOutNum;

    /**
     * 出库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出库时间", width = 15, dateFormat = "yyyy-MM-dd")
    private Date outTime;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

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

    public Long getOutNum() {
        return outNum;
    }

    public void setOutNum(Long outNum) {
        this.outNum = outNum;
    }

    public Long getActOutNum() {
        return actOutNum;
    }

    public void setActOutNum(Long actOutNum) {
        this.actOutNum = actOutNum;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }
}
