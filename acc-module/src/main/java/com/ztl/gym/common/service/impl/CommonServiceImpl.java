package com.ztl.gym.common.service.impl;

import com.ztl.gym.common.mapper.CommonMapper;
import com.ztl.gym.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private CommonMapper commonMapper;

    @Override
    public synchronized long selectCurrentVal(long companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        int num = commonMapper.selectIsExist(params);
        if (num <= 0) {
            commonMapper.insertCompany(params);
        }
        return commonMapper.selectCurrentVal(params);
    }

    @Override
    public synchronized long selectNextVal(long companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        int num = commonMapper.selectIsExist(params);
        if (num <= 0) {
            commonMapper.insertCompany(params);
        }
        return commonMapper.selectNextVal(params);
    }

    @Override
    public int updateVal(long companyId, long val) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("val", val);
        return commonMapper.updateVal(params);
    }
}
