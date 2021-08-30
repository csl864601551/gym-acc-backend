package com.ztl.gym.idis.model.result;

import lombok.Data;

import java.util.List;

/**
 * IDIS 查询结果
 */
@Data
public class IdisIdentityDetail {

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 标识
     */
    private String dataHandle;

    /**
     * 模板版本
     */
    private String templateVersion;

    /**
     * 标识属性
     */
    private List<ValueItem> value;

    @Data
    public static class ValueItem {

        /**
         * 权限码
         */
        private String auth;

        /**
         * 标识属性索引
         */
        private Integer index;

        /**
         * 标识属性内容
         */
        private ValueData data;
    }

    @Data
    public static class ValueData {

        /**
         * 标识属性内容格式（现只支持 string）
         */
        private String format;

        /**
         * 标识属性内容值
         */
        private String value;

        /**
         * 标识属性类型
         */
        private String type;

    }

}
