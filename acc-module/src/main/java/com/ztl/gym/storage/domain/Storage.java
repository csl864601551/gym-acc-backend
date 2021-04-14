package com.ztl.gym.storage.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 仓库对象 t_storage
 *
 * @author zhucl
 * @date 2021-04-13
 */
public class Storage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long companyId;

    /** 级别（企业or经销商） */
    @Excel(name = "级别", readConverterExp = "企=业or经销商")
    private Long level;

    /** 经销商id */
    @Excel(name = "经销商id")
    private Long tenantId;

    /** 仓库编号 */
    @Excel(name = "仓库编号")
    private String storageNo;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String storageName;

    /** 负责人 */
    @Excel(name = "负责人")
    private String director;

    /** 仓库地址 */
    @Excel(name = "仓库地址")
    private String address;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createUser;

    /** 更新人 */
    @Excel(name = "更新人")
    private Long updateUser;

    /** 创建人 */
    @Excel(name = "创建人姓名")
    private String createUserName;

    /** 更新人 */
    @Excel(name = "更新人姓名")
    private String updateUserName;

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
    public void setLevel(Long level)
    {
        this.level = level;
    }

    public Long getLevel()
    {
        return level;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }
    public void setStorageNo(String storageNo)
    {
        this.storageNo = storageNo;
    }

    public String getStorageNo()
    {
        return storageNo;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setStorageName(String storageName)
    {
        this.storageName = storageName;
    }

    public String getStorageName()
    {
        return storageName;
    }
    public void setDirector(String director)
    {
        this.director = director;
    }

    public String getDirector()
    {
        return director;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setCreateUser(Long createUser)
    {
        this.createUser = createUser;
    }

    public Long getCreateUser()
    {
        return createUser;
    }
    public void setUpdateUser(Long updateUser)
    {
        this.updateUser = updateUser;
    }

    public Long getUpdateUser()
    {
        return updateUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("level", getLevel())
            .append("tenantId", getTenantId())
            .append("storageNo", getStorageNo())
            .append("status", getStatus())
            .append("storageName", getStorageName())
            .append("director", getDirector())
            .append("address", getAddress())
            .append("remark", getRemark())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
