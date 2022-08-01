package com.ztl.gym.common.core.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 获取access token 的响应
 */
@Data
public class AccessTokenResponse {

    /**
     * 用于获取该用户信息的 token
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * access_token 的有效期
     */
    @JsonProperty("expires_in")
    private Long expiresIn;

    /**
     * token 的 scope
     */
    @JsonProperty("scope")
    private String scope;

}
