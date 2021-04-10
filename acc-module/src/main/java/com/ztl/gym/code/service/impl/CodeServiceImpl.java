package com.ztl.gym.code.service.impl;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.mapper.CodeMapper;
import com.ztl.gym.code.service.CodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CodeServiceImpl implements CodeService {
    @Autowired
    private CodeMapper codeMapper;

    @Override
    @DataSource(DataSourceType.SHARDING)
    public Code selectCodeById(long deptId, Long index) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", deptId);
        params.put("index", index);
        return codeMapper.selectCodeById(params);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public Code selectCode(long deptId, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", deptId);
        params.put("code", code);
        return codeMapper.selectCode(params);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Code> selectCodeListByCompanyId(long companyId) {
        return codeMapper.selectCodeListByCompanyId(companyId);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public int insertCode(Code code) {
        return codeMapper.insertCode(code);
    }
}
