package com.ztl.gym.storage.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 出库对象 t_storage_out
 *
 * @author ruoyi
 * @date 2021-04-09
 */
public class StorageOut extends BaseEntity
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

    /** 出库单号 */
    @Excel(name = "出库单号")
    private String out_no;

    /** 产品id */
    @Excel(name = "产品id")
    private Long product_id;

    /** 产品批次 */
    @Excel(name = "产品批次")
    private String batch_no;

    /** 出库数量 */
    @Excel(name = "出库数量")
    private Long out_num;

    /** 实际出库数量 */
    @Excel(name = "实际出库数量")
    private Long act_out_num;

    /** 发货单位 */
    @Excel(name = "发货单位")
    private String storage_from;

    /** 收货单位 */
    @Excel(name = "收货单位")
    private String storage_to;

    /** 出货仓库 */
    @Excel(name = "出货仓库")
    private Long from_storage_id;

    /** 收货仓库 */
    @Excel(name = "收货仓库")
    private Long to_storage_id;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long create_user;

    /** 修改人 */
    @Excel(name = "修改人")
    private Long update_user;
    
    /** 出库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Excel(name = "出库时间", width = 30, dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date out_time;

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

    public String getOut_no() {
        return out_no;
    }

    public void setOut_no(String out_no) {
        this.out_no = out_no;
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

    public Long getOut_num() {
        return out_num;
    }

    public void setOut_num(Long out_num) {
        this.out_num = out_num;
    }

    public Long getAct_out_num() {
        return act_out_num;
    }

    public void setAct_out_num(Long act_out_num) {
        this.act_out_num = act_out_num;
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

    public Long getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(Long update_user) {
        this.update_user = update_user;
    }

    public Date getOut_time() {
        return out_time;
    }

    public void setOut_time(Date out_time) {
        this.out_time = out_time;
    }

    @Override
    public String toString() {
        return "StorageOut{" +
                "id=" + id +
                ", company_id=" + company_id +
                ", tenant_id=" + tenant_id +
                ", status=" + status +
                ", out_no='" + out_no + '\'' +
                ", product_id=" + product_id +
                ", batch_no='" + batch_no + '\'' +
                ", out_num=" + out_num +
                ", act_out_num=" + act_out_num +
                ", storage_from='" + storage_from + '\'' +
                ", storage_to='" + storage_to + '\'' +
                ", from_storage_id=" + from_storage_id +
                ", to_storage_id=" + to_storage_id +
                ", create_user=" + create_user +
                ", update_user=" + update_user +
                ", out_time=" + out_time +
                '}';
    }
}
