package com.ztl.gym.idis.model.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdisBatchResult implements Serializable {
    public final static int SUCCESS_CODE = 1;
    private String handle;
    private Integer code;
    private String msg;
}
