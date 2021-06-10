package com.ztl.gym.common.service.impl;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.mapper.CommonMapper;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.domain.StorageTransfer;
import com.ztl.gym.storage.service.IStorageBackService;
import com.ztl.gym.storage.service.IStorageInService;
import com.ztl.gym.storage.service.IStorageOutService;
import com.ztl.gym.storage.service.IStorageTransferService;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ICodeService codeService;
    @Autowired
    private IStorageInService storageInService;
    @Autowired
    private IStorageOutService storageOutService;
    @Autowired
    private IStorageTransferService storageTransferService;
    @Autowired
    private IStorageBackService storageBackService;

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
    public List<SysUser> getTenantByParent() {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", SecurityUtils.getLoginUserCompany().getDeptId());
        return commonMapper.getTenantByParent(params);
    }

    @Override
    public Long getParentDeptId(long deptId) {
        SysDept sysDept = deptService.selectDeptById(deptId);
        return sysDept.getParentId();
    }

    @Override
    public Long getTenantId() {
        Long tenantId = null;
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            String[] ancestors = SecurityUtils.getLoginUserCompany().getAncestors().split(",");
            if (ancestors.length == 2) {
                tenantId = companyId;
            } else if (ancestors.length > 2) {
                if (SecurityUtils.getLoginUserCompany().getDeptType() == 1) {
                    SysDept pDept = deptService.selectDeptById(SecurityUtils.getLoginUserCompany().getParentId());
                    if (pDept.getDeptType() == 2) {
                        tenantId = pDept.getDeptId();
                    } else {
                        throw new CustomException("企业数据异常：上级用户不是自营！", HttpStatus.ERROR);
                    }
                } else if (SecurityUtils.getLoginUserCompany().getDeptType() == 2) {
                    tenantId = companyId;
                }
            }
        }
        return tenantId;
    }

    @Override
    public String getStorageNo(int storageType) {
        String no = null;
        if (storageType == AccConstants.STORAGE_TYPE_IN) {
            no = "RK" + SecurityUtils.getLoginUser().getUser().getDeptId() + DateUtils.dateTimeNow();
        } else if (storageType == AccConstants.STORAGE_TYPE_OUT) {
            no = "CH" + SecurityUtils.getLoginUser().getUser().getDeptId() + DateUtils.dateTimeNow();
        } else if (storageType == AccConstants.STORAGE_TYPE_TRANSFER) {
            no = "DB" + SecurityUtils.getLoginUser().getUser().getDeptId() + DateUtils.dateTimeNow();
        } else if (storageType == AccConstants.STORAGE_TYPE_BACK) {
            no = "TH" + SecurityUtils.getLoginUser().getUser().getDeptId() + DateUtils.dateTimeNow();
        }
        return no;
    }

    /**
     * 查询码当前操作是否合法 【输入流转操作输入货码时调用】
     *
     * @param companyId   企业id
     * @param storageType 当前流转操作类型 【见AccConstants】
     * @param queryValue  查询值
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public boolean judgeStorageIsIllegalByValue(long companyId, Integer storageType, String queryValue) {
        //判断是否是平台
        if (companyId == AccConstants.ADMIN_DEPT_ID || companyId == 0) {
            throw new CustomException("平台无需进行码操作！", HttpStatus.ERROR);
        }

        //查询当前用户信息
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        long currentUserDeptId = currentUser.getDeptId();

        //查询码
        Code codeParam = new Code();
        Code codeResult = null;
        codeParam.setCode(queryValue);
        Long codeCompanyId = CodeRuleUtils.getCompanyIdByCode(queryValue);
        //判断码
        if (codeCompanyId == null || codeCompanyId == 0) {
            throw new CustomException("码格式错误！", HttpStatus.ERROR);
        }
        //判断码企业
        if (codeCompanyId != companyId) {
            throw new CustomException("该码不属于当前用户企业！", HttpStatus.ERROR);
        }

        codeParam.setCompanyId(codeCompanyId);
        codeResult = codeService.selectCode(codeParam);
        if (codeResult == null) {
            throw new CustomException("码不存在！", HttpStatus.ERROR);
        }

        //除入库以外的流转都需要有状态 【所有产品必须先入库，所以其他流转状态时需判断是否已入库或是否有其他状态】
        if (storageType != AccConstants.STORAGE_TYPE_IN) {
            if (codeResult.getCodeAttr().getStorageType() == null) {
                throw new CustomException("该码当前未入库！", HttpStatus.ERROR);
            }
        }

        //判断码合不合规
        switch (storageType) {
            case AccConstants.STORAGE_TYPE_IN:
                if (codeResult.getCodeAttr().getStorageType() == null || codeResult.getCodeAttr().getStorageType() == 0) {
                    //第一次入库只有企业才有权限
                    long userCompanyId = SecurityUtils.getLoginUserTopCompanyId();
                    if (userCompanyId != currentUserDeptId) {
                        throw new CustomException("首次入库需要企业权限！", HttpStatus.ERROR);
                    }
                } else if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_IN) {
                    //查询当前码状态是否是入库
                    throw new CustomException("该码当前流转状态为入库中，无法重复入库", HttpStatus.ERROR);
                } else if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_OUT) {
                    //判断出货数据是否正常
                    StorageOut storageOut = storageOutService.selectStorageOutById(codeResult.getCodeAttr().getStorageRecordId());
                    if (storageOut == null) {
                        throw new CustomException("该码当前出库数据异常", HttpStatus.ERROR);
                    } else {
                        //判断出货的接收人是否是当前用户
                        if (storageOut.getStorageTo() != currentUserDeptId) {
                            throw new CustomException("该码当前出库接收人与当前登录用户不一致", HttpStatus.ERROR);
                        }
                    }
                } else if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_BACK) {
                    //查询当前码状态是否是入库
                    throw new CustomException("该码当前流转状态为退货入库中，无法重复入库", HttpStatus.ERROR);
                } else if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_TRANSFER) {
                    //判断调拨状态
                    throw new CustomException("该码当前为调拨中状态，无法入库！", HttpStatus.ERROR);
                }
                break;
            case AccConstants.STORAGE_TYPE_OUT:
                if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_IN) {
                    if (codeResult.getCodeAttr().getTenantId() != currentUserDeptId) {
                        throw new CustomException("该码不属于当前部门！", HttpStatus.ERROR);
                    }
                } else if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_OUT) {
                    throw new CustomException("该码当前已出库！", HttpStatus.ERROR);
                } else if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_BACK) {
                    throw new CustomException("该码当前流转状态为退货中，无法出库", HttpStatus.ERROR);
                } else if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_TRANSFER) {
                    //判断调拨状态
                    StorageTransfer storageTransfer = storageTransferService.selectStorageTransferById(codeResult.getCodeAttr().getStorageRecordId());
                    if (storageTransfer == null) {
                        throw new CustomException("该码当前调拨数据异常", HttpStatus.ERROR);
                    } else {
                        if (codeResult.getCodeAttr().getTenantId() != currentUserDeptId) {
                            throw new CustomException("该码不属于当前部门！", HttpStatus.ERROR);
                        }
                    }
                } else {
                    throw new CustomException("该码当前未入库！", HttpStatus.ERROR);
                }
                break;
            case AccConstants.STORAGE_TYPE_TRANSFER:
                break;
            case AccConstants.STORAGE_TYPE_BACK:
                if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_IN) {
                    if (codeResult.getCodeAttr().getTenantId() != currentUserDeptId) {
                        throw new CustomException("该码不属于当前部门！", HttpStatus.ERROR);
                    }
                } else if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_OUT) {
                    throw new CustomException("该码状态当前为出库中，无法退货！", HttpStatus.ERROR);
                } else if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_BACK) {
                    //查询当前码状态是否是入库
                    throw new CustomException("该码当前流转状态为退货中，无法重复退货", HttpStatus.ERROR);
                } else if (codeResult.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_TRANSFER) {
                    throw new CustomException("该码状态当前为调拨中，无法退货！", HttpStatus.ERROR);
                } else {
                    throw new CustomException("该码当前未入库！", HttpStatus.ERROR);
                }
                break;
        }
        return true;
    }

    /**
     * 根据流转信息查询码集合
     *
     * @param companyId
     * @param storageType
     * @param storageRecordId
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Code> selectCodeByStorage(long companyId, int storageType, long storageRecordId) {
        List<Code> list = null;
        List<String> codeStrs = codeService.selectCodeByStorage(companyId, storageType, storageRecordId);
        if (codeStrs.size() > 0) {
            Code codeParam = new Code();
            codeParam.setCompanyId(companyId);
            codeParam.setCode(codeStrs.get(0));
            Code code = codeService.selectCode(codeParam);

            codeParam.setCode(null);
            codeParam.setCodeAttrId(code.getCodeAttrId());
            list = codeService.selectCodeList(codeParam);
        }
        return list;
    }

    /**
     * 根据流转信息查询码集合,返回查询条件 【分页查询】
     *
     * @param companyId
     * @param storageType
     * @param storageRecordId
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public Code selectCodeByStorageForPage(long companyId, int storageType, long storageRecordId) {
        List<String> codeStrs = codeService.selectCodeByStorage(companyId, storageType, storageRecordId);
        if (codeStrs.size() > 0) {
            Code codeParam = new Code();
            codeParam.setCompanyId(companyId);
            codeParam.setCode(codeStrs.get(0));
            Code code = codeService.selectCode(codeParam);

            codeParam.setCode(null);
            codeParam.setCodeAttrId(code.getCodeAttrId());
            return codeParam;
        }
        return null;
    }
    /**
     * 实现txt下载
     * @param response
     */
    public void downloadTXT(String fileName,String content,HttpServletResponse response) {

        //String fileName = "fileName" + ".txt";
        //String content = "写入txt的内容";
        response.setContentType("text/plain");

        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ServletOutputStream outputStream = null;
        BufferedOutputStream buffer = null;

        try {
            outputStream = response.getOutputStream();
            buffer = new BufferedOutputStream(outputStream);
            buffer.write(content.getBytes("UTF-8"));
            buffer.flush();
            buffer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
