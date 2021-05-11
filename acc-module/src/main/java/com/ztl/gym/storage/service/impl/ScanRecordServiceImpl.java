package com.ztl.gym.storage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.ScanRecordMapper;
import com.ztl.gym.storage.domain.ScanRecord;
import com.ztl.gym.storage.service.IScanRecordService;

/**
 * 扫码记录Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-28
 */
@Service
public class ScanRecordServiceImpl implements IScanRecordService {
    @Autowired
    private ScanRecordMapper scanRecordMapper;
    @Autowired
    private ICodeService codeService;

    /**
     * 查询扫码记录
     *
     * @param id 扫码记录ID
     * @return 扫码记录
     */
    @Override
    public ScanRecord selectScanRecordById(Long id) {
        return scanRecordMapper.selectScanRecordById(id);
    }

    /**
     * 查询扫码记录列表
     *
     * @param scanRecord 扫码记录
     * @return 扫码记录
     */
    @Override
    public List<ScanRecord> selectScanRecordList(ScanRecord scanRecord) {
        return scanRecordMapper.selectScanRecordList(scanRecord);
    }

    /**
     * 新增扫码记录
     *
     * @param scanRecord 扫码记录
     * @return 结果
     */
    @Override
    public int insertScanRecord(ScanRecord scanRecord) {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            scanRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        scanRecord.setCreateTime(DateUtils.getNowDate());
        scanRecord.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return scanRecordMapper.insertScanRecord(scanRecord);
    }

    /**
     * 修改扫码记录
     *
     * @param scanRecord 扫码记录
     * @return 结果
     */
    @Override
    public int updateScanRecord(ScanRecord scanRecord) {
        scanRecord.setUpdateTime(DateUtils.getNowDate());
        return scanRecordMapper.updateScanRecord(scanRecord);
    }

    /**
     * 批量删除扫码记录
     *
     * @param ids 需要删除的扫码记录ID
     * @return 结果
     */
    @Override
    public int deleteScanRecordByIds(Long[] ids) {
        return scanRecordMapper.deleteScanRecordByIds(ids);
    }

    /**
     * 删除扫码记录信息
     *
     * @param id 扫码记录ID
     * @return 结果
     */
    @Override
    public int deleteScanRecordById(Long id) {
        return scanRecordMapper.deleteScanRecordById(id);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public Map<String, Object> getScanRecordByCode(Long companyId,String codeVal) {
        Map<String, Object> returnMap = new HashMap<>();//返回数据
        Code code = new Code();//码产品信息
        ScanRecord scanRecord=new ScanRecord();//扫码记录
        code.setCode(codeVal);
        scanRecord.setCode(codeVal);
        if (companyId > 0) {
            code.setCompanyId(companyId);
            scanRecord.setCompanyId(companyId);
        }
        Code codeEntity = codeService.selectCode(code);//查询码产品你基本信息
        List<ScanRecord> scanList=scanRecordMapper.selectScanRecordList(scanRecord);//查询扫码记录
        List<Map<String,Object>> flowList=scanRecordMapper.selectFlowList(companyId,codeVal);//查询物流记录

        returnMap.put("codeEntity",codeEntity);
        returnMap.put("scanList",scanList);
        returnMap.put("flowList",flowList);
        return returnMap;
    }


}
