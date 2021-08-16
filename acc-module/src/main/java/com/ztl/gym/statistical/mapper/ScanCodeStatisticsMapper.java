package com.ztl.gym.statistical.mapper;

import com.ztl.gym.statistical.domain.vo.DateAndCountResp;
import com.ztl.gym.statistical.domain.vo.NameAndCountResp;
import com.ztl.gym.statistical.domain.vo.StatisticalDateEChartResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: zyl
 * @Date: 2021/8/4 10:03
 */
@Mapper
public interface ScanCodeStatisticsMapper {

    long generateCount(@Param("deptId") Long deptId);


    /**
     * 查询t_scan_record本周，上周，当天，昨天数量
     * @return
     */
    HashMap<String, Long> scanWeekAndDayCount(@Param("deptId")Long deptId);

    /**
     * 查询窜货数量，总数量(code_record+code_single)，上周数量，本周数量，昨天数量，今天数量
     * @param deptId
     * @return
     */
    HashMap<String, Object> mixRate(@Param("deptId")Long deptId);

    /**
     * 查询最近几天的扫码数量
     * @param day 天数
     * @param deptId
     * @return
     */
    List<DateAndCountResp> scanEChartTime(@Param("day")Integer day, @Param("deptId")Long deptId);

    int selectSecurityTotalDay(@Param("deptId")Long deptId);

    int selectGenerateTotalDay(@Param("deptId")Long deptId);

    List<DateAndCountResp> selectRecordWeekCount(@Param("deptId")Long deptId);

    List<DateAndCountResp> selectSingleWeekCount(@Param("deptId")Long deptId);


    int selectScanCityCount(@Param("deptId")Long deptId);

    List<NameAndCountResp> selectScanCityCodeCount(@Param("deptId")Long deptId);

    List<NameAndCountResp> selectScanProvinceCodeCount(@Param("deptId")Long deptId);

    List<NameAndCountResp> selectScanYesterdayTimeCount(@Param("deptId")Long deptId,@Param("yesterday")String yesterday);

    List<StatisticalDateEChartResp> selectStatisticalDateEChart(@Param("deptId")Long deptId, @Param("startTime")String startTime, @Param("between")long between);

    List<NameAndCountResp> generateCodeTop();

    List<NameAndCountResp> scanCodeTop();

    List<NameAndCountResp> securityCodeTop();
}
