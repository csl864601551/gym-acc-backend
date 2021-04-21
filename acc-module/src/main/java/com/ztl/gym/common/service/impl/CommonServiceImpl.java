package com.ztl.gym.common.service.impl;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.mapper.CommonMapper;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private ISysDeptService deptService;

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

    @Override
    public List<SysUser> getNextLevelUser() {
        Map<String, Object> params = new HashMap<>();
        Long company_id = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!company_id.equals(AccConstants.ADMIN_DEPT_ID)) {
            params.put("companyId", SecurityUtils.getLoginUserTopCompanyId());
        } else {
            params.put("companyId", AccConstants.ADMIN_DEPT_ID);
        }
        return commonMapper.getNextLevelUser(params);
    }

    @Override
    public List<SysUser> getSameLevelUser() {
        Map<String, Object> params = new HashMap<>();
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            params.put("companyId", SecurityUtils.getLoginUserTopCompanyId());
        } else {
            params.put("companyId", AccConstants.ADMIN_DEPT_ID);
        }
        return commonMapper.getSameLevelUser(params);
    }

    @Override
    public Long getTenantId() {
        Long tenantId = null;
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            String[] ancestors = SecurityUtils.getLoginUserCompany().getAncestors().split(",");
            if (ancestors.length > 2) {
                if (SecurityUtils.getLoginUserCompany().getDeptType() == 1) {
                    SysDept pDept = deptService.selectDeptById(SecurityUtils.getLoginUserCompany().getParentId());
                    if (pDept.getDeptType() == 2) {
                        tenantId = pDept.getDeptId();
                    }
                } else if (SecurityUtils.getLoginUserCompany().getDeptType() == 2) {
                    tenantId = companyId;
                }
            }
        }
        return tenantId;
    }
}
