package com.ztl.gym.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;

import java.util.Date;

public class StorageOutExport extends BaseEntity {


    /**
     * 经销商编号
     */
    @Excel(name = "经销商编号")
    private String deptNo;
    /**
     * 经销商名称
     */
    @Excel(name = "经销商名称")
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
//    @Excel(name = "应发数量")
    private Long outNum;

    /**
     * 实发数量
     */
//    @Excel(name = "实发数量")
    private Long actOutNum;

    private String barCode;



    /**
     * 出库时间
     */
    @Excel(name = "流转码")
    private String code;

    /**
     * 出库时间
     */
    @Excel(name = "所属箱码")
    private String pCode;

    /**
     * 出库时间
     */
    @Excel(name = "box箱码，single单码")
    private String codeType;

    /**
     * 出库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出库时间", width = 15, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date outTime;

    /**
     * 出库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入库时间", width = 15, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date inTime;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

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
