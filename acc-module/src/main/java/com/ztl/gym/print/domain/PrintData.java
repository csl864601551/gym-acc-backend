package com.ztl.gym.print.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 产量统计对象 t_print_data
 *
 * @author ruoyi
 * @date 2022-06-28
 */
public class PrintData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 企业Id */
    private Long companyId;

    /** 箱码 */
    private String boxCode;

    /** 流水号 */
    private Long codeIndex;

    /** 打印状态（0：未打印，1：已打印） */
    private Integer printStatus;

    /** 产线 */
    @Excel(name = "产线")
    private String productLine;

    /** 箱数量 */
    private Long boxNum;

    /** 产品 */
    @Excel(name = "产品")
    private String productName;

    /** 型号 */
    @Excel(name = "型号")
    private String productModel;

    /** 批次 */
    private String batchName;

    /** 生产日期 */
    private String produceDate;

    /** 装箱数 */
    private Long codeCount;

    /** 毛重 */
    private String grossWeight;

    /** 净重 */
    private String netWeight;

    /** 定货号 */
    private String orderNo;

    /** 条形码 */
    private String barCode;

    /** 产量 */
    @Excel(name = "产量")
    private Long produceCount;

    /** 实际生产日期 */
    @Excel(name = "生产日期")
    private String produceFactDate;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCompanyId(Long companyId)
    {
        this.companyId = companyId;
    }

    public Long getCompanyId()
    {
        return companyId;
    }
    public void setBoxCode(String boxCode)
    {
        this.boxCode = boxCode;
    }

    public String getBoxCode()
    {
        return boxCode;
    }
    public void setCodeIndex(Long codeIndex)
    {
        this.codeIndex = codeIndex;
    }

    public Long getCodeIndex()
    {
        return codeIndex;
    }
    public void setPrintStatus(Integer printStatus)
    {
        this.printStatus = printStatus;
    }

    public Integer getPrintStatus()
    {
        return printStatus;
    }
    public void setProductLine(String productLine)
    {
        this.productLine = productLine;
    }

    public String getProductLine()
    {
        return productLine;
    }
    public void setBoxNum(Long boxNum)
    {
        this.boxNum = boxNum;
    }

    public Long getBoxNum()
    {
        return boxNum;
    }
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductName()
    {
        return productName;
    }
    public void setProductModel(String productModel)
    {
        this.productModel = productModel;
    }

    public String getProductModel()
    {
        return productModel;
    }
    public void setBatchName(String batchName)
    {
        this.batchName = batchName;
    }

    public String getBatchName()
    {
        return batchName;
    }
    public void setProduceDate(String produceDate)
    {
        this.produceDate = produceDate;
    }

    public String getProduceDate()
    {
        return produceDate;
    }
    public void setCodeCount(Long codeCount)
    {
        this.codeCount = codeCount;
    }

    public Long getCodeCount()
    {
        return codeCount;
    }
    public void setGrossWeight(String grossWeight)
    {
        this.grossWeight = grossWeight;
    }

    public String getGrossWeight()
    {
        return grossWeight;
    }
    public void setNetWeight(String netWeight)
    {
        this.netWeight = netWeight;
    }

    public String getNetWeight()
    {
        return netWeight;
    }
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }
    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
    }

    public String getBarCode()
    {
        return barCode;
    }
    public Long getProduceCount() {
        return produceCount;
    }

    public void setProduceCount(Long produceCount) {
        this.produceCount = produceCount;
    }
    public String getProduceFactDate() {
        return produceFactDate;
    }

    public void setProduceFactDate(String produceFactDate) {
        this.produceFactDate = produceFactDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("companyId", getCompanyId())
                .append("boxCode", getBoxCode())
                .append("codeIndex", getCodeIndex())
                .append("printStatus", getPrintStatus())
                .append("productLine", getProductLine())
                .append("boxNum", getBoxNum())
                .append("productName", getProductName())
                .append("productModel", getProductModel())
                .append("batchName", getBatchName())
                .append("produceDate", getProduceDate())
                .append("codeCount", getCodeCount())
                .append("grossWeight", getGrossWeight())
                .append("netWeight", getNetWeight())
                .append("orderNo", getOrderNo())
                .append("barCode", getBarCode())
                .append("createTime", getCreateTime())
                .toString();
    }
}
