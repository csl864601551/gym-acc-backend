package com.ztl.gym.mix.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.mix.domain.MixRecord;
import com.ztl.gym.mix.mapper.MixRecordMapper;
import com.ztl.gym.mix.service.IMixRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 窜货记录Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-28
 */
@Service
public class MixRecordServiceImpl implements IMixRecordService
{
    @Autowired
    private MixRecordMapper mixRecordMapper;

    /**
     * 查询窜货记录
     *
     * @param id 窜货记录ID
     * @return 窜货记录
     */
    @Override
    public MixRecord selectMixRecordById(Long id)
    {
        return mixRecordMapper.selectMixRecordById(id);
    }

    /**
     * 查询窜货记录列表
     *
     * @param mixRecord 窜货记录
     * @return 窜货记录
     */
    @Override
    public List<MixRecord> selectMixRecordList(MixRecord mixRecord)
    {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            mixRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        return mixRecordMapper.selectMixRecordList(mixRecord);
    }

    /**
     * 新增窜货记录
     *
     * @param mixRecord 窜货记录
     * @return 结果
     */
    @Override
    public int insertMixRecord(MixRecord mixRecord)
    {
        if(StrUtil.isNotEmpty(mixRecord.getFromType())){
            if(mixRecord.getFromType().equals("0")){
                Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
                if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
                    mixRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
                }
            }else{
                mixRecord.setCreateUser(AccConstants.WEIXIN_ADMIN_ID);
            }
        }else{
            Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
            if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
                mixRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
            }
            mixRecord.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        }
        mixRecord.setCreateTime(DateUtils.getNowDate());
        return mixRecordMapper.insertMixRecord(mixRecord);
    }

    /**
     * 修改窜货记录
     *
     * @param mixRecord 窜货记录
     * @return 结果
     */
    @Override
    public int updateMixRecord(MixRecord mixRecord)
    {
        mixRecord.setUpdateTime(DateUtils.getNowDate());
        return mixRecordMapper.updateMixRecord(mixRecord);
    }

    /**
     * 批量删除窜货记录
     *
     * @param ids 需要删除的窜货记录ID
     * @return 结果
     */
    @Override
    public int deleteMixRecordByIds(Long[] ids)
    {
        return mixRecordMapper.deleteMixRecordByIds(ids);
    }

    /**
     * 删除窜货记录信息
     *
     * @param id 窜货记录ID
     * @return 结果
     */
    @Override
    public int deleteMixRecordById(Long id)
    {
        return mixRecordMapper.deleteMixRecordById(id);
    }
}
