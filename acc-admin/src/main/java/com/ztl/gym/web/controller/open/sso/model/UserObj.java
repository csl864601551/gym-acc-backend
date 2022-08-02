package com.ztl.gym.web.controller.open.sso.model;

import java.io.Serializable;

public class UserObj implements Serializable {
    public static String ROLE_OWNER = "owner";

    private String sid;
    /**
     * 账号
     */
    private String sub;
    /**
     * 昵称
     */
    private String nick_name;
    /**
     * 手机
     */
    private String phone_number;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 企业id
     */
    private String org_id;
    /**
     * 企业名称
     */
    private String org_name;
    /**
     * 企业角色
     */
    private String org_role;
    /**
     * 营业执照
     */
    private String license_number;

    public static String getRoleOwner() {
        return ROLE_OWNER;
    }

    public static void setRoleOwner(String roleOwner) {
        ROLE_OWNER = roleOwner;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_role() {
        return org_role;
    }

    public void setOrg_role(String org_role) {
        this.org_role = org_role;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }
}
