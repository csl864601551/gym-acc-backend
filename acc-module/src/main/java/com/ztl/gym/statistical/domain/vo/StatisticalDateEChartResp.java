package com.ztl.gym.statistical.domain.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: zyl
 * @Date: 2021/8/5 17:45
 */
@Data
public class StatisticalDateEChartResp {
    private String date;
    //扫码数量
    private Integer countScan;
    //查验数量
    private Integer countSecurity;
    //生码数量
    private Integer countGenerate;
}
