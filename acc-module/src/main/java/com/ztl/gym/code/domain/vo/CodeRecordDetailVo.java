package com.ztl.gym.code.domain.vo;

import com.ztl.gym.code.domain.Code;

import java.util.List;

/**
 * 生码记录详情
 */
public class CodeRecordDetailVo {
    /**
     * 生码记录id
     */
    private long recordId;
    /**
     * 状态
     */
    private String status;
    /**
     * 生码类型
     */
    private String type;
    /**
     * 产品名
     */
    private String productName;
    /**
     * 物料编码
     */
    private String barCode;
    /**
     * 产品批次
     */
    private String batchNo;
    /**
     * 流水号范围
     */
    private String codeIndexs;
    /**
     * 规格数量
     */
    private String sizeNum;
    /**
     * 生码时间
     */
    private String createTime;
    /**
     * 生码集合
     */
    private List<Code> codes;

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getCodeIndexs() {
        return codeIndexs;
    }

    public void setCodeIndexs(String codeIndexs) {
        this.codeIndexs = codeIndexs;
    }

    public String getSizeNum() {
        return sizeNum;
    }

    public void setSizeNum(String sizeNum) {
        this.sizeNum = sizeNum;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
