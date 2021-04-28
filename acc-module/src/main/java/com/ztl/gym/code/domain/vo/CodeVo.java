package com.ztl.gym.code.domain.vo;

import com.ztl.gym.code.domain.Code;

public class CodeVo {
    private String codeStr;
    private Code code;

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getCodeStr() {
        return codeStr;
    }

    public void setCodeStr(String codeStr) {
        this.codeStr = codeStr;
    }
}
