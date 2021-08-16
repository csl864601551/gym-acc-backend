package com.ztl.gym.statistical.service;


import com.alibaba.fastjson.JSONObject;


public interface IScanCodeStatisticsService {

    JSONObject generateCount(Long deptId);

    JSONObject scanWeekAndDayRate(Long deptId);

    JSONObject securityCount(Long deptId);

    JSONObject mixRate(Long deptId);

    JSONObject scanEChartTime(Integer day, Long deptId);

    JSONObject generateEChartWeek(Long deptId);

    JSONObject scanEChartArea(Long deptId);

    JSONObject scanEChartYesterday(Long deptId);

    JSONObject statisticalDateEChart(Long deptId, String startTime, String endTime);

    JSONObject generateCodeTop();

    JSONObject scanCodeTop();

    JSONObject securityCodeTop();
}
