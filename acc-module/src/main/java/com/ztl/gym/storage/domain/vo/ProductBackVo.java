package com.ztl.gym.storage.domain.vo;

/**
 * 退货对象 t_storage_back
 *
 * @author ruoyi
 * @date 2021-04-19
 */
public class ProductBackVo {
    private static final long serialVersionUID = 1L;

    private String codeIndex;
    private String productNo;
    private String productName;
    private String batchNo;
    private String backNum;


    public String getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(String codeIndex) {
        this.codeIndex = codeIndex;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBackNum() {
        return backNum;
    }

    public void setBackNum(String backNum) {
        this.backNum = backNum;
    }
}
