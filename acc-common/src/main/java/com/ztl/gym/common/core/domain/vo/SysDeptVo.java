package com.ztl.gym.common.core.domain.vo;

import com.ztl.gym.common.annotation.Excel;

public class SysDeptVo {

    /** 父部门ID  1:直属  2：自营 */
    @Excel(name = "企业类型")
    private String deptType;

    /** 父部门名称 */
    @Excel(name = "父级名称")
    private String parentName;

    /** 部门名称 */
    @Excel(name = "名称")
    private String deptName;

    /** 级别 */
    @Excel(name = "级别")
    private String deptLevel;

    /** 负责人 */
    @Excel(name = "负责人")
    private String leader;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 部门状态:0正常,1停用 */
    @Excel(name = "状态")
    private String status;

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptLevel() {
        return deptLevel;
    }

    public void setDeptLevel(String deptLevel) {
        this.deptLevel = deptLevel;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
