package com.ztl.gym.code.service.impl;

import java.util.List;
import java.util.Objects;

import cn.hutool.core.collection.CollectionUtil;
import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAccRecord;
import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.code.domain.SecurityCodeRecord;
import com.ztl.gym.code.domain.vo.ScanSecurityCodeOutBean;
import com.ztl.gym.code.mapper.CodeAccRecordMapper;
import com.ztl.gym.code.mapper.CodeRecordMapper;
import com.ztl.gym.code.mapper.SecurityCodeRecordMapper;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.code.service.ISecurityCodeRecordService;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.service.IProductService;
import com.ztl.gym.system.mapper.SysDeptMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 防伪记录 company_id字段分Service业务层处理
 *
 * @author ruoyi
 * @date 2021-08-02
 */
@Service
public class SecurityCodeRecordServiceImpl implements ISecurityCodeRecordService {
    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(SecurityCodeRecordServiceImpl.class);
    @Autowired
    private SecurityCodeRecordMapper securityCodeRecordMapper;
    @Autowired
    private ICodeService codeService;
    @Autowired
    private CodeAccRecordMapper codeAccRecordMapper;
    @Autowired
    private CodeRecordMapper codeRecordMapper;
    @Autowired
    private IProductService productService;
    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 查询防伪记录 company_id字段分
     *
     * @param id 防伪记录 company_id字段分ID
     * @return 防伪记录 company_id字段分
     */
    @Override
    public SecurityCodeRecord selectSecurityCodeRecordById(Long id) {
        return securityCodeRecordMapper.selectSecurityCodeRecordById(id);
    }

    /**
     * 查询防伪记录 company_id字段分列表
     *
     * @param securityCodeRecord 防伪记录 company_id字段分
     * @return 防伪记录 company_id字段分
     */
    @Override
    public List<SecurityCodeRecord> selectSecurityCodeRecordList(SecurityCodeRecord securityCodeRecord) {
        return securityCodeRecordMapper.selectSecurityCodeRecordList(securityCodeRecord);
    }

    /**
     * 新增防伪记录 company_id字段分
     *
     * @param securityCodeRecord 防伪记录 company_id字段分
     * @return 结果
     */
    @Override
    public int insertSecurityCodeRecord(SecurityCodeRecord securityCodeRecord) {
        securityCodeRecord.setCreateTime(DateUtils.getNowDate());
        return securityCodeRecordMapper.insertSecurityCodeRecord(securityCodeRecord);
    }


    /**
     * 修改防伪记录 company_id字段分
     *
     * @param securityCodeRecord 防伪记录 company_id字段分
     * @return 结果
     */
    @Override
    public int updateSecurityCodeRecord(SecurityCodeRecord securityCodeRecord) {
        securityCodeRecord.setUpdateTime(DateUtils.getNowDate());
        return securityCodeRecordMapper.updateSecurityCodeRecord(securityCodeRecord);
    }

    /**
     * 批量删除防伪记录 company_id字段分
     *
     * @param ids 需要删除的防伪记录 company_id字段分ID
     * @return 结果
     */
    @Override
    public int deleteSecurityCodeRecordByIds(Long[] ids) {
        return securityCodeRecordMapper.deleteSecurityCodeRecordByIds(ids);
    }

    /**
     * 删除防伪记录 company_id字段分信息
     *
     * @param id 防伪记录 company_id字段分ID
     * @return 结果
     */
    @Override
    public int deleteSecurityCodeRecordById(Long id) {
        return securityCodeRecordMapper.deleteSecurityCodeRecordById(id);
    }

