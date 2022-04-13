package com.ztl.gym.storage.domain;

import com.ztl.gym.common.core.domain.BaseEntity;

public class InCodeFlow extends BaseEntity {
    private long companyId;
    private String code;
    private long storageRecordId;

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

    public long getStorageRecordId() {
        return storageRecordId;
    }

    public void setStorageRecordId(long storageRecordId) {
        this.storageRecordId = storageRecordId;
    }
}
