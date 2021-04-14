package com.ztl.gym.code.service.impl;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.mapper.CodeTestMapper;
import com.ztl.gym.code.service.CodeTestService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CodeTestServiceImpl implements CodeTestService {
    @Autowired
    private CodeTestMapper codeTestMapper;

    @Override
    @DataSource(DataSourceType.SHARDING)
    public Code selectCodeById(long deptId, Long index) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", deptId);
        params.put("index", index);
        return codeTestMapper.selectCodeById(params);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public Code selectCode(long deptId, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", deptId);
        params.put("code", code);
        return codeTestMapper.selectCode(params);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Code> selectCodeListByCompanyId(long companyId) {
        return codeTestMapper.selectCodeListByCompanyId(companyId);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public int insertCode(Code code) {
        return codeTestMapper.insertCode(code);
    }

    @Override
    public boolean checkCompanyTableExist(long companyId, String tablePrefix) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyName", tablePrefix + companyId);
        int num = codeTestMapper.checkTableByCompany(params);
        if (num <= 0) {
            return false;
        }
        return true;
    }

}
