package com.ztl.gym.storage.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 对接ERP主对象 t_erp
 *
 * @author ruoyi
 * @date 2022-06-11
 */
public class Erp
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 出库单据号 */
    private Long companyId;

    /** 出库单据号 */
    private String erpOutNo;

    /** 经销商主键 */
    private Long deptId;

    /** 经销商编号 */
    private String deptNo;

    /** 经销商名称 */
    private String deptName;

    /** 仓库名称 */
    private String storageName;

    /** 出库组织 */
    private String postName;


    /** 出库日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outTime;

    /** 状态（0，单据未完成，1，单据已完成） */
    private Long status;

    /** 创建人 */
    private String createUser;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /** 对接出库单明细 */
    private List<ErpDetail> outProductList;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd" )
    private Date beginTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setErpOutNo(String erpOutNo)
    {
        this.erpOutNo = erpOutNo;
    }

    public String getErpOutNo()
    {
        return erpOutNo;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setDeptNo(String deptNo)
    {
        this.deptNo = deptNo;
    }

    public String getDeptNo()
    {
        return deptNo;
    }
    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDeptName()
    {
        return deptName;
    }
    public void setOutTime(Date outTime)
    {
        this.outTime = outTime;
    }

    public Date getOutTime()
    {
        return outTime;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public String getCreateUser()
    {
        return createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<ErpDetail> getOutProductList() {
        return outProductList;
    }

    public void setOutProductList(List<ErpDetail> outProductList) {
        this.outProductList = outProductList;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

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

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }
}
