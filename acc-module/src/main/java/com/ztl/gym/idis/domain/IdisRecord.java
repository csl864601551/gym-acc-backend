package com.ztl.gym.idis.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import lombok.*;

/**
 * IDIS同步日志对象 t_idis_record
 *
 * @author zt_sly
 * @date 2021-07-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IdisRecord extends BaseEntity {

    /** 主键 */
    private Long id;

    /** 接口类型 */
    @Excel(name = "接口类型")
    private String type;

    /** 标识码 */
    @Excel(name = "标识码")
    private String code;

    /** 请求链接 */
    @Excel(name = "请求链接")
    private String url;

    /** 请求参数 */
    @Excel(name = "请求参数")
    private String param;

    /** 请求返回码 */
    @Excel(name = "请求返回码")
    private String respCode;

    /** 请求返回信息 */
    @Excel(name = "请求返回信息")
    private String respMsg;

}
