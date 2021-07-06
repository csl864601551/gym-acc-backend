package com.ztl.gym.common.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 安卓版本信息对象 t_android_version
 *
 * @author ruoyi
 * @date 2021-07-06
 */
public class AndroidVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 版本号数字 */
    @Excel(name = "版本号数字")
    private Integer version;

    /** 最新版本号 */
    @Excel(name = "最新版本号")
    private String versionName;

    /** 更新描述 */
    @Excel(name = "更新描述")
    private String updateDescription;

    /** apk下载地址 */
    @Excel(name = "apk下载地址")
    private String apkUrl;

    /** 是否更新 */
    @Excel(name = "是否更新")
    private boolean isUpdate;

    /** 是否强制更新 */
    @Excel(name = "是否强制更新")
    private boolean forceUpdate;


    /** 终端类型 */
    private String clientType;

    /** 文件大小 */
    private String fileSize;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }

    public String getVersionName()
    {
        return versionName;
    }
    public void setUpdateDescription(String updateDescription)
    {
        this.updateDescription = updateDescription;
    }

    public String getUpdateDescription()
    {
        return updateDescription;
    }
    public void setApkUrl(String apkUrl)
    {
        this.apkUrl = apkUrl;
    }

    public String getApkUrl()
    {
        return apkUrl;
    }
    public void setIsUpdate(boolean isUpdate)
    {
        this.isUpdate = isUpdate;
    }

    public boolean getIsUpdate()
    {
        return isUpdate;
    }
    public void setForceUpdate(boolean forceUpdate)
    {
        this.forceUpdate = forceUpdate;
    }

    public boolean getForceUpdate()
    {
        return forceUpdate;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("versionName", getVersionName())
            .append("updateDescription", getUpdateDescription())
            .append("apkUrl", getApkUrl())
            .append("isUpdate", getIsUpdate())
            .append("forceUpdate", getForceUpdate())
            .append("createTime", getCreateTime())
            .toString();
    }
}
