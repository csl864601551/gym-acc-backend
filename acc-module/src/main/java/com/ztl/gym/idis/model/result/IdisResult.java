package com.ztl.gym.idis.model.result;

import lombok.Data;
import org.springframework.util.ObjectUtils;

/**
 * IDIS 响应的数据
 */
@Data
public class IdisResult<T> {

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 状态码描述
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    public boolean isSuccess() {
        return status != null && status.equals(1);
    }

    public boolean hasData() {
        return isSuccess() && !ObjectUtils.isEmpty(data);
    }
}
