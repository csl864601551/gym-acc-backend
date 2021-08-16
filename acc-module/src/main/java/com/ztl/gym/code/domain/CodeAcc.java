package com.ztl.gym.code.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 码 对象 t_code
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public class CodeAcc extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 企业ID
     */
    private Long companyId;


    /**
     * 码
     */
    //@Excel(name = "码")
    private String code;

    /**
     * 防伪码
     */
    @Excel(name = "防伪码")
    private String codeAcc;

    /**
     * 查询次数
     */
    //@Excel(name = "查询次数")
    private Long queryCount;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 生码记录id
     */
    private Long recordId;

    /**
     * 创建时间
     */
    private Date createTime;

    @Excel(name = "码类型")
    private String codeTypeName;


    public String getCodeTypeName() {
        return codeTypeName;
    }

    public void setCodeTypeName(String codeTypeName) {
        this.codeTypeName = codeTypeName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public Long getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(Long queryCount) {
        this.queryCount = queryCount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
