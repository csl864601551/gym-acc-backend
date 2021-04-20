package com.ztl.gym.storage.domain.vo;

public class FlowVo {
    private long companyId;
    private String code;
    private int storageType;
    private long storageRecordId;
    private long createUser;
    private String createTime;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStorageType() {
        return storageType;
    }

    public void setStorageType(int storageType) {
        this.storageType = storageType;
    }

    public long getStorageRecordId() {
        return storageRecordId;
    }

    public void setStorageRecordId(long storageRecordId) {
        this.storageRecordId = storageRecordId;
    }

    public long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(long createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
