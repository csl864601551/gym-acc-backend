package com.ztl.gym.web.controller.open.sso.model;

import lombok.Data;

import java.io.Serializable;

@Data
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
}
