package com.ztl.gym.system.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * config对象 t_weixin_config
 *
 * @author ruoyi
 * @date 2021-06-17
 */
public class TWeixinConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 企业id
     */
    @Excel(name = "企业id")
    private Long companyId;

    /**
     * 企业名称
     */
    @Excel(name = "企业名称")
    private String companyName;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Long status;

    /**
     * ip
     */
    @Excel(name = "ip")
    private String ip;

    /**
     * 端口
     */
    @Excel(name = "端口")
    private String port;

    /**
     * 域名
     */
    @Excel(name = "域名")
    private String domain;

    /**
     * 微信公众号名称
     */
    @Excel(name = "微信公众号名称")
    private String weixinName;

    /**
     * APPID
     */
    @Excel(name = "APPID")
    private String appid;

    /**
     * appSecret
     */
    @Excel(name = "appSecret")
    private String appsecret;

    /**
     * token
     */
    @Excel(name = "token")
    private String token;

    /**
     * aeskey
     */
    @Excel(name = "aeskey")
    private String aeskey;

    /**
     * 创建者
     */
    @Excel(name = "创建者")
    private Long createUser;

    /**
     * 创建者
     */
    @Excel(name = "创建者")
    private String createUserName;

    /**
     * 修改人
     */
    @Excel(name = "修改人")
    private Long updateUser;

    /**
     * 修改人
     */
    @Excel(name = "修改人")
    private String updateUserName;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStatus() {
        return status;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setWeixinName(String weixinName) {
        this.weixinName = weixinName;
    }

    public String getWeixinName() {
        return weixinName;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey;
    }

    public String getAeskey() {
        return aeskey;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Long getUpdateUser() {
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
                .append("companyName", getCompanyName())
                .append("status", getStatus())
                .append("ip", getIp())
                .append("port", getPort())
                .append("domain", getDomain())
                .append("weixinName", getWeixinName())
                .append("appid", getAppid())
                .append("appsecret", getAppsecret())
                .append("token", getToken())
                .append("aeskey", getAeskey())
                .append("remark", getRemark())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .append("createUserName", getCreateUserName())
                .append("updateUserName", getUpdateUserName())
                .toString();
    }
}
