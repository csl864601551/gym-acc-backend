package com.ztl.gym.idis.domain.vo;

import lombok.Data;

/**
 * idis API响应体
 *
 * @author zt_sly
 * @date 2021-07-21
 */
@Data
public class IdisRespBody {

    private String responseCode;

    private String msg;

    private String token;

    public boolean isSuccess() {
        return "1".equals(responseCode);
    }

}
