package com.ztl.gym.code.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 生码记录对象 t_code_record
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public class CodeRule {
    private static final long serialVersionUID = 1L;


    /** 生码记录ID */
    private Long codeSingleId;

    /** 成品物料码 */
    private String codeNo;

    /** 生产日期 */
    private String codeDate;

    /** 线别 */
    private String lineNo;

    /** 流水码 */
    private String indexNo;

    /** 工厂代码 */
    private String factoryNo;

    public Long getCodeSingleId() {
        return codeSingleId;
    }

    public void setCodeSingleId(Long codeSingleId) {
        this.codeSingleId = codeSingleId;
    }

    public String getCodeNo() {
        return codeNo;
    }

    public void setCodeNo(String codeNo) {
        this.codeNo = codeNo;
    }

    public String getCodeDate() {
        return codeDate;
    }

    public void setCodeDate(String codeDate) {
        this.codeDate = codeDate;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(String indexNo) {
        this.indexNo = indexNo;
    }

    public String getFactoryNo() {
        return factoryNo;
    }

    public void setFactoryNo(String factoryNo) {
        this.factoryNo = factoryNo;
    }
}
