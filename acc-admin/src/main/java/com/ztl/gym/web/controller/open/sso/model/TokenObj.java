package com.ztl.gym.web.controller.open.sso.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenObj implements Serializable {
    private String access_token;
}
