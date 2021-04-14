package com.ztl.gym.product.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 产品批次对象 t_product_batch
 *
 * @author ruoyi
 * @date 2021-04-14
 */
public class ProductBatch extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long company_id;

    /** 编号 */
    @Excel(name = "编号")
    private String batch_no;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 批次标题 */
    @Excel(name = "批次标题")
    private String batch_title;

    /** 批次日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "批次日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date batch_date;

    /** 产品id */
    @Excel(name = "产品id")
    private Long product_id;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long create_user;

    /** 更新人 */
    @Excel(name = "更新人")
    private Long update_user;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getBatch_title() {
        return batch_title;
    }

    public void setBatch_title(String batch_title) {
        this.batch_title = batch_title;
    }

    public Date getBatch_date() {
        return batch_date;
    }

    public void setBatch_date(Date batch_date) {
        this.batch_date = batch_date;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
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


    @Override
    public String toString() {
        return "ProductBatch{" +
                "id=" + id +
                ", company_id=" + company_id +
                ", batch_no='" + batch_no + '\'' +
                ", status=" + status +
                ", batch_title='" + batch_title + '\'' +
                ", batch_date=" + batch_date +
                ", product_id=" + product_id +
                ", create_user=" + create_user +
                ", update_user=" + update_user +
                '}';
    }
}
