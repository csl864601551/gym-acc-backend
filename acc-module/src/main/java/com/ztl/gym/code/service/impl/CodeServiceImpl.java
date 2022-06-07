package com.ztl.gym.code.service.impl;

import cn.hutool.json.JSONUtil;
import com.ztl.gym.code.domain.*;
import com.ztl.gym.code.domain.vo.CRMInfoVo;
import com.ztl.gym.code.mapper.CodeMapper;
import com.ztl.gym.code.mapper.CodeRecordMapper;
import com.ztl.gym.code.mapper.CodeSingleMapper;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.product.domain.ProductStockFlow;
import com.ztl.gym.product.service.IProductStockFlowService;
import com.ztl.gym.storage.domain.vo.FlowVo;
import com.ztl.gym.storage.mapper.StorageInMapper;
import com.ztl.gym.storage.service.IStorageInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private CodeSingleMapper codeSingleMapper;
    @Autowired
    private StorageInMapper storageInMapper;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ICodeAttrService codeAttrService;
    @Autowired
    private IProductStockFlowService productStockFlowService;
    @Autowired
    private IStorageInService storageInService;


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
    @DataSource(DataSourceType.SHARDING)
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
    @DataSource(DataSourceType.SHARDING)
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

    /**
     * 生码
     *
     * @param companyId    企业id
     * @param codeRecordId 生码记录id
     * @param codeTotalNum 生码总数
     * @param boxCount     箱数
     * @param userId       用户id
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public int createCode(Long companyId, Long codeRecordId, Long codeTotalNum, long boxCount, Long userId) {
        int correct = 0;
        List<Code> codeList = new ArrayList<>();
        //企业自增数
        CodeRecord codeRecord = codeRecordMapper.selectCodeRecordById(codeRecordId);
        long codeIndex = codeRecord.getIndexStart();
        if (boxCount > 0) {
            for (int i = 0; i < boxCount; i++) {
                //按箱来创建码属性
                long boxAttrId = saveCodeAttr(companyId, userId, codeRecord.getId(), codeRecord.getIndexStart(), codeRecord.getIndexEnd());

                //箱码
                String pCode = CodeRuleUtils.buildCode(companyId, CodeRuleUtils.CODE_PREFIX_B, codeIndex);
                Code boxCode = new Code();
                boxCode.setCodeIndex(codeIndex);
                boxCode.setCompanyId(companyId);
                boxCode.setCodeType(AccConstants.CODE_TYPE_BOX);
                boxCode.setCode(pCode);
                boxCode.setCodeAttrId(boxAttrId);
                codeList.add(boxCode);
                //单码流水号+1
                codeIndex += 1;

                //单码
                for (int j = 0; j < codeTotalNum; j++) {
                    Code singleCode = new Code();
                    singleCode.setCodeIndex(codeIndex);
                    singleCode.setpCode(pCode);
                    singleCode.setCompanyId(companyId);
                    singleCode.setCodeType(AccConstants.CODE_TYPE_SINGLE);
                    singleCode.setCode(CodeRuleUtils.buildCode(companyId, CodeRuleUtils.CODE_PREFIX_S, singleCode.getCodeIndex()));
                    singleCode.setCodeAttrId(boxAttrId);
                    codeList.add(singleCode);
                    codeIndex += 1;
                }

                //更新自增数
                if (i + 1 == boxCount) {
                    commonService.updateVal(companyId, codeRecord.getIndexEnd());
                }
            }
        } else {
            long attrId = saveCodeAttr(companyId, userId, codeRecord.getId(), codeRecord.getIndexStart(), codeRecord.getIndexEnd());
            for (int i = 0; i < codeTotalNum; i++) {
                Code code = new Code();
                code.setCodeIndex(codeIndex + i);
                code.setpCode(null);
                code.setCompanyId(companyId);
                code.setCodeType(AccConstants.CODE_TYPE_SINGLE);
                code.setCode(CodeRuleUtils.buildCode(companyId, CodeRuleUtils.CODE_PREFIX_S, code.getCodeIndex()));
                code.setCodeAttrId(attrId);
                codeList.add(code);

                //更新自增数
                if (i + 1 == codeTotalNum) {
                    commonService.updateVal(companyId, codeRecord.getIndexEnd());
                }
            }
        }

        int res = codeMapper.insertCodeForBatch(codeList);
        if (res > 0) {
            logger.info("生码记录ID：" + codeRecordId + "生码成功");
            Map<String, Object> params = new HashMap<>();
            params.put("id", codeRecordId);
            params.put("status", AccConstants.CODE_RECORD_STATUS_FINISH);
            codeRecordMapper.insertCodeRecordStatus(params);
        } else {
            logger.error("生码记录ID：" + codeRecordId + "生码异常");
        }
        return correct;
    }

    /**
     * 生码
     *
     * @param companyId    企业id
     * @param codeSingleId 生码记录id
     * @param codeTotalNum 生码总数
     * @param userId       用户id
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public int createCodeSingle(Long companyId, Long codeSingleId, Long codeTotalNum, Long userId) {
        int correct = 0;
        List<Code> codeList = new ArrayList<>();
        //企业自增数
        CodeSingle codeSingle = codeSingleMapper.selectCodeSingleById(codeSingleId);
        long codeIndex = codeSingle.getIndexStart();


        for (int i = 0; i < codeTotalNum; i++) {
            Code code = new Code();
            code.setCodeIndex(codeIndex + i);
            code.setpCode(null);
            code.setCompanyId(companyId);
            code.setCodeType(AccConstants.CODE_TYPE_SINGLE);
            code.setCode(CodeRuleUtils.buildCode(companyId, CodeRuleUtils.CODE_PREFIX_S, code.getCodeIndex()));
            code.setSingleId(codeSingleId);
            codeList.add(code);

            //更新自增数
            if (i + 1 == codeTotalNum) {
                commonService.updateVal(companyId, codeSingle.getIndexEnd());
            }
        }


        int res = codeMapper.insertCodeForBatch(codeList);
        if (res > 0) {
            logger.info("single生码记录ID：" + codeSingleId + "生码成功");
            Map<String, Object> params = new HashMap<>();
            params.put("id", codeSingleId);
            params.put("status", AccConstants.CODE_RECORD_STATUS_FINISH);
            codeSingleMapper.insertCodeSingleStatus(params);
        } else {
            logger.error("single生码记录ID：" + codeSingleId + "生码异常");
        }
        return correct;
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public int updateStatusByAttrId(Long companyId, List<Long> idList, int status) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("idList", idList);
        params.put("status", status);
        return codeMapper.updateStatusByAttrId(params);
    }

    /**
     * 批量新增单码流转明细 【insertProvider形式】
     *
     * @param companyId   企业id
     * @param storageType 流转类型
     * @param list        流转码集合
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public int insertCodeFlowForBatchSingle(long companyId, int storageType, List<FlowVo> list) {
        //        return codeMapper.insertCodeFlowForBatchSingle(companyId, list);
        if (storageType == AccConstants.STORAGE_TYPE_IN) {
            return codeMapper.insertInCodeFlowForBatchSingleV2(companyId, list);
        } else if (storageType == AccConstants.STORAGE_TYPE_OUT) {
            return codeMapper.insertOutCodeFlowForBatchSingleV2(companyId, list);
        } else if (storageType == AccConstants.STORAGE_TYPE_TRANSFER) {
            return codeMapper.insertTransferCodeFlowForBatchSingleV2(companyId, list);
        } else if (storageType == AccConstants.STORAGE_TYPE_BACK) {
            return codeMapper.insertBackCodeFlowForBatchSingleV2(companyId, list);
        } else {
            throw new CustomException("未知的流转类型");
        }
    }

    /**
     * 根据物流流转信息查询码集合
     *
     * @param companyId
     * @param storageType
     * @param storageRecordId
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<String> selectCodeByStorage(long companyId, int storageType, long storageRecordId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("storageType", storageType);
        params.put("storageRecordId", storageRecordId);

        List<String> list = null;
        if (storageType == AccConstants.STORAGE_TYPE_IN) {
            list = codeMapper.selectInCodeByStorage(params);
        } else if (storageType == AccConstants.STORAGE_TYPE_OUT) {
            list = codeMapper.selectOutCodeByStorage(params);
        } else if (storageType == AccConstants.STORAGE_TYPE_TRANSFER) {
            list = codeMapper.selectTransferCodeByStorage(params);
        } else if (storageType == AccConstants.STORAGE_TYPE_BACK) {
            list = codeMapper.selectBackCodeByStorage(params);
        }
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

    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Code> selectCodeListByCodeOrIndex(Map<String, Object> map) {
        Code code = new Code();
        if (map.get("code") == null) {
            code.setCode("");
        } else {
            code.setCode(map.get("code").toString());
        }
        code.setCompanyId(Long.valueOf(map.get("companyId").toString()));
        Code temp = codeMapper.selectCode(code);
        if (temp == null) {
            throw new CustomException("未查询到相关码信息，请检查码最新流转状态");
        } else {
            if (temp.getCodeType().equals("box")) {
                return codeMapper.selectCodeListByCodeOrIndex(map);
            } else {
                if (temp.getpCode() == null) {
                    return codeMapper.selectCodeListByCodeOrIndex(map);
                }
                map.put("code", temp.getpCode());
                return codeMapper.selectCodeListByCodeOrIndex(map);
            }
        }
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public long getCodeCount(String codeStr) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", codeStr);
        map.put("companyId", Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
        List<Code> codeList = selectCodeListByCodeOrIndex(map);
        boolean flag = false;
        for (Code code : codeList) {
//            if (code.getCode().startsWith("P")) {
            if (CodeRuleUtils.getCodeType(code.getCode()).equals(AccConstants.CODE_TYPE_BOX)) {
                flag = true;
            }
        }
        if (flag) {
            return codeList.size() - 1;
        }
        return 1;
    }

    /**
     * 根据生码记录id查询码集合
     *
     * @param recordId
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Code> selectCodeListByRecord(Long companyId, Long recordId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("recordId", recordId);
        return codeMapper.selectCodeListByRecord(companyId, recordId);
    }

    /**
     * 根据生码记录id查询码集合
     *
     * @param singleId
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Code> selectCodeListBySingle(Long companyId, Long singleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("singleId", singleId);
        return codeMapper.selectCodeListBySingle(companyId, singleId);
    }


    /**
     * 保存生码属性
     *
     * @param companyId
     * @param codeRecordId
     * @param indexStart
     * @param indexEnd
     */
    private long saveCodeAttr(long companyId, long userId, long codeRecordId, long indexStart, long indexEnd) {
        CodeAttr codeAttr = new CodeAttr();
        codeAttr.setCompanyId(companyId);
        codeAttr.setTenantId(companyId);
        codeAttr.setRecordId(codeRecordId);
        codeAttr.setIndexStart(indexStart);
        codeAttr.setIndexEnd(indexEnd);
        codeAttr.setCreateUser(userId);
        codeAttr.setCreateTime(new Date());
        codeAttr.setUpdateUser(userId);
        codeAttr.setUpdateTime(new Date());
        codeAttrService.insertCodeAttr(codeAttr);
        return codeAttr.getId();
    }

    @Override
    public void updatePCodeByCode(Long companyId, String pCode, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("pCode", pCode);
        params.put("code", code);
        codeMapper.updatePCodeByCode(params);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Code> selectCodes(Map<String, Object> codeParam) {
        return codeMapper.selectCodes(codeParam);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public void updateCodeStorageByPCode(Code codeTemp) {
        codeMapper.updateCodeStorageByPCode(codeTemp);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public int updateCodeStorageByCode(Code codeRes) {
        return codeMapper.updateCodeStorageByCode(codeRes);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public void updateCodeAttrIdByPCode(Map<String, Object> param) {
        codeMapper.updateCodeAttrIdByPCode(param);
    }


    @Override
    @DataSource(DataSourceType.SHARDING)
    public Code selectCodeByCodeVal(String codeVal, Long companyId) {
        Code code = new Code();
        code.setCode(codeVal);
        code.setCompanyId(companyId);
        return codeMapper.selectCode(code);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Code> selectCodeRecordBySecurityCode(String securityCode, long companyId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("companyId", companyId);
        params.put("codeAcc", securityCode);
        return codeMapper.selectCodeRecordBySecurityCode(params);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public int unBindCodes(Map<String, Object> parmMap) {
        try {
            Long companyId = Long.valueOf(SecurityUtils.getLoginUserTopCompanyId());
            String codeVal = parmMap.get("code").toString();
            Code code = new Code();
            code.setCode(codeVal);
            code.setCompanyId(companyId);
            Code codeEntity = selectCode(code);//查询码产品基本信息

            if (codeEntity.getStorageRecordId() != null) {
                //解除库存(t_product_stock、t_product_stock_flow)
                Long inId = codeEntity.getStorageRecordId(); //查询入库表ID
                productStockFlowService.unBindProductStockFlowByInId(companyId, inId);
                //解除物流明细、解除出入库明细(t_in_code_flow、t_storage_in)
                storageInService.unBindStorageInByInId(companyId, inId);
            }
            if (codeEntity.getCodeAttrId() != null) {
                Long attrId = codeEntity.getCodeAttrId();//属性ID
                //解除码属性(t_code_attr)
                codeAttrService.deleteCodeAttrById(attrId);
                //解除绑定关系(t_code)
                unBindCodeByAttrId(companyId, attrId);
            }
            return 1;
        } catch (Exception e) {
            throw new CustomException("请输入正确的码！");
        }
    }
    @Override
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public int unBindCodesByPCodes(Map<String, List<String>> parmMap) {
        try {
            Long companyId = Long.valueOf(SecurityUtils.getLoginUserTopCompanyId());
            List<String> list = parmMap.get("codes");
            Code code = new Code();
            for (int i = 0; i < list.size(); i++) {
                code = new Code();
                code.setpCode(list.get(i));
                code.setCompanyId(companyId);
                Code codeEntity = selectCode(code);//查询码产品基本信息
                if (codeEntity.getStorageRecordId() != null) {
                    //解除库存(t_product_stock、t_product_stock_flow)
                    Long inId = codeEntity.getStorageRecordId(); //查询入库表ID
                    productStockFlowService.unBindProductStockFlowByInId(companyId, inId);
                    //解除物流明细、解除出入库明细(t_in_code_flow、t_storage_in)
                    storageInService.unBindStorageInByInId(companyId, inId);
                }
                if (codeEntity.getCodeAttrId() != null) {
                    Long attrId = codeEntity.getCodeAttrId();//属性ID
                    //解除码属性(t_code_attr)
                    codeAttrService.deleteCodeAttrById(attrId);
                    //解除绑定关系(t_code)
                    unBindCodeByAttrId(companyId, attrId);
                }
            }
            return 1;
        } catch (Exception e) {
            throw new CustomException("请输入正确的码！");
        }
    }


    @Override
    @DataSource(DataSourceType.SHARDING)
    public void unBindCodeByAttrId(Long companyId, Long attrId) {
        codeMapper.deletePCodeByAttrId(companyId, attrId);
        codeMapper.unBindCodeByAttrId(companyId, attrId);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public void deletePCodeBycode(String codeVal, Long companyId) {
        Code code = new Code();
        code.setCode(codeVal);
        code.setCompanyId(companyId);
        Code temp = codeMapper.selectCode(code);
        //判定有单箱关系解除
        if (temp.getpCode() != null) {
            if (temp.getCodeAttrId() == null) {
                throw new CustomException("请先对码进行赋值操作！");
            }
//            codeMapper.deletePCodeByAttrId(companyId, temp.getCodeAttrId());
            codeMapper.unBindCodeByPCode(companyId, temp.getpCode());
        }
    }


    @Override
    public void createCodeSingleByRule(Long companyId, CodeRule codeRule) {
        int correct = 0;
        List<Code> codeList = new ArrayList<>();
        //企业自增数
        CodeSingle codeSingle = codeSingleMapper.selectCodeSingleById(codeRule.getCodeSingleId());
        long codeIndex = codeSingle.getIndexStart();


        Code code = new Code();
        code.setCodeIndex(codeIndex + 1);
        code.setpCode(null);
        code.setCompanyId(companyId);
        code.setCodeType(AccConstants.CODE_TYPE_SINGLE);
        code.setCode(CodeRuleUtils.buildCodeByRule(companyId, CodeRuleUtils.CODE_PREFIX_S, code.getCode()));
        code.setSingleId(codeRule.getCodeSingleId());
        codeList.add(code);


        commonService.updateVal(companyId, codeSingle.getIndexEnd());


        int res = codeMapper.insertCodeForBatch(codeList);

    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<CRMInfoVo> getCRMInfo(String preFixUrl, Date beginTime, Date endTime) {
        long companyId = 102;//CodeRuleUtils.getCompanyIdByCode(code);暂时免密登录默认查询大艺数据
        List<CRMInfoVo> crmInfo = codeMapper.getCRMInfo(preFixUrl, companyId, beginTime, endTime);


        //2022-04-11效率问题弃用
//        List<String> codeStrs = crmInfo.stream().map(CRMInfoVo::getCode).collect(Collectors.toList());
//        Map<String,Object> codeParam = new HashMap<>();
//        codeParam.put("companyId",companyId);
//        codeParam.put("codes",codeStrs);
//        List<Code> lists= selectCodes(codeParam);
//        List<CRMInfoVo> commonList = crmInfo.stream()
//             .map((uA) -> {
//                     return lists.stream()
//                             .filter((uB) -> {
//                                     return StringUtils.equals(uB.getCode(), uA.getCode());
//                                 })
//                             .map((uB) -> {
//                                 uA.setProductName(uB.getCodeAttr().getProduct().getProductName());
//                                 uA.setBarCode(uB.getCodeAttr().getProduct().getBarCode());
//                                 uA.setProductNo(uB.getCodeAttr().getProduct().getProductNo());
//                                 uA.setCode(uB.getCode());
//                                 uA.setAttributeList(uB.getCodeAttr().getProduct().getAttributeList());
//                                 return uA;
//                             })
//                             .collect(Collectors.toList());
//                 }) // 结果类型 Steam<List<Hero>>
//             .flatMap(List::stream) // 结果类型 Steam<Hero>
//             .collect(Collectors.toList()); // 结果类型 List<Hero>
//        System.out.println(JSONUtil.toJsonStr(commonList));


        return crmInfo;
    }

    @Override
    public List<Long> selectStorageRecordIdsByAttrIds(Long companyId, List idList) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("idList", idList);
        return codeMapper.selectStorageRecordIdsByAttrIds(params);
    }
}
