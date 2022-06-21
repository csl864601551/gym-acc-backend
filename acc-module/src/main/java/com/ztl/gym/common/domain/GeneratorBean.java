package com.ztl.gym.common.domain;

/**
 * @author : wujinhao
 * @Description: 分布式id
 * @date Date : 2021年08月11日 13:42
 */
public class GeneratorBean {
    private Integer id;
    /**
     * 企业id
     */
    private Long companyId;
    /**
     * 当前最大id
     */
    private Long maxId;
    /**
     * 业务类型
     */
    private Integer type;
    /**
     * 步长
     */
    private Integer step;
    /**
     * 版本号
     */
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getMaxId() {
        return maxId;
    }

    public void setMaxId(Long maxId) {
        this.maxId = maxId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
