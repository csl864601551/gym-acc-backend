package com.ztl.gym.print.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PrintData extends BaseEntity {

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long companyId;

    /** 箱码 */
    @Excel(name = "箱码")
    private String boxCode;

    /** 流水号 */
    @Excel(name = "流水号")
    private Long codeIndex;

    /** 打印状态 */
    @Excel(name = "打印状态（0：未打印，1：已打印）")
    private int printStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public Long getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(Long codeIndex) {
        this.codeIndex = codeIndex;
    }

    public int getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(int printStatus) {
        this.printStatus = printStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("boxCode", getBoxCode())
                .append("codeIndex", getCodeIndex())
                .append("printStatus", getPrintStatus())
                .toString();
    }
}
