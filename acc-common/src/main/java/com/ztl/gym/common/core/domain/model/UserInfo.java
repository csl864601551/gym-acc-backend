package com.ztl.gym.common.core.domain.model;

import lombok.Data;

/**
 * 用于传递用户信息
 */
@Data
public class UserInfo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户登录授权密钥
     */
    private String accessSecurity;

    private String phone;

    private String companyName;

    private String idisPrefix;

    private String companyAddress;

    private String companyAddressDetail;

    private String tenantId;

}
