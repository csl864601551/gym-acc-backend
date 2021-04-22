package com.ztl.gym.code.service.impl;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.mapper.CodeMapper;
import com.ztl.gym.code.mapper.CodeRecordMapper;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.storage.domain.vo.FlowVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 码 Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-14
 */
@Service
public class CodeServiceImpl implements ICodeService {
    private static final Logger logger = LoggerFactory.getLogger(CodeServiceImpl.class);

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private CodeRecordMapper codeRecordMapper;

    @Autowired
    private CommonService commonService;

    @Override
    @DataSource(DataSourceType.SHARDING)
    public Code selectCode(Code code) {
        return codeMapper.selectCode(code);
    }

    /**
     * 查询码
     *
     * @param codeIndex 码 ID
     * @return 码
     */
    @Override
    public Code selectCodeById(Long codeIndex) {
        return codeMapper.selectCodeById(codeIndex);
    }

    /**
     * 查询码 列表
     *
     * @param code 码
     * @return 码
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Code> selectCodeList(Code code) {
        return codeMapper.selectCodeList(code);
    }

    /**
     * 新增码
     *
     * @param code 码
     * @return 结果
     */
    @Override
    public int insertCode(Code code) {
        return codeMapper.insertCode(code);
    }

    /**
     * 修改码
     *
     * @param code 码
     * @return 结果
     */
    @Override
    public int updateCode(Code code) {
        return codeMapper.updateCode(code);
    }

    /**
     * 批量删除码
     *
     * @param codeIndexs 需要删除的码 ID
     * @return 结果
     */
    @Override
    public int deleteCodeByIds(Long[] codeIndexs) {
        return codeMapper.deleteCodeByIds(codeIndexs);
    }

    /**
     * 删除码 信息
     *
     * @param codeIndex 码 ID
     * @return 结果
     */
    @Override
    public int deleteCodeById(Long codeIndex) {
        return codeMapper.deleteCodeById(codeIndex);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public int createCode(Long companyId, Long codeRecordId, Long codeTotalNum, String pCode, Long codeAttrId) {
        int correct = 0;
        List<Code> codeList = new ArrayList<>();
        //企业自增数
        long codeVal = commonService.selectCurrentVal(companyId);
        long codeIndex = codeVal;
        if (StringUtils.isNotBlank(pCode)) {
            codeIndex += 1;
            //箱码
            Code code = new Code();
            code.setCodeIndex(codeIndex);
            code.setCompanyId(companyId);
            code.setCodeType(AccConstants.CODE_TYPE_BOX);
            //生码规则 企业id+日期+流水 【注意：客户扫码时没办法知道码所属企业，无法从对应分表查询，这里设置规则的时候需要把企业id带进去】
            code.setCode(pCode);
            code.setCodeAttrId(codeAttrId);
            codeList.add(code);
        }
        for (int i = 1; i <= codeTotalNum; i++) {
            //流水号
            codeIndex += 1;
            Code code = new Code();
            code.setCodeIndex(codeIndex);
            if (StringUtils.isNotBlank(pCode)) {
                code.setpCode(pCode);
            }
            code.setCompanyId(companyId);
            code.setCodeType(AccConstants.CODE_TYPE_SINGLE);
            //生码规则 企业id+日期+流水
            String codeStr = "S" + companyId + "/" + DateUtils.dateTimeNow() + codeIndex;
            code.setCode(codeStr);
            if (codeAttrId != null && codeAttrId > 0) {
                code.setCodeAttrId(codeAttrId);
            }
            codeList.add(code);

            //更新自增数
            if (i == codeTotalNum) {
                commonService.updateVal(companyId, codeIndex);
            }
        }

        int res = codeMapper.insertCodeForBatch(codeList);
        if (res > 0) {
            logger.info("生码记录ID：" + codeRecordId + "生码成功，总数：" + codeTotalNum);
            Map<String, Object> params = new HashMap<>();
            params.put("id", codeRecordId);
            params.put("status", AccConstants.CODE_RECORD_STATUS_FINISH);
            codeRecordMapper.insertCodeRecordStatus(params);
        } else {
            logger.error("生码记录ID：" + codeRecordId + "生码异常，总数：" + codeTotalNum + "，生码数：" + correct);
        }
        return correct;
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public int updateStatusByAttrId(Long companyId, Long codeAttrId, int status) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("codeAttrId", codeAttrId);
        params.put("status", status);
        return codeMapper.updateStatusByAttrId(params);
    }

    /**
     * 新增单码流转明细
     *
     * @param flowVo
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public int insertCodeFlowForSingle(FlowVo flowVo) {
        return codeMapper.insertCodeFlowForSingle(flowVo);
    }

    /**
     * 批量新增单码流转明细 【insertProvider形式】
     *
     * @param list
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public int insertCodeFlowForBatchSingle(long companyId, List<FlowVo> list) {
//        return codeMapper.insertCodeFlowForBatchSingle(companyId, list);
        return codeMapper.insertCodeFlowForBatchSingleV2(companyId, list);
    }

    /**
     * 新增箱码流转明细
     *
     * @param flowVo
     * @return
     */
    @Override
    public int insertCodeFlowForBox(FlowVo flowVo) {
        return codeMapper.insertCodeFlowForBox(flowVo);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<String> selectCodeByStorage(long companyId, int storageType, long storageRecordId) {
        List<String> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("storageType", storageType);
        params.put("storageRecordId", storageRecordId);
        String pCode = codeMapper.selectPcodeByStorage(params);
        list.add(pCode);
        List<String> code = codeMapper.selectCodeByStorage(params);
        list.addAll(code);
        return list;
    }

    /**
     * 构建批量插入单码明细sql
     *
     * @param list
     * @return
     */
    public static String buildInsertBatchCodeFlowSql(long companyId, List<FlowVo> list) {
        StringBuffer sqlList = new StringBuffer();
        sqlList.append(" INSERT INTO t_code_flow(company_id,code,storage_type,storage_record_id,create_user,create_time)  VALUES ");
        for (int i = 0; i < list.size(); i++) {
            FlowVo flowVo = list.get(i);
            sqlList.append(" (").
                    append(companyId).append(",")
                    .append("'").append(flowVo.getCode()).append("',")
                    .append(flowVo.getStorageType()).append(",")
                    .append(flowVo.getStorageRecordId()).append(",")
                    .append(flowVo.getCreateUser()).append(",")
                    .append("'").append(flowVo.getCreateTime()).append("'")
                    .append(")");
            if (i < list.size() - 1) {
                sqlList.append(",");
            }
        }
        System.out.println("sql: " + sqlList.toString());
        return sqlList.toString();
    }
}
