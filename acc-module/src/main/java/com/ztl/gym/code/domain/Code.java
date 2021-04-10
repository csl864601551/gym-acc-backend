package com.ztl.gym.code.domain;

public class Code {
    private long codeIndex;
    private long companyId;
    private int status;
    private int codeType;
    private String code;
    private String pCode;
    private String codeAcc;
    private long codeAttrId;

    public long getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(long codeIndex) {
        this.codeIndex = codeIndex;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCodeType() {
        return codeType;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
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

    public String getCodeAcc() {
        return codeAcc;
    }

    public void setCodeAcc(String codeAcc) {
        this.codeAcc = codeAcc;
    }

    public long getCodeAttrId() {
        return codeAttrId;
    }

    public void setCodeAttrId(long codeAttrId) {
        this.codeAttrId = codeAttrId;
    }
}