    /**
     * 扫描防伪码操作
     *
     * @param securityCodeRecord 防伪记录
     * @return 响应
     */
    @Override
    public ScanSecurityCodeOutBean getSecurityCodeInfo(SecurityCodeRecord securityCodeRecord) {
        logger.info("the method getSecurityCodeInfo enter");
        //防伪码查询，先去查询t_code表，如果码记录没有查到防伪码，则去t_code_acc防伪码表查询
        //查询t_code表是否有与该防伪关联的防窜码
        List<Code> codes = codeService.selectCodeRecordBySecurityCode(securityCodeRecord.getCodeAcc(), securityCodeRecord.getCompanyId());
        List<SecurityCodeRecord> securityCodeRecords = securityCodeRecordMapper.selectRecordsByAccCode(securityCodeRecord.getCodeAcc());
        ScanSecurityCodeOutBean scanSecurityCodeOutBean = null;
        //如果没有记录去t_code_acc表里查询
        if (!CollectionUtil.isEmpty(codes)) {
            scanSecurityCodeOutBean = buildScanResultBeanByCode(securityCodeRecord, codes.get(0), securityCodeRecords);
        } else {
            logger.info("单码记录表不存在该防伪码记录。");
            //去t_code_acc防伪码表查询并获取防伪记录
            List<CodeAccRecord> codeAccRecords = codeAccRecordMapper.selectRecordByAccCode(securityCodeRecord.getCodeAcc());
            if (CollectionUtil.isEmpty(codeAccRecords)) {
                logger.error("防伪码不存在，验证失败！");
                throw new CustomException("验证失败", HttpStatus.ERROR);
            }
            scanSecurityCodeOutBean = buildScanResultBeanByAccCode(securityCodeRecord, codeAccRecords.get(0), securityCodeRecords);
        }
        securityCodeRecordMapper.insertSecurityCodeRecord(securityCodeRecord);
        logger.info("防伪码扫描记录入库成功。");
        return scanSecurityCodeOutBean;
    }

    /**
     * 扫描标识码获取防伪校验
     *
     * @param securityCodeRecord 防伪记录
     * @return 响应
     */
    @Override
    public ScanSecurityCodeOutBean getSecurityCodeInfoByCode(SecurityCodeRecord securityCodeRecord) {
        logger.info("the method getSecurityCodeInfoByCode enter");
        //根据code去t_code表获取响应的防伪码
        Code code = codeService.selectCodeByCodeVal(securityCodeRecord.getCode(),securityCodeRecord.getCompanyId());
        ScanSecurityCodeOutBean scanSecurityCodeOutBean = new ScanSecurityCodeOutBean();
        //如果没有对应防伪码，返回空
        if (StringUtils.isBlank(code.getCodeAcc())) {
            logger.info("没有对应的防伪码");
            return scanSecurityCodeOutBean;
        }
        List<SecurityCodeRecord> securityCodeRecords = securityCodeRecordMapper.selectRecordsByAccCode(securityCodeRecord.getCodeAcc());
        scanSecurityCodeOutBean = buildScanResultBeanByCode(securityCodeRecord, code, securityCodeRecords);
        securityCodeRecordMapper.insertSecurityCodeRecord(securityCodeRecord);
        logger.info("防伪码扫描记录入库成功。");
        return scanSecurityCodeOutBean;
    }


