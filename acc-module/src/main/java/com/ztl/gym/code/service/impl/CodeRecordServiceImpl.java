package com.ztl.gym.code.service.impl;

import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.code.mapper.CodeAttrMapper;
import com.ztl.gym.code.mapper.CodeMapper;
import com.ztl.gym.code.mapper.CodeRecordMapper;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.domain.GeneratorBean;
import com.ztl.gym.common.domain.GeneratorEnum;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.mapper.CommonMapper;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 生码记录Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-13
 */
@Service
public class CodeRecordServiceImpl implements ICodeRecordService {
    private static final Logger log = LoggerFactory.getLogger(CodeRecordServiceImpl.class);

    @Autowired
    private ICodeService codeService;

    @Autowired
    private ICodeAttrService codeAttrService;

    @Autowired
    private CodeRecordMapper codeRecordMapper;

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private CommonService commonService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private CodeAttrMapper codeAttrMapper;

    /**
     * 查询生码记录
     *
     * @param id 生码记录ID
     * @return 生码记录
     */
    @Override
    public CodeRecord selectCodeRecordById(Long id) {
        return codeRecordMapper.selectCodeRecordById(id);
    }


    /**
     * 查询生码记录
     *
     * @param codeIndex 生码记录ID
     * @return 生码记录
     */
    @Override
    public CodeRecord selectCodeRecordByIndex(long codeIndex,long companyId) {
        return codeRecordMapper.selectCodeRecordByIndex(codeIndex,companyId);
    }





    /**
     * 查询生码记录列表
     *
     * @param codeRecord 生码记录
     * @return 生码记录
     */
    @Override
    public List<CodeRecord> selectCodeRecordList(CodeRecord codeRecord) {
        return codeRecordMapper.selectCodeRecordList(codeRecord);
    }

    /**
     * 新增生码记录
     *
     * @param codeRecord 生码记录
     * @return 结果
     */
    @Override
    public int insertCodeRecord(CodeRecord codeRecord) {
        codeRecord.setCreateTime(DateUtils.getNowDate());
        return codeRecordMapper.insertCodeRecord(codeRecord);
    }

    /**
     * 修改生码记录
     *
     * @param codeRecord 生码记录
     * @return 结果
     */
    @Override
    public int updateCodeRecord(CodeRecord codeRecord) {
        codeRecord.setUpdateTime(DateUtils.getNowDate());
        return codeRecordMapper.updateCodeRecord(codeRecord);
    }

    /**
     * 批量删除生码记录
     *
     * @param ids 需要删除的生码记录ID
     * @return 结果
     */
    @Override
    public int deleteCodeRecordByIds(Long[] ids) {
        return codeRecordMapper.deleteCodeRecordByIds(ids);
    }

    /**
     * 删除生码记录信息
     *
     * @param id 生码记录ID
     * @return 结果
     */
    @Override
    public int deleteCodeRecordById(Long id) {
        return codeRecordMapper.deleteCodeRecordById(id);
    }

