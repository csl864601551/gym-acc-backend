package com.ztl.gym.idis.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description: IDIS 常量
 * @Author husl
 * @Date 2019/10/31 11:13
 * @Version v1.0
 */
@Component
public class IDISConst {

    /**
     * 用户名
     */
//    public static final String USERNAME = "大同二级节点";
//    public static final String USERNAME = "ztthl";

    /**
     * 密码
     */
//    public static final String PASSWORD = "bin8848bin";
//    public static final String PASSWORD = "Chinaztt12345!";
//    public static final String urlPrefix = "http://121.30.221.52:1980";

    private static String urlPrefix;
    /**
     * 用户认证
     */
    public static String URL_TOKEN;

    /**
     * 标识查询
     */
    public static String URL_DATA_DETAIL;

    /**
     * 标识添加
     */
    public static String URL_DATA_ADD;
    /**
     * 批量标识添加
     */
    public static String URL_DATA_ADD_BATCH;

    /**
     * 标识更新
     */
    public static String URL_DATA_UPDATE;

    /**
     * 保存数据模板
     */
    public static String URL_TEMPLATE_ADD;

    /**
     * 删除数据模板
     */
    public static String URL_TEMPLATE_DELETE;

    /**
     * 据模板
     */
    public static String URL_TEMPLATE_DETAIL;

    /**
     * 查询标识信息接口
     */
    public static String URL_IDENTITY_QUERY;

    /**
     * 企业信息同步接口
     */
    public static String URL_ENT_APPLY;

    @Value("${idis.url.prefix}")
    public void setUrlPrefix(String prefix) {
        urlPrefix = prefix;
        URL_TOKEN = urlPrefix + "/identity/token";
        URL_DATA_DETAIL = urlPrefix + "/identityv2/data/detail";
        URL_DATA_ADD = urlPrefix + "/identityv2/data";
        URL_DATA_ADD_BATCH = urlPrefix + "/identityv2/data/batchCreate";
        URL_DATA_UPDATE = urlPrefix + "/identityv2/data";
        URL_TEMPLATE_ADD = urlPrefix + "/snms/api/template/v1";
        URL_TEMPLATE_DELETE = urlPrefix + "/snms/api/template/v1";
        URL_TEMPLATE_DETAIL = urlPrefix + "/snms/api/template/v1";
        URL_IDENTITY_QUERY = urlPrefix + "/api/identityv2/query/data?handle=s%";
        URL_ENT_APPLY = urlPrefix + "/snms/api/ent/apply";
    }






    /**
     * 默认模板版本
     */
    public static final String IDENTITY_TEMPLATE_VERSION = "OneCodeDefault";

    /**
     * 默认数据类型
     */
    public static final String DATE_TYPE_DEFAULT = "string";

    /**
     * 模板中index与type分割符
     */
    public static final String SPLITE_INDEX_TYPE = "---";

    /**
     * session 中保存idis token 的名称.
     */
    public static final String CACHE_KEY_TOKEN = "idis-token";

    /**
     * session 中保存的 idis token 的过期时间
     */
    public static final String CACHE_KEY_TOKEN_SET_TIME = "idis-token-expire-time";

}
