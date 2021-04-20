package com.ztl.gym.code.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.domain.ProductBatch;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 码 对象 t_code
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public class Code extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    private Long codeIndex;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 状态
     */
    private int status;

    /**
     * 码
     */
    @Excel(name = "码")
    private String code;

    /**
     * 防窜码
     */
    @Excel(name = "防窜码")
    private String codeAcc;

    /**
     * 码类型（箱码or单码）
     */
    private String codeType;

    /**
     * 所属箱码
     */
    @Excel(name = "所属箱码")
    private String pCode;

    /**
     * 码属性id
     */
//    @Excel(name = "码属性id")
    private Long codeAttrId;

    /*---------------------- 冗余字段 ----------------------*/
    @Excel(name = "码状态")
    private String statusName;
    @Excel(name = "码类型")
    private String codeTypeName;
    /**
     * 码属性
     */
    private CodeAttr codeAttr;

    public void setCodeIndex(Long codeIndex) {
        this.codeIndex = codeIndex;
    }

    public Long getCodeIndex() {
        return codeIndex;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCodeAcc(String codeAcc) {
        this.codeAcc = codeAcc;
    }

    public String getCodeAcc() {
        return codeAcc;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getpCode() {
        return pCode;
    }

    public void setCodeAttrId(Long codeAttrId) {
        this.codeAttrId = codeAttrId;
    }

    public Long getCodeAttrId() {
        return codeAttrId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCodeTypeName() {
        return codeTypeName;
    }

    public void setCodeTypeName(String codeTypeName) {
        this.codeTypeName = codeTypeName;
    }

    public CodeAttr getCodeAttr() {
        return codeAttr;
    }

    public void setCodeAttr(CodeAttr codeAttr) {
        this.codeAttr = codeAttr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("codeIndex", getCodeIndex())
                .append("companyId", getCompanyId())
                .append("status", getStatus())
                .append("code", getCode())
                .append("codeAcc", getCodeAcc())
                .append("codeType", getCodeType())
                .append("pCode", getpCode())
                .append("codeAttrId", getCodeAttrId())
                .toString();
    }
}
