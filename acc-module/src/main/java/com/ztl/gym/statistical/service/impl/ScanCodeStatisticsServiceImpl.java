package com.ztl.gym.statistical.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ztl.gym.statistical.domain.vo.DateAndCountResp;
import com.ztl.gym.statistical.domain.vo.NameAndCountResp;
import com.ztl.gym.statistical.domain.vo.StatisticalDateEChartResp;
import com.ztl.gym.statistical.mapper.ScanCodeStatisticsMapper;
import com.ztl.gym.statistical.service.IScanCodeStatisticsService;
import com.ztl.gym.storage.service.IScanRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description:
 * @Author: zyl
 * @Date: 2021/8/4 9:14
 */
@Service
public class ScanCodeStatisticsServiceImpl implements IScanCodeStatisticsService {

    @Autowired
    private ScanCodeStatisticsMapper mapper;

    @Autowired
    private IScanRecordService scanRecordService;



    @Override
    public JSONObject generateCount(Long deptId) {
        JSONObject result = new JSONObject();
        long count = mapper.generateCount(deptId);
        result.put("count",count);
        //日均生码,查询两个个表最大的天数
        int totalDay = mapper.selectGenerateTotalDay(deptId);
        result.put("dayAverage",count/totalDay);
        return result;
    }

    @Override
    public JSONObject scanWeekAndDayRate(Long deptId) {
        //总扫码量
        Map<String, Object> query = new HashMap<>();
        query.put("deptId",deptId);
        int count = scanRecordService.selectScanRecordNum(query);

        String weekRate;
        String dayRate;
        //本周,上周，当天，昨天
        HashMap<String,Long> map = mapper.scanWeekAndDayCount(deptId);
        Long currentWeek = map.get("currentWeek");
        Long lastWeek = map.get("lastWeek");
        Long today = map.get("today");
        Long yesterday = map.get("yesterday");
        Long totalDay = map.get("totalDay");

        //周环比
        if (lastWeek != 0) {
            Long diff = currentWeek - lastWeek;
            weekRate = String.format("%.0f", ((diff.doubleValue() / lastWeek.doubleValue()) * 100));
        } else if(currentWeek != 0) {
            weekRate = "100";
        } else {
            weekRate = "0";
        }
        //日环比
        if (yesterday != 0) {
            Long diff = today - yesterday;
            dayRate = String.format("%.0f", ((diff.doubleValue() / yesterday.doubleValue()) * 100));
        } else if (today != 0) {
            dayRate = "100";
        } else {
            dayRate = "0";
        }

        JSONObject result = new JSONObject();
        result.put("count",count);
        result.put("weekRate",weekRate);
        result.put("dayRate",dayRate);
        result.put("dayAverage",count/totalDay.intValue());
        return result;
    }

    @Override
    public JSONObject securityCount(Long deptId) {
        JSONObject result = new JSONObject();
        //查验次数
        Map<String, Object> query = new HashMap<>();
        query.put("deptId",deptId);
        int count = scanRecordService.selectSecueityRecordNum(query);
        int totalDay = mapper.selectSecurityTotalDay(deptId);
        result.put("count",count);
        result.put("dayAverage",count/totalDay);
        return result;
    }

