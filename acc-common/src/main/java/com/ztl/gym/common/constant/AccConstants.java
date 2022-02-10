package com.ztl.gym.common.constant;

/**
 * 业务常量
 */
public class AccConstants {
    public final static String SUCCESS = "SUCCESS";
    public final static String FAILURE = "FAILURE";

    /*---------------------------- 通用常量 ----------------------------*/
    /**
     * 状态-删除
     */
    public final static int STATUS_DELETE = 0;
    /**
     * 状态-正常
     */
    public final static int STATUS_NORMAL = 1;

    /**
     * 文本类型-未知
     */
    public final static int NO_TYPE_UNKONW = 0;
    /**
     * 文本类型-码-箱码
     */
    public final static int NO_TYPE_CODE_P = 1;
    /**
     * 文本类型-码-单码
     */
    public final static int NO_TYPE_CODE_S = 2;
    /**
     * 文本类型-物流单号-入库
     */
    public final static int NO_TYPE_STORAGE_IN = 3;
    /**
     * 文本类型-物流单号-出库
     */
    public final static int NO_TYPE_STORAGE_OUT = 4;
    /**
     * 文本类型-物流单号-退货
     */
    public final static int NO_TYPE_STORAGE_BACK = 5;
    /**
     * 文本类型-物流单号-调拨
     */
    public final static int NO_TYPE_STORAGE_TRANSFER = 6;

    /**
     * 扫码用户
     */
    public final static Long WEIXIN_ADMIN_ID = 2L;


    /*---------------------------- 生码常量 ----------------------------*/
    /**
     * 码类型-单码
     */
    public final static String CODE_TYPE_SINGLE = "single";
    /**
     * 码类型-箱码
     */
    public final static String CODE_TYPE_BOX = "box";
    /**
     * 码类型-错误类型
     */
    public final static String CODE_TYPE_ERROR = "error";

    /**
     * 码状态-未赋值
     */
    public final static int CODE_STATUS_WAIT = 0;
    /**
     * 码状态-已赋值
     */
    public final static int CODE_STATUS_FINISH = 1;
    /**
     * 码状态-已成功同步至企业节点
     */
    public final static int CODE_STATUS_SYNCED = 2;

    /*---------------------------- 生码记录常量 ----------------------------*/
    /**
     * 生码记录状态-生码中
     */
    public final static int CODE_RECORD_STATUS_WAIT = 0;
    /**
     * 生码记录状态-生码完成，待赋值
     */
    public final static int CODE_RECORD_STATUS_FINISH = 1;
    /**
     * 生码记录状态-已赋值
     */
    public final static int CODE_RECORD_STATUS_EVA = 2;

    /**
     * 生码类型-普通生码
     */
    public final static int GEN_CODE_TYPE_SINGLE = 1;
    /**
     * 生码类型-套标生码
     */
    public final static int GEN_CODE_TYPE_BOX = 2;
    /**
     * 生码类型-防伪码生码
     */
    public final static int GEN_CODE_TYPE_ACC = 3;

    /*---------------------------- 仓库库存 ----------------------------*/
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

    /**
     * 产品库存变动类型-新增
     */
    public final static int STOCK_TYPE_ADD = 1;
    /**
     * 产品库存变动类型-减少
     */
    public final static int STOCK_TYPE_REDUCE = 2;

    /**
     * 仓库级别-企业
     */
    public final static int STOCK_LEVEL_COMPANY = 10;
    /**
     * 仓库级别-经销商
     */
    public final static int STOCK_LEVEL_TENANT = 20;

    /*---------------------------- 部门常量 ----------------------------*/
    /**
     * 平台ID
     */
    public final static Long ADMIN_DEPT_ID = 100L;

    /**
     * 部门状态-正常
     */
    public final static String DEPT_STATUS_NORMAL = "0";
    /**
     * 部门状态-停用
     */
    public final static String DEPT_STATUS_STOP = "1";

    /**
     * 部门类型-直属
     */
    public final static int DEPT_TYPE_ZS = 1;
    /**
     * 部门类型-自营
     */
    public final static int DEPT_TYPE_ZY = 2;

    /*---------------------------- 物流常量 ----------------------------*/
    /**
     * 物流类型-入库
     */
    public final static int STORAGE_TYPE_IN = 1;
    /**
     * 物流类型-出库
     */
    public final static int STORAGE_TYPE_OUT = 2;
    /**
     * 物流类型-调拨
     */
    public final static int STORAGE_TYPE_TRANSFER = 3;
    /**
     * 物流类型-退货 【退货相当于出库，例：A发货给B B退货】
     */
    public final static int STORAGE_TYPE_BACK = 4;



    /**
     * 调拨状态-待发货
     */
    public final static int STORAGE_TRANSFER_STATUS_WAIT = 0;
    /**
     * 调拨状态-发货中
     */
    public final static int STORAGE_TRANSFER_STATUS_OUT = 0;
    /**
     * 调拨状态-已入库
     */
    public final static int STORAG_TRANSFERE_STATUS_IN = 0;


    /*---------------------------- 角色常量 ----------------------------*/
    /**
     * 企业角色
     */
    public final static long COMPANY_ROLE = 101;

    /**
     * 管理员角色
     */
    public final static long ADMIN_ROLE = 1;


    /**
     * 默认图片
     */
    public final static String DEFAULT_IMAGE = "https://data-ir.obs.cn-east-2.myhuaweicloud.com/acc/image/202202/1491698876321763328.png";



}
