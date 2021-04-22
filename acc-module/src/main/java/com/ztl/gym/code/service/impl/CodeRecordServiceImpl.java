package com.ztl.gym.code.service.impl;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.code.mapper.CodeMapper;
import com.ztl.gym.code.mapper.CodeRecordMapper;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    @Transactional(rollbackFor = Exception.class)
    public int createCodeRecord(long companyId, long num, String remark) {
        //TODO 判断企业是否生码中
        //TODO 生码数量不能为0
        //TODO 生码回显
        //TODO 生码规则工具类集成
        //TODO 出异常自增数bug,异步生码上一个事物未提交，导致生码时主键重复
        //TODO insertProvider形式修改
        //TODO 企业经销商区域设置
        //TODO 批量插入
        //TODO 数据源切换效率

        CodeRecord codeRecord = buildCodeRecord(companyId, AccConstants.GEN_CODE_TYPE_SINGLE, num, remark);
        int res = codeRecordMapper.insertCodeRecord(codeRecord);
        if (res > 0) {
            long codeRecordId = codeRecord.getId();
            //更新生码记录流水号
            long codeNo = commonService.selectCurrentVal(companyId);
            Map<String, Object> params = new HashMap<>();
            params.put("id", codeRecord.getId());
            params.put("indexStart", codeNo + 1);
            params.put("indexEnd", codeNo + num);
            codeRecordMapper.updateCodeIndex(params);

            //生码属性
            long codeAttrId = saveCodeAttr(companyId, codeRecordId, codeNo, num);

            //异步生码
            String message = codeAttrId + "-" + codeRecordId + "-" + companyId + "-" + num;
            stringRedisTemplate.convertAndSend("code.gen", message);
        }
        return res;
    }

    /**
     * 生码-套标
     *
     * @param companyId 企业id
     * @param num       每箱码数
     * @param remark    备注详情
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DataSource(DataSourceType.SHARDING)
    public int createPCodeRecord(long companyId, long num, String remark) {
        CodeRecord codeRecord = buildCodeRecord(companyId, AccConstants.GEN_CODE_TYPE_BOX, num, remark);
        int res = codeRecordMapper.insertCodeRecord(codeRecord);
        if (res > 0) {
            long codeRecordId = codeRecord.getId();
            //更新生码记录流水号
            long codeNo = commonService.selectCurrentVal(companyId);
            Map<String, Object> params = new HashMap<>();
            params.put("id", codeRecord.getId());
            params.put("indexStart", codeNo + 1);
            params.put("indexEnd", codeNo + 1 + num);
            codeRecordMapper.updateCodeIndex(params);

            //生码属性
            long codeAttrId = saveCodeAttr(companyId, codeRecordId, codeNo, num);

            //箱码
            //生码规则 企业id+日期+流水 【注意：客户扫码时没办法知道码所属企业，无法从对应分表查询，这里设置规则的时候需要把企业id带进去】
            String pCode = "P" + companyId + "/" + DateUtils.dateTimeNow();
            //异步生码
            String message = codeAttrId + "-" + codeRecordId + "-" + companyId + "-" + num + "-" + pCode;
            stringRedisTemplate.convertAndSend("code.gen", message);
        }
        return res;
    }

    /**
     * redis生码监听
     *
     * @param codeGenMessage
     */
    public void onPublishCode(String codeGenMessage) {
        System.out.println(codeGenMessage);
        log.info("onPublishCode {}", codeGenMessage);
        String[] codeGenMsgs = codeGenMessage.split("-");
        //生码属性id
        long codeAttrId = Long.parseLong(codeGenMsgs[0]);
        //生码记录id
        long codeRecordId = Long.parseLong(codeGenMsgs[1]);
        //企业id
        long companyId = Long.parseLong(codeGenMsgs[2]);
        //生码总数
        long codeTotalNum = Long.parseLong(codeGenMsgs[3]);
        //箱码
        String pCode = null;
        if (codeGenMsgs.length == 5) {
            pCode = codeGenMsgs[4];
        }
        codeService.createCode(companyId, codeRecordId, codeTotalNum, pCode, codeAttrId);
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
    public static CodeRecord buildCodeRecord(long companyId, int type, long num, String remark) {
        CodeRecord codeRecord = new CodeRecord();
        codeRecord.setCompanyId(companyId);
        codeRecord.setCount(num);
        codeRecord.setType(type);
        codeRecord.setStatus(AccConstants.CODE_RECORD_STATUS_WAIT);
        codeRecord.setRemark(remark);
        codeRecord.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        codeRecord.setUpdateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        codeRecord.setCreateTime(new Date());
        codeRecord.setUpdateTime(new Date());
        return codeRecord;
    }

    /**
     * 保存生码属性
     *
     * @param companyId
     * @param codeRecordId
     * @param codeNo
     * @param num
     */
    private long saveCodeAttr(long companyId, long codeRecordId, long codeNo, long num) {
        CodeAttr codeAttr = new CodeAttr();
        codeAttr.setCompanyId(companyId);
        codeAttr.setRecordId(codeRecordId);
        codeAttr.setIndexStart(codeNo + 1);
        codeAttr.setIndexEnd(codeNo + num);
        codeAttr.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        codeAttr.setCreateTime(new Date());
        codeAttr.setUpdateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        codeAttr.setUpdateTime(new Date());
        codeAttrService.insertCodeAttr(codeAttr);
        return codeAttr.getId();
    }
}