    @Override
    public JSONObject mixRate(Long deptId) {
        JSONObject result = new JSONObject();
        String mixRate;
        String weekRate;
        String dayRate;
        //窜货数量
        HashMap<String,Object> map = mapper.mixRate(deptId);
        Long mixCount = (Long) map.get("mixCount");
        BigDecimal totalCount = (BigDecimal) map.get("totalCount");//为毛这里的sum函数返回的是这个类型
        Long totalCountL = totalCount.longValue();
        Long currentWeek = (Long) map.get("currentWeek");
        Long lastWeek = (Long) map.get("lastWeek");
        Long today = (Long) map.get("today");
        Long yesterday = (Long) map.get("yesterday");

        //窜货率
        if (totalCountL != 0) {
            mixRate = String.format("%.2f", ((mixCount.doubleValue() / totalCountL.doubleValue()) * 100));
        } else {
            mixRate = "0";
        }
        //
        if (lastWeek != 0) {
            Long diff = currentWeek - lastWeek;
            weekRate = String.format("%.0f", ((diff.doubleValue() / lastWeek.doubleValue()) * 100)) ;
        } else if(currentWeek != 0) {
            weekRate = "100";
        } else {
            weekRate = "0";
        }
        if (yesterday != 0) {
            Long diff = today - yesterday;
            dayRate = String.format("%.0f", ((diff.doubleValue() / yesterday.doubleValue()) * 100)) ;
        } else if (today != 0) {
            dayRate = "100";
        } else {
            dayRate = "0";
        }
        result.put("mixRate",mixRate);
        result.put("weekRate",weekRate);
        result.put("dayRate",dayRate);
        return result;
    }

    @Override
    public JSONObject scanEChartTime(Integer day, Long deptId) {
        JSONObject result = new JSONObject();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<Integer> countList = new ArrayList<>();
        List<DateAndCountResp> respList = mapper.scanEChartTime(day,deptId);
        if (CollectionUtils.isNotEmpty(respList)) {
            for (DateAndCountResp resp : respList) {
                dateList.add(resp.getDate());
                countList.add(resp.getCount());
            }
        }
        result.put("dateList",dateList);
        result.put("countList",countList);
        return result;
    }