    /**
     * 生码-普通单码
     *
     * @param companyId 企业id
     * @param num       生码数量
     * @param remark    备注详情
     * @return
     */
    @Override
    public int createCodeRecord(long companyId,int isAcc, long num, String remark) {
        CodeRecord codeRecord = buildCodeRecord(companyId, AccConstants.GEN_CODE_TYPE_SINGLE, isAcc, 0, num, remark);
        int res = codeRecordMapper.insertCodeRecord(codeRecord);
        if (res > 0) {
            //生码属性
//            long codeAttrId = saveCodeAttr(companyId, codeRecord.getId(), codeRecord.getIndexStart(), codeRecord.getIndexEnd());
            long userId = SecurityUtils.getLoginUser().getUser().getUserId();

            //获取IP
            String localIp = "";
            try {
                InetAddress ip4 = Inet4Address.getLocalHost();
                localIp = ip4.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            //异步生码
            String message = userId + "^" + codeRecord.getId() + "^" + companyId + "^" + num + "^" + localIp;
            stringRedisTemplate.convertAndSend("acc.code.gen", message);
        }
        return res;
    }

    /**
     * 生码 【多箱】
     *
     * @param companyId 企业id
     * @param boxCount  箱数
     * @param num       每箱码数
     * @param remark    备注详情
     * @return
     */
    @Override
    public int createPCodeRecord(long companyId,int isAcc, long boxCount, long num, String remark) {
        CodeRecord codeRecord = buildCodeRecord(companyId, AccConstants.GEN_CODE_TYPE_BOX,isAcc, boxCount, num, remark);
        int res = codeRecordMapper.insertCodeRecord(codeRecord);
        if (res > 0) {
            //生码属性
//            long codeAttrId = saveCodeAttr(companyId, codeRecord.getId(), codeRecord.getIndexStart(), codeRecord.getIndexEnd());
            long userId = SecurityUtils.getLoginUser().getUser().getUserId();

            //获取IP
            String localIp = "";
            try {
                InetAddress ip4 = Inet4Address.getLocalHost();
                localIp = ip4.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            //异步生码
            String message = userId + "^" + codeRecord.getId() + "^" + companyId + "^" + num + "^" + localIp + "^" + boxCount;
            stringRedisTemplate.convertAndSend("acc.code.gen", message);
        }
        return res;
    }

    /**
     * redis生码监听
     *
     * @param codeGenMessage
     */
    public void onPublishCode(String codeGenMessage) {
        //获取IP
        String localIp = "";
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            localIp = ip4.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println(codeGenMessage);
        log.info("onPublishCode {}", codeGenMessage);
        try {
            String[] codeGenMsgs = codeGenMessage.split("\\^");
            //生码属性id
            long userId = Long.parseLong(codeGenMsgs[0]);
            //生码记录id
            long codeRecordId = Long.parseLong(codeGenMsgs[1]);
            //企业id
            long companyId = Long.parseLong(codeGenMsgs[2]);
            //生码总数
            long codeTotalNum = Long.parseLong(codeGenMsgs[3]);
            //ip
            String ip = codeGenMsgs[4];
            //箱码
            long boxCount = 0;
            if (codeGenMsgs.length == 6) {
                boxCount = Long.parseLong(codeGenMsgs[5]);
            }
            if (localIp.equals(ip)) {
                //获取当前最大码属性id值
                Long attrId = codeAttrMapper.getMaxAttrId();
                //跟新生码属性序列
                if(boxCount > 0){
                    commonService.updateGeneratorVal(companyId, attrId,boxCount, GeneratorEnum.ATTR.getType());
                }else{
                    commonService.updateGeneratorVal(companyId, attrId,1, GeneratorEnum.ATTR.getType());
                }
                codeService.createCode(companyId, codeRecordId, codeTotalNum, boxCount, userId, attrId);
            }
        } catch (Exception e) {
            log.error("Exception is {}", e);
            throw new CustomException("接收数据异常，请检查码数据格式！", HttpStatus.ERROR);
        }

    }

    /**
     * 构建生码记录
     *
     * @param companyId
     * @param type
     * @param num
     * @param remark
     * @return
     */
    public static CodeRecord buildCodeRecord(long companyId, int type,int isAcc, long boxCount, long num, String remark) {
        CodeRecord codeRecord = new CodeRecord();
        //获取并更新生码记录流水号
        String codePrefix = null;
        if (type == AccConstants.GEN_CODE_TYPE_SINGLE) {
            codePrefix = CodeRuleUtils.CODE_PREFIX_S;
        } else if (type == AccConstants.GEN_CODE_TYPE_BOX) {
            codePrefix = CodeRuleUtils.CODE_PREFIX_B;
        }
        String codeNoStr = CodeRuleUtils.getCodeIndex(companyId, boxCount, num, codePrefix);
        String[] codeIndexs = codeNoStr.split("-");
        codeRecord.setIndexStart(Long.parseLong(codeIndexs[0]) + 1);
        codeRecord.setIndexEnd(Long.parseLong(codeIndexs[1]));
        //基础属性
        codeRecord.setCompanyId(companyId);
        codeRecord.setBoxCount(boxCount);
        codeRecord.setSingleCount(num);
        long totalNum = num;
        if (boxCount > 0) {
            totalNum = boxCount * totalNum + boxCount;
        }
        codeRecord.setCount(totalNum);
        codeRecord.setType(type);
        codeRecord.setIsAcc(isAcc);
        codeRecord.setStatus(AccConstants.CODE_RECORD_STATUS_WAIT);
        codeRecord.setRemark(remark);
        codeRecord.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        codeRecord.setUpdateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        codeRecord.setCreateTime(new Date());
        codeRecord.setUpdateTime(new Date());
        return codeRecord;
    }


    /**
     * 生码总量
     *
     * @param map 部门信息
     * @return 结果
     */
    @Override
    public int selectCodeNum(Map<String, Object> map) {
        return codeRecordMapper.selectCodeNum(map);
    }


    /**
     * 套标生码总量统计图
     *
     * @param map 部门信息
     * @return 结果
     */
    @Override
    public List<Map<String,Object>> selectCodeByDate(Map<String, Object> map) {
        return codeRecordMapper.selectCodeByDate(map);
    }



    /**
     * 单码生码总量统计图
     *
     * @param map 部门信息
     * @return 结果
     */
    @Override
    public List<Map<String,Object>> selectCodeSingByDate(Map<String, Object> map) {
        return codeRecordMapper.selectCodeSingByDate(map);
    }
}
