package com.ztl.gym.idis.domain.vo;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * idis标识码属性值
 *
 * @author zt_sly
 * @date 2021-07-21
 */
@Data
@RequiredArgsConstructor
public class IdisCodeParamValue {

    private String format = "string";

    @NonNull
    private String value;

}
