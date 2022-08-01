package com.ztl.gym.common.core.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 获取用户 access token 的请求参数
 */
@Data
public class AccessTokenRequest {

    /**
     * 应用客户端的 id
     */
    @JsonProperty("client_id")
    private String clientId;

    /**
     * 应用客户端的密码
     */
    @JsonProperty("client_secret")
    private String clientSecret;

    /**
     * 表示用户的 code
     */
    @JsonProperty(value = "code")
    private String code;

    /**
     * authorization_code
     */
    @JsonProperty("grant_type")
    private String grantType;

    /**
     * 回调的地址
     */
    @JsonProperty("redirect_uri")
    private String redirectUri;

}
