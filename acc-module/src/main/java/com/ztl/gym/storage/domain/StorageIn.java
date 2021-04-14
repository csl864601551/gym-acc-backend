package com.ztl.gym.storage.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 入库对象 t_storage_in
 *
 * @author ruoyi
 * @date 2021-04-09
 */
public class StorageIn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long company_id;

    /** 经销商id */
    @Excel(name = "经销商id")
    private Long tenant_id;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 入库单号 */
    @Excel(name = "入库单号")
    private String in_no;

    /** 产品id */
    @Excel(name = "产品id")
    private Long product_id;

    /** 产品批次 */
    @Excel(name = "产品批次")
    private String batch_no;

    /** 入库数量 */
    @Excel(name = "入库数量")
    private Long in_num;

    /** 实际入库数量 */
    @Excel(name = "实际入库数量")
    private Long act_in_num;

    /** 发货单位 */
    @Excel(name = "发货单位")
    private String storage_from;

    /** 收货单位 */
    @Excel(name = "收货单位")
    private String storage_to;

    /** 出库仓库 */
    @Excel(name = "出库仓库")
    private Long from_storage_id;

    /** 入库仓库 */
    @Excel(name = "入库仓库")
    private Long to_storage_id;

    /** 创建人 */
    private Long create_user;


    /** 创建人 */
    @Excel(name = "创建人")
    @TableField(exist = false)
    private String create_user_name;

    /** 修改人 */
    private Long update_user;

    /** 修改人 */
    @Excel(name = "修改人")
    @TableField(exist = false)
    private String update_user_name;
    
    /** 入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Excel(name = "入库时间", width = 30, dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date in_time;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public Long getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(Long tenant_id) {
        this.tenant_id = tenant_id;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getIn_no() {
        return in_no;
    }

    public void setIn_no(String in_no) {
        this.in_no = in_no;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public Long getIn_num() {
        return in_num;
    }

    public void setIn_num(Long in_num) {
        this.in_num = in_num;
    }

    public Long getAct_in_num() {
        return act_in_num;
    }

    public void setAct_in_num(Long act_in_num) {
        this.act_in_num = act_in_num;
    }

    public String getStorage_from() {
        return storage_from;
    }

    public void setStorage_from(String storage_from) {
        this.storage_from = storage_from;
    }

    public String getStorage_to() {
        return storage_to;
    }

    public void setStorage_to(String storage_to) {
        this.storage_to = storage_to;
    }

    public Long getFrom_storage_id() {
        return from_storage_id;
    }

    public void setFrom_storage_id(Long from_storage_id) {
        this.from_storage_id = from_storage_id;
    }

    public Long getTo_storage_id() {
        return to_storage_id;
    }

    public void setTo_storage_id(Long to_storage_id) {
        this.to_storage_id = to_storage_id;
    }

    public Long getCreate_user() {
        return create_user;
    }

    public void setCreate_user(Long create_user) {
        this.create_user = create_user;
    }

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public Long getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(Long update_user) {
        this.update_user = update_user;
    }

    public String getUpdate_user_name() {
        return update_user_name;
    }

    public void setUpdate_user_name(String update_user_name) {
        this.update_user_name = update_user_name;
    }

    public Date getIn_time() {
        return in_time;
    }

    public void setIn_time(Date in_time) {
        this.in_time = in_time;
    }

    @Override
    public String toString() {
        return "StorageIn{" +
                "id=" + id +
                ", company_id=" + company_id +
                ", tenant_id=" + tenant_id +
                ", status=" + status +
                ", in_no='" + in_no + '\'' +
                ", product_id=" + product_id +
                ", batch_no='" + batch_no + '\'' +
                ", in_num=" + in_num +
                ", act_in_num=" + act_in_num +
                ", storage_from='" + storage_from + '\'' +
                ", storage_to='" + storage_to + '\'' +
                ", from_storage_id=" + from_storage_id +
                ", to_storage_id=" + to_storage_id +
                ", create_user=" + create_user +
                ", create_user_name='" + create_user_name + '\'' +
                ", update_user=" + update_user +
                ", update_user_name='" + update_user_name + '\'' +
                ", in_time=" + in_time +
                '}';
    }
}
