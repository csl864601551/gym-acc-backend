package com.ztl.gym.web.controller.open.sso.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CompanyObj implements Serializable {
    private String name;
    private String summary;
    private String code;
    private String province;
    private String provinceCode;
    private String city;
    private String cityCode;
    private String district;
    private String districtCode;
    private String address;
}
