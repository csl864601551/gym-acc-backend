package com.ztl.gym.idis.model.result;

import lombok.Data;

import java.util.List;

/**
 * idis template 查询接口返回的的值
 */
@Data
public class IdisTemplateDetail {

    private Long id;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 模板版本
     */
    private String version;

    /**
     * 描述
     */
    private String description;

    /**
     * 模板内容
     */
    private List<IdisTemplateItemDetail> items;

}
