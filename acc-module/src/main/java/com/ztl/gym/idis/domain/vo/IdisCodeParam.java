package com.ztl.gym.idis.domain.vo;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * idis标识码属性
 *
 * @author zt_sly
 * @date 2021-07-21
 */
@Data
@RequiredArgsConstructor
public class IdisCodeParam {

    private String index = "0";

    @NonNull
    private String type;

    @NonNull
    private IdisCodeParamValue data;

}
