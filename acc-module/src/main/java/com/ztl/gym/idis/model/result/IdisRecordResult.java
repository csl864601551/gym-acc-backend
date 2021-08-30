package com.ztl.gym.idis.model.result;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * IDIS日志记录表 
 * </p>
 *
 * @author gull
 * @since 2020-07-02
 */
@Data
public class IdisRecordResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 业务接口类型
     */
    private String type;

    /**
     * 请求链接
     */
    private String url;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求返回码
     */
    private String code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

}