    /**
     * 如果防伪码存在在t_code表，构建返回对象
     *
     * @param securityCodeRecord
     * @param code
     * @param securityCodeRecords
     * @return
     */
    private ScanSecurityCodeOutBean buildScanResultBeanByCode(SecurityCodeRecord securityCodeRecord, Code code, List<SecurityCodeRecord> securityCodeRecords) {
        ScanSecurityCodeOutBean scanSecurityCodeOutBean = new ScanSecurityCodeOutBean();
        scanSecurityCodeOutBean.setCode(code.getCode());
        scanSecurityCodeOutBean.setCodeAcc(code.getCodeAcc());
        scanSecurityCodeOutBean.setCount(CollectionUtil.isEmpty(securityCodeRecords) ? 0 : securityCodeRecords.size());
        scanSecurityCodeOutBean.setCompany(getCompanyName(securityCodeRecord.getCompanyId()));
        //判断是否存在扫防伪码记录
        if (!CollectionUtil.isEmpty(securityCodeRecords)) {
            scanSecurityCodeOutBean.setFirstQueryTime(securityCodeRecords.get(0).getCreateTime());
            scanSecurityCodeOutBean.setLastQueryTime(securityCodeRecords.get(securityCodeRecords.size() - 1).getCreateTime());
        }
        CodeRecord codeRecord = codeRecordMapper.selectCodeRecordByIndex(code.getCodeIndex(), code.getCompanyId());
        if (!Objects.isNull(codeRecord)) {
            securityCodeRecord.setProductId(codeRecord.getProductId());
        }
        logger.info("该防伪码对应的标识码没有生码记录！");
        Product product = productService.selectTProductById(codeRecord.getProductId());
        if (!Objects.isNull(product)) {
            scanSecurityCodeOutBean.setProduct(product.getProductName());
            scanSecurityCodeOutBean.setMoreContent(product.getContent2());
            scanSecurityCodeOutBean.setOnceContent(product.getContent1());
            scanSecurityCodeOutBean.setOnceTemplateContent(product.getTemplateContent1());
            scanSecurityCodeOutBean.setMoreTemplateContent(product.getTemplateContent2());
        }
        logger.info("该防伪码对应的标识码没有关联产品！");
        //给防伪记录塞值
        securityCodeRecord.setCode(code.getCode());
        securityCodeRecord.setCreateTime(DateUtils.getNowDate());
        return scanSecurityCodeOutBean;
    }

    /**
     * 如果防伪码存在在t_code_acc表，表示单独生码与产品没有关联。且SecurityCodeRecord记录无需产品id,产品名称和标识码
     *
     * @param securityCodeRecord
     * @param codeAccRecord
     * @param securityCodeRecords
     * @return
     */
    private ScanSecurityCodeOutBean buildScanResultBeanByAccCode(SecurityCodeRecord securityCodeRecord, CodeAccRecord codeAccRecord, List<SecurityCodeRecord> securityCodeRecords) {
        ScanSecurityCodeOutBean scanSecurityCodeOutBean = new ScanSecurityCodeOutBean();
        scanSecurityCodeOutBean.setCodeAcc(securityCodeRecord.getCodeAcc());
        scanSecurityCodeOutBean.setCount(CollectionUtil.isEmpty(securityCodeRecords) ? 0 : securityCodeRecords.size());
        scanSecurityCodeOutBean.setCompany(getCompanyName(securityCodeRecord.getCompanyId()));
        //判断是否存在扫防伪码记录
        if (!CollectionUtil.isEmpty(securityCodeRecords)) {
            scanSecurityCodeOutBean.setFirstQueryTime(securityCodeRecords.get(0).getCreateTime());
            scanSecurityCodeOutBean.setLastQueryTime(securityCodeRecords.get(securityCodeRecords.size() - 1).getCreateTime());
        }
        if (!Objects.isNull(codeAccRecord)) {
            scanSecurityCodeOutBean.setMoreContent(codeAccRecord.getOnceContent());
            scanSecurityCodeOutBean.setOnceContent(codeAccRecord.getMoreContent());
            scanSecurityCodeOutBean.setOnceTemplateContent(codeAccRecord.getOnceTemplateContent());
            scanSecurityCodeOutBean.setMoreTemplateContent(codeAccRecord.getMoreTemplateContent());
        }
        logger.info("该防伪码对应的标识码没有关联产品！");
        //给防伪记录塞值
        securityCodeRecord.setCreateTime(DateUtils.getNowDate());
        return scanSecurityCodeOutBean;
    }

    /**
     * 获取企业名称
     * @param companyId 企业id
     * @return 企业名称
     */
    private String getCompanyName(Long companyId){
        SysDept sysDept =  deptMapper.selectDeptById(companyId);
        if(Objects.isNull(sysDept)){
            return "";
        }
        return sysDept.getDeptName();
    }
}
