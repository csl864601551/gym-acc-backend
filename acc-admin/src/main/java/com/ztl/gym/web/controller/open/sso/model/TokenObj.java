package com.ztl.gym.web.controller.open.sso.model;

import java.io.Serializable;


public class TokenObj implements Serializable {
    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
