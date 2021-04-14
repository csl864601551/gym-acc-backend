package com.ztl.gym.code.service.impl;

import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.code.mapper.CodeRecordMapper;
import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.DateUtils;
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
    private CodeRecordMapper codeRecordMapper;

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
        CodeRecord codeRecord = new CodeRecord();
        codeRecord.setCompanyId(companyId);
        codeRecord.setCount(num);
        codeRecord.setType(AccConstants.GEN_CODE_TYPE_SINGLE);
        codeRecord.setStatus(AccConstants.CODE_RECORD_STATUS_WAIT);
        codeRecord.setCreateUser(1L);
        codeRecord.setCreateTime(new Date());
        codeRecord.setUpdateUser(2L);
        codeRecord.setUpdateTime(new Date());
//        codeRecord.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId()); TODO LJ
//        codeRecord.setUpdateUser(SecurityUtils.getLoginUser().getUser().getUserId());
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

            //异步生码
            String message = "code-" + codeRecordId + "-" + companyId + "-" + num;
            stringRedisTemplate.convertAndSend("code.gen", message);
        }
        return res;
    }

    // redis生码监听
    public void onPublishCode(String codeGenMessage) {
        System.out.println(codeGenMessage);
        log.info("onPublishCode {}", codeGenMessage);
        String[] codeGenMsgs = codeGenMessage.split("-");
        //生码记录id
        long codeRecordId = Long.parseLong(codeGenMsgs[1]);
        //企业id
        long companyId = Long.parseLong(codeGenMsgs[2]);
        //生码总数
        long codeTotalNum = Long.parseLong(codeGenMsgs[3]);
        codeService.createCode(companyId, codeRecordId, codeTotalNum);
    }
}
