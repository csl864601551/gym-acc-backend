package com.ztl.gym.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;

/**
 * 【请填写功能名称】对象 t_code_sequence_new
 *
 * @author ruoyi
 * @date 2022-03-22
 */
public class CodeSequenceNew
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 序列的名字 */
    @Excel(name = "序列的名字")
    private Long companyId;

    /** 序列的当前值 */
    @Excel(name = "序列的当前值")
    private Long currentValue;

    /** 序列的自增值(流水码) */
    @Excel(name = "序列的自增值")
    private Long increment;

    /** 成品物料码 */
    @Excel(name = "成品物料码")
    private String codeNo;

    /** 生产日期 */
    private String codeDate;

    /** 线别 */
    @Excel(name = "线别")
    private String lineNo;

    /** 工厂代码 */
    @Excel(name = "工厂代码")
    private String factoryNo;

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
    public void setCurrentValue(Long currentValue)
    {
        this.currentValue = currentValue;
    }

    public Long getCurrentValue()
    {
        return currentValue;
    }
    public void setIncrement(Long increment)
    {
        this.increment = increment;
    }

    public Long getIncrement()
    {
        return increment;
    }
    public void setCodeNo(String codeNo)
    {
        this.codeNo = codeNo;
    }

    public String getCodeNo()
    {
        return codeNo;
    }
    public void setCodeDate(String codeDate)
    {
        this.codeDate = codeDate;
    }

    public String getCodeDate()
    {
        return codeDate;
    }
    public void setLineNo(String lineNo)
    {
        this.lineNo = lineNo;
    }

    public String getLineNo()
    {
        return lineNo;
    }

    public void setFactoryNo(String factoryNo)
    {
        this.factoryNo = factoryNo;
    }

    public String getFactoryNo()
    {
        return factoryNo;
    }


}
