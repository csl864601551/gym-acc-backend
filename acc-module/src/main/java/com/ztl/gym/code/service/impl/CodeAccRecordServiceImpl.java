package com.ztl.gym.code.service.impl;

import com.ztl.gym.code.domain.CodeAcc;
import com.ztl.gym.code.domain.CodeAccRecord;
import com.ztl.gym.code.mapper.CodeAccRecordMapper;
import com.ztl.gym.code.mapper.CodeMapper;
import com.ztl.gym.code.service.ICodeAccRecordService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 生码记录Service业务层处理
 *
 * @author ruoyi
 * @date 2021-07-22
 */
@Service
public class CodeAccRecordServiceImpl implements ICodeAccRecordService
{
    @Autowired
    private CodeAccRecordMapper codeAccRecordMapper;
    @Autowired
    private CodeMapper codeMapper;

    /**
     * 查询生码记录
     *
     * @param id 生码记录ID
     * @return 生码记录
     */
    @Override
    public CodeAccRecord selectCodeAccRecordById(Long id)
    {
        return codeAccRecordMapper.selectCodeAccRecordById(id);
    }

    /**
     * 查询生码记录列表
     *
     * @param codeAccRecord 生码记录
     * @return 生码记录
     */
    @Override
    public List<CodeAccRecord> selectCodeAccRecordList(CodeAccRecord codeAccRecord)
    {
        return codeAccRecordMapper.selectCodeAccRecordList(codeAccRecord);
    }

    /**
     * 新增生码记录
     *
     * @param codeAccRecord 生码记录
     * @return 结果
     */
    @Override
    public int insertCodeAccRecord(CodeAccRecord codeAccRecord)
    {
        codeAccRecord.setCreateTime(DateUtils.getNowDate());
        return codeAccRecordMapper.insertCodeAccRecord(codeAccRecord);
    }
    /**
     * 生码-防伪码
     *
     * @param companyId 企业id
     * @param num       生码数量
     * @param remark    备注详情
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public int createAccCodeSingle(long companyId, long num, String remark) {
        CodeAccRecord codeAccRecord = new CodeAccRecord();
        codeAccRecord.setCompanyId(companyId);
        codeAccRecord.setCount(num);
        codeAccRecord.setType(AccConstants.GEN_CODE_TYPE_ACC);
        codeAccRecord.setStatus(AccConstants.CODE_RECORD_STATUS_EVA);
        codeAccRecord.setRemark(remark);
        codeAccRecord.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        codeAccRecord.setCreateTime(new Date());
        int res = codeAccRecordMapper.insertCodeAccRecord(codeAccRecord);

        if (res > 0) {
            List<Map> codeList = new ArrayList<>();
            Date createTime=DateUtils.getNowDate();
            for (int i = 0; i < num; i++) {
                Map<String,Object> code=new HashMap();
                code.put("companyId",companyId);
                code.put("codeAcc", CodeRuleUtils.buildAccCode(companyId));
                code.put("recordId",codeAccRecord.getId());
                code.put("createTime",createTime);
                codeList.add(code);
            }
            res = codeMapper.insertAccCodeForBatch(codeList);
        }
        return res;
    }


    /**
     * 修改生码记录
     *
     * @param codeAccRecord 生码记录
     * @return 结果
     */
    @Override
    public int updateCodeAccRecord(CodeAccRecord codeAccRecord)
    {
        codeAccRecord.setUpdateTime(DateUtils.getNowDate());
        return codeAccRecordMapper.updateCodeAccRecord(codeAccRecord);
    }

    /**
     * 批量删除生码记录
     *
     * @param ids 需要删除的生码记录ID
     * @return 结果
     */
    @Override
    public int deleteCodeAccRecordByIds(Long[] ids)
    {
        return codeAccRecordMapper.deleteCodeAccRecordByIds(ids);
    }

    /**
     * 删除生码记录信息
     *
     * @param id 生码记录ID
     * @return 结果
     */
    @Override
    public int deleteCodeAccRecordById(Long id)
    {
        return codeAccRecordMapper.deleteCodeAccRecordById(id);
    }

    @Override
    public List<CodeAcc> selectAccCodeListByRecord(Long companyId, Long recordId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("recordId", recordId);
        return codeAccRecordMapper.selectAccCodeListByRecord(companyId, recordId);
    }


    /**
     * 生防伪码总量
     *
     * @param map 部门信息
     * @return 结果
     */
    @Override
    public int selectAccCodeNum(Map<String, Object> map) {
        return codeAccRecordMapper.selectAccCodeNum(map);
    }
}
