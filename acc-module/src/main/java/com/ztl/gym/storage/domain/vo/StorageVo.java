package com.ztl.gym.storage.domain.vo;

import java.util.Date;

/**
 * 码物流包装类
 */
public class StorageVo {
    /*--------------------- 基础数据 ---------------------*/
    /**
     * 企业id
     */
    private long companyId;
    /**
     * 经销商id
     */
    private long tenantId;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 经销商名称
     */
    private String tenantName;
    /**
     * 物流流转类型
     */
    private int storageType;
    /**
     * 物流流转记录id
     */
    private int storageRecordId;
    /**
     * 产品id
     */
    private long productId;
    /**
     * 产品编码
     */
    private String productNo;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 批次id
     */
    private long batchId;
    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 物流操作类型【1：单码操作  2：箱码操作】
     */
    private int codeType;

    /*--------------------- 单码操作 ---------------------*/
    /**
     * 流水号
     */
    private long codeIndex;
    /**
     * 码
     */
    private String code;

    /*--------------------- 按箱操作 ---------------------*/
    /**
     * 起始流水号
     */
    private long indexStart;
    /**
     * 截止流水号
     */
    private long indexEnd;
    /**
     * 箱码
     */
    private String pCode;
    /**
     * 单码数量
     */
    private Long num;

    /*--------------------- 入库信息 ---------------------*/
    /**
     * 入库单号
     */
    private String inNo;
    /**
     * 对应入库单号
     */
    private String inOutNo;
    /**
     * 对应入库单位
     */
    private String inOutUserName;

    /**
     * 入库仓库
     */
    private Date inStorage;
    /**
     * 入库备注
     */
    private String inRemark;

    /*--------------------- 出库信息 ---------------------*/
    /**
     * 出库单号
     */
    private String outNo;
    /**
     * 出库单位
     */
    private String outUserName;
    /**
     * 出库时间
     */
    private Date outTime;
    /**
     * 出库仓库
     */
    private Date outStorage;
    /**
     * 出库备注
     */
    private String outRemark;

    /*--------------------- 退货信息 ---------------------*/
    /**
     * 退货入库单号
     */
    private String backNo;
    /**
     * 对应出库单号
     */
    private String backOutNo;
    /**
     * 对应出库单位
     */
    private String backOutUserName;
    /**
     * 退货单位
     */
    private String backUserName;
    /**
     * 退货时间
     */
    private Date backTime;
    /**
     * 退货仓库
     */
    private Date backStorage;
    /**
     * 退货备注
     */
    private String backRemark;

    /*--------------------- 调拨信息 ---------------------*/


    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public int getStorageType() {
        return storageType;
    }

    public void setStorageType(int storageType) {
        this.storageType = storageType;
    }

    public int getStorageRecordId() {
        return storageRecordId;
    }

    public void setStorageRecordId(int storageRecordId) {
        this.storageRecordId = storageRecordId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getInNo() {
        return inNo;
    }

    public void setInNo(String inNo) {
        this.inNo = inNo;
    }

    public String getInOutNo() {
        return inOutNo;
    }

    public void setInOutNo(String inOutNo) {
        this.inOutNo = inOutNo;
    }

    public String getInOutUserName() {
        return inOutUserName;
    }

    public void setInOutUserName(String inOutUserName) {
        this.inOutUserName = inOutUserName;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public Date getInStorage() {
        return inStorage;
    }

    public void setInStorage(Date inStorage) {
        this.inStorage = inStorage;
    }

    public String getInRemark() {
        return inRemark;
    }

    public void setInRemark(String inRemark) {
        this.inRemark = inRemark;
    }

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public String getOutUserName() {
        return outUserName;
    }

    public void setOutUserName(String outUserName) {
        this.outUserName = outUserName;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public Date getOutStorage() {
        return outStorage;
    }

    public void setOutStorage(Date outStorage) {
        this.outStorage = outStorage;
    }

    public String getOutRemark() {
        return outRemark;
    }

    public void setOutRemark(String outRemark) {
        this.outRemark = outRemark;
    }

    public String getBackNo() {
        return backNo;
    }

    public void setBackNo(String backNo) {
        this.backNo = backNo;
    }

    public String getBackOutNo() {
        return backOutNo;
    }

    public void setBackOutNo(String backOutNo) {
        this.backOutNo = backOutNo;
    }

    public String getBackOutUserName() {
        return backOutUserName;
    }

    public void setBackOutUserName(String backOutUserName) {
        this.backOutUserName = backOutUserName;
    }

    public String getBackUserName() {
        return backUserName;
    }

    public void setBackUserName(String backUserName) {
        this.backUserName = backUserName;
    }

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public Date getBackStorage() {
        return backStorage;
    }

    public void setBackStorage(Date backStorage) {
        this.backStorage = backStorage;
    }

    public String getBackRemark() {
        return backRemark;
    }

    public void setBackRemark(String backRemark) {
        this.backRemark = backRemark;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public int getCodeType() {
        return codeType;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
    }

    public long getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(long codeIndex) {
        this.codeIndex = codeIndex;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getIndexStart() {
        return indexStart;
    }

    public void setIndexStart(long indexStart) {
        this.indexStart = indexStart;
    }

    public long getIndexEnd() {
        return indexEnd;
    }

    public void setIndexEnd(long indexEnd) {
        this.indexEnd = indexEnd;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
}
