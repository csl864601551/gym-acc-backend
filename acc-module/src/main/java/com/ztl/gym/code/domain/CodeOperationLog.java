package com.ztl.gym.code.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 操作日志对象 t_code_operation_log
 *
 * @author ruoyi
 * @date 2021-08-05
 */
public class CodeOperationLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 码
     */
    @Excel(name = "码")
    private String code;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Long count;

    /**
     * 操作类型 0-装箱，1-拆箱，2-补标入箱，3-分离单标
     */
    @Excel(name = "操作类型 0-装箱，1-拆箱，2-补标入箱，3-分离单标")
    private Integer type;

    /**
     * 描述信息
     */
    @Excel(name = "描述信息")
    private String message;

    private Long companyId;

    private String userName;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("code", getCode())
                .append("count", getCount())
                .append("type", getType())
                .append("message", getMessage())
                .append("remark", getRemark())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .append("companyId", getCompanyId())
                .append("userName", getUserName())
                .toString();
    }
}