    @Override
    public JSONObject generateEChartWeek(Long deptId) {
        //查询所有最近7天的数据,分3次查询吧
        //1.查询t_code_record表最近7天数据总和
        List<DateAndCountResp> respList1 = mapper.selectRecordWeekCount(deptId);
        //2.查询t_code_single表最近7天数据总和
        List<DateAndCountResp> respList2 = mapper.selectSingleWeekCount(deptId);


        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<Integer> countList = new ArrayList<>();
        Map<String, Integer> map = new LinkedHashMap<>();

        for (DateAndCountResp resp : respList1) {
            map.put(resp.getDate(),resp.getCount());
        }

        for (DateAndCountResp resp : respList2) {
            String date = resp.getDate();
            Integer count = map.get(date);
            map.put(date,count+resp.getCount());
        }



        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            dateList.add(entry.getKey());
            countList.add(entry.getValue());
        }
        JSONObject result = new JSONObject();
        result.put("dateList",dateList);
        result.put("countList",countList);
        return result;

    }

    @Override
    public JSONObject scanEChartArea(Long deptId) {

        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> countList = new ArrayList<>();
        List<NameAndCountResp> respList;
        //先根据城市划分，如果城市个数大于35，则改为省划分
        //查询城市的数量
        int cityCount = mapper.selectScanCityCount(deptId);
        if (cityCount <= 35) {
            respList = mapper.selectScanCityCodeCount(deptId);
        } else {
            respList = mapper.selectScanProvinceCodeCount(deptId);
        }
        for (NameAndCountResp resp : respList) {
            nameList.add(resp.getName());
            countList.add(resp.getCount());
        }
        JSONObject result = new JSONObject();
        result.put("nameList",nameList);
        result.put("countList",countList);
        return result;
    }

    @Override
    public JSONObject scanEChartYesterday(Long deptId) {
        String yesterday = DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd");
        String timeStr = ":00";
        //设置分段时间数据
        Map<String, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < 25; i++) {
            String key = i+timeStr;
            map.put(key,0);
        }
        List<NameAndCountResp> dataList = mapper.selectScanYesterdayTimeCount(deptId,yesterday);

        if (CollectionUtils.isNotEmpty(dataList)) {
            for (NameAndCountResp data : dataList) {
                String name = data.getName();
                Integer count = data.getCount();
                map.put(name+timeStr,count);
            }
        }
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> countList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            nameList.add(entry.getKey());
            countList.add(entry.getValue());
        }
        JSONObject result = new JSONObject();
        result.put("nameList",nameList);
        result.put("countList",countList);
        return result;
    }

    @Override
    public JSONObject statisticalDateEChart(Long deptId, String startTime, String endTime) {
        DateTime startDate = DateUtil.parse(startTime, "yyyy-MM-dd");
        DateTime endDate = DateUtil.parse(endTime, "yyyy-MM-dd");
        long between = DateUtil.between(startDate, endDate, DateUnit.DAY);

        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> countScanList = new ArrayList<>();
        ArrayList<Integer> countSecurityList = new ArrayList<>();
        ArrayList<Integer> countGenerateList = new ArrayList<>();
        List<StatisticalDateEChartResp> respList = mapper.selectStatisticalDateEChart(deptId,startTime,between);
        for (StatisticalDateEChartResp resp : respList) {
            nameList.add(resp.getDate());
            countScanList.add(resp.getCountScan());
            countSecurityList.add(resp.getCountSecurity());
            countGenerateList.add(resp.getCountGenerate());
        }
        JSONObject result = new JSONObject();
        result.put("nameList",nameList);
        result.put("countScanList",countScanList);
        result.put("countSecurityList",countSecurityList);
        result.put("countGenerateList",countGenerateList);
        return result;
    }

    @Override
    public JSONObject generateCodeTop() {
        List<NameAndCountResp> respList = mapper.generateCodeTop();
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> countList = new ArrayList<>();
        for (NameAndCountResp resp : respList) {
            String companyName = resp.getName();
            if (companyName.length() >7) {
                companyName = handleCompanyName(companyName);
            }
            nameList.add(companyName);
            countList.add(resp.getCount());
        }
        JSONObject result = new JSONObject();
        result.put("nameList",nameList);
        result.put("countList",countList);
        return result;
    }

    @Override
    public JSONObject scanCodeTop() {
        List<NameAndCountResp> respList = mapper.scanCodeTop();
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> countList = new ArrayList<>();
        for (NameAndCountResp resp : respList) {
            String companyName = resp.getName();
            if (companyName.length() >7) {
                companyName = handleCompanyName(companyName);
            }
            nameList.add(companyName);
            countList.add(resp.getCount());
        }
        JSONObject result = new JSONObject();
        result.put("nameList",nameList);
        result.put("countList",countList);
        return result;
    }

    @Override
    public JSONObject securityCodeTop() {
        List<NameAndCountResp> respList = mapper.securityCodeTop();
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> countList = new ArrayList<>();
        for (NameAndCountResp resp : respList) {
            String companyName = resp.getName();
            if (companyName.length() >7) {
                companyName = handleCompanyName(companyName);
            }
            nameList.add(companyName);
            countList.add(resp.getCount());
        }
        JSONObject result = new JSONObject();
        result.put("nameList",nameList);
        result.put("countList",countList);
        return result;
    }

    /**
     * 处理echart图公司名称过长显示不美观问题
     * @param companyName 公司名称长度大于7的
     * @return
     */
    public String handleCompanyName(String companyName){
        String companyNameHand = "";
        int indexLength = 7;
        ArrayList<String> companySubList = new ArrayList<>();
        int length = companyName.length();
        int count = length / indexLength;
        int remainder = length%indexLength;
        if (remainder !=0) {
            count = count +1;
        }
        for (int i = 0; i < count; i++) {
            int indexMax = (i + 1) * indexLength;
            String substring;
            if (length > indexMax) {
                substring = companyName.substring(i * 7, (i + 1) * 7);
            } else {
                substring = companyName.substring(i * 7, length);
            }
            companySubList.add(substring);
        }
        for (String s : companySubList) {
            companyNameHand += s+"\n";
        }
        companyNameHand = companyNameHand.substring(0,companyNameHand.length()-1);
        return companyNameHand;

    }
}
