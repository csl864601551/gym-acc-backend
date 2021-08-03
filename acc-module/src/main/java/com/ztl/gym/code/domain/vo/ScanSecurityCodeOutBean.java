package com.ztl.gym.code.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : wujinhao
 * @Description: 扫码返回对象
 * @date Date : 2021年08月03日 8:04
 */
public class ScanSecurityCodeOutBean{

    /**
     * 防伪码
     */
    private String codeAcc;

    /**
     * 单次查询防伪模板1内容
     */
    private String onceTemplateContent;

    /**
     * 多次查询防伪模板2内容
     */
    private String moreTemplateContent;

    /**
     * 单次查询内容
     */
    private String onceContent;

    /**
     * 多次查询内容
     */
    private String moreContent;

    /**
     * 扫描次数
     */
    private Integer count;

    /**
     * 标识码
     */
    private String code;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 首次查询时间
     */
    private Date firstQueryTime;

    /**
     * 最后查询时间
     */
    private Date lastQueryTime;

    /**
     * 企业名称
     */
    private String companyName;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getFirstQueryTime() {
        return firstQueryTime;
    }

    public void setFirstQueryTime(Date firstQueryTime) {
        this.firstQueryTime = firstQueryTime;
    }

    public Date getLastQueryTime() {
        return lastQueryTime;
    }

    public void setLastQueryTime(Date lastQueryTime) {
        this.lastQueryTime = lastQueryTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCodeAcc() {
        return codeAcc;
    }

    public void setCodeAcc(String codeAcc) {
        this.codeAcc = codeAcc;
    }

    public String getOnceTemplateContent() {
        return onceTemplateContent;
    }

    public void setOnceTemplateContent(String onceTemplateContent) {
        this.onceTemplateContent = onceTemplateContent;
    }

    public String getMoreTemplateContent() {
        return moreTemplateContent;
    }

    public void setMoreTemplateContent(String moreTemplateContent) {
        this.moreTemplateContent = moreTemplateContent;
    }

    public String getOnceContent() {
        return onceContent;
    }

    public void setOnceContent(String onceContent) {
        this.onceContent = onceContent;
    }

    public String getMoreContent() {
        return moreContent;
    }

    public void setMoreContent(String moreContent) {
        this.moreContent = moreContent;
    }
}
