package com.ztl.gym.idis.model.result;

import lombok.Data;

import java.lang.reflect.Array;

/**
 * IDIS 模板的items (模板内容)
 */
@Data
public class IdisTemplateItemDetail {

    /**
     * 中文名
     */
    private String name;

    /**
     * 英文名称
     */
    private String idType;

    /**
     * ?
     */
    private Integer idIndex;

    /**
     * 是否必须
     */
    private Boolean required;

    /**
     * ?
     */
    private Long metadataId;

    /**
     * ?
     */
    private Object authorizationKey;

    /**
     * 数据限制
     */
    private Array metadata;
}
