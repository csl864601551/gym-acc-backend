package com.ztl.gym.common.constant;

/**
 * 业务常量
 */
public class AccConstants {
    /*---------------------------- 通用常量 ----------------------------*/
    /**
     * 状态-删除
     */
    public final static int STATUS_DELETE = 0;
    /**
     * 状态-正常
     */
    public final static int STATUS_NORMAL = 1;


    /*---------------------------- 企业常量 ----------------------------*/
    /**
     * 平台部门id
     */
    public final static Long TOP_DEPT_ID = 100l;


    /*---------------------------- 用户常量 ----------------------------*/
    /**
     * 用户类型-系统
     */
    public final static String USER_TYPE_SYSTEM = "00";
    /**
     * 用户类型-企业
     */
    public final static String USER_TYPE_COMPANY = "10";
    /**
     * 用户类型-经销商
     */
    public final static String USER_TYPE_TENANT = "20";
    /**
     * 用户类型-经销商下级
     */
    public final static String USER_TYPE_TENANT_CUSTOM = "30";


    /*---------------------------- 生码常量 ----------------------------*/
    /**
     * 单码
     */
    public final static int CODE_TYPE_SINGLE = 1;
    /**
     * 箱码
     */
    public final static int CODE_TYPE_BOX = 2;

    /*---------------------------- 仓库 ----------------------------*/
    /**
     * 仓库级别-企业
     */
    public final static Long STORAGE_LEVEL_COMPANY = 10L;
    /**
     * 仓库级别-经销商
     */
    public final static Long STORAGE_LEVEL_TENANT = 20L;
    /**
     * 仓库状态-正常
     */
    public final static Long STORAGE_DELETE_NO = 0L;
    /**
     * 仓库状态-已删除
     */
    public final static Long STORAGE_DELETE_YSE = 1L;

    /*---------------------------- 平台ID ----------------------------*/
    /**
     * 平台ID
     */
    public final static Long ADMIN_DEPT_ID = 100L;
}
