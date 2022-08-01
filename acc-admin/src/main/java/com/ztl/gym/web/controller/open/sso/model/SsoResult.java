package com.ztl.gym.web.controller.open.sso.model;

import lombok.Data;

/**
 * IDIS 响应的数据
 */
@Data
public class SsoResult<T> {
    private Integer code;
    private String message;
    private T data;
    private Boolean success;


}
