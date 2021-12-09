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

    /** 箱号 */
    @Excel(name = "箱号")
    private String boxNum;

    /** 流水号 */
    @Excel(name = "流水号")
    private Long codeIndex;

    /** 打印状态 */
    @Excel(name = "打印状态（0：未打印，1：已打印）")
    private int printStatus;

    /** 产线 */
    @Excel(name = "产线")
    private String productLine;

    /** 产品 */
    @Excel(name = "产品")
    private String productName;

    /** 型号 */
    @Excel(name = "型号")
    private String productModel;

    /** 批次 */
    @Excel(name = "批次")
    private String batchName;

    /** 生产日期 */
    @Excel(name = "生产日期")
    private String produceDate;

    /** 装箱数 */
    @Excel(name = "装箱数")
    private String codeCount;

    /** 毛重 */
    @Excel(name = "毛重")
    private String grossWeight;

    /** 净重 */
    @Excel(name = "净重")
    private String netWeight;

    /** 定货号 */
    @Excel(name = "定货号")
    private String orderNo;

    /** 条形码 */
    @Excel(name = "条形码")
    private String barCode;

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

    public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum;
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

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(String produceDate) {
        this.produceDate = produceDate;
    }

    public String getCodeCount() {
        return codeCount;
    }

    public void setCodeCount(String codeCount) {
        this.codeCount = codeCount;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("boxCode", getBoxCode())
                .append("boxNum", getBoxNum())
                .append("codeIndex", getCodeIndex())
                .append("printStatus", getPrintStatus())
                .append("productLine", getProductLine())
                .append("productName", getProductName())
                .append("productModel", getProductModel())
                .append("batchName", getBatchName())
                .append("produceDate", getProduceDate())
                .append("codeCount", getCodeCount())
                .append("grossWeight", getGrossWeight())
                .append("netWeight", getNetWeight())
                .append("orderNo", getOrderNo())
                .append("barCode", getBarCode())
                .toString();
    }
}
