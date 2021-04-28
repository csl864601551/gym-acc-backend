package com.ztl.gym.mix.domain;

import com.ztl.gym.common.annotation.Excel;
import com.ztl.gym.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 窜货记录对象 t_mix_record
 *
 * @author ruoyi
 * @date 2021-04-28
 */
public class MixRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long companyId;

    /** 码 */
    @Excel(name = "码")
    private String code;

    /** 窜货经销商id */
    @Excel(name = "窜货经销商id")
    private Long tenantId;

    /** 产品id */
    @Excel(name = "产品id")
    private Long productId;

    /** 产品批次id */
    @Excel(name = "产品批次id")
    private Long batchId;

    /** 原省 */
    @Excel(name = "原省")
    private String provinceOld;

    /** 原市 */
    @Excel(name = "原市")
    private String cityOld;

    /** 原区 */
    @Excel(name = "原区")
    private String areaOld;

    /** 窜货省 */
    @Excel(name = "窜货省")
    private String provinceMix;

    /** 窜货市 */
    @Excel(name = "窜货市")
    private String cityMix;

    /** 窜货区 */
    @Excel(name = "窜货区")
    private String areaMix;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createUser;

    /** 更新人 */
    @Excel(name = "更新人")
    private Long updateUser;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCompanyId(Long companyId)
    {
        this.companyId = companyId;
    }

    public Long getCompanyId()
    {
        return companyId;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }
    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getProductId()
    {
        return productId;
    }
    public void setBatchId(Long batchId)
    {
        this.batchId = batchId;
    }

    public Long getBatchId()
    {
        return batchId;
    }
    public void setProvinceOld(String provinceOld)
    {
        this.provinceOld = provinceOld;
    }

    public String getProvinceOld()
    {
        return provinceOld;
    }
    public void setCityOld(String cityOld)
    {
        this.cityOld = cityOld;
    }

    public String getCityOld()
    {
        return cityOld;
    }
    public void setAreaOld(String areaOld)
    {
        this.areaOld = areaOld;
    }

    public String getAreaOld()
    {
        return areaOld;
    }
    public void setProvinceMix(String provinceMix)
    {
        this.provinceMix = provinceMix;
    }

    public String getProvinceMix()
    {
        return provinceMix;
    }
    public void setCityMix(String cityMix)
    {
        this.cityMix = cityMix;
    }

    public String getCityMix()
    {
        return cityMix;
    }
    public void setAreaMix(String areaMix)
    {
        this.areaMix = areaMix;
    }

    public String getAreaMix()
    {
        return areaMix;
    }
    public void setCreateUser(Long createUser)
    {
        this.createUser = createUser;
    }

    public Long getCreateUser()
    {
        return createUser;
    }
    public void setUpdateUser(Long updateUser)
    {
        this.updateUser = updateUser;
    }

    public Long getUpdateUser()
    {
        return updateUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("code", getCode())
            .append("tenantId", getTenantId())
            .append("productId", getProductId())
            .append("batchId", getBatchId())
            .append("provinceOld", getProvinceOld())
            .append("cityOld", getCityOld())
            .append("areaOld", getAreaOld())
            .append("provinceMix", getProvinceMix())
            .append("cityMix", getCityMix())
            .append("areaMix", getAreaMix())
            .append("createUser", getCreateUser())
            .append("createTime", getCreateTime())
            .append("updateUser", getUpdateUser())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
