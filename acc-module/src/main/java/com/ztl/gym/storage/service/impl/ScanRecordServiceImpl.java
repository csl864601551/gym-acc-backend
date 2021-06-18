package com.ztl.gym.storage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.area.service.ICompanyAreaService;
import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.exception.BaseException;
import com.ztl.gym.common.exception.CustomException;
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
    @Autowired
    private ICompanyAreaService companyAreaService;
    @Autowired
    private ICodeAttrService codeAttrService;


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
        }else{
            throw new CustomException("码格式错误！", HttpStatus.ERROR);
        }
        Code codeEntity = codeService.selectCode(code);//查询码产品你基本信息
        List<ScanRecord> scanList=scanRecordMapper.selectScanRecordList(scanRecord);//查询扫码记录
        List<Map<String,Object>> flowList=scanRecordMapper.selectFlowList(companyId,codeVal);//查询物流记录

        returnMap.put("codeEntity",codeEntity);
        returnMap.put("scanList",scanList);
        returnMap.put("flowList",flowList);
        /** 基本信息*/
        if(flowList.size()>0){
            returnMap.put("baseStatus","已"+flowList.get(0).get("title_name"));
        }else{
            returnMap.put("baseStatus","未入库");
        }
        if(codeEntity.getCodeType().equals("single")){
            returnMap.put("baseBoxType","小标");
            returnMap.put("baseBox","单码");
        }else{
            returnMap.put("baseBoxType","大标");
            returnMap.put("baseBox","箱码");
        }
        returnMap.put("batchNo",codeEntity.getCodeAttr().getBatchNo());
        returnMap.put("productName",codeEntity.getCodeAttr().getProductName());
        returnMap.put("codeAttrId",codeEntity.getCodeAttr().getId());

        //TODO 判定是否窜货

        return returnMap;
    }

    @Override
    public CompanyArea getIsMixInfo(CompanyArea area) {
        LoginUser user = SecurityUtils.getLoginUser();
        SysDept dept = user.getUser().getDept();
        Long deptId = dept.getDeptId();
        CompanyArea temp = new CompanyArea();

        if(area.getCodeAttrId()==null){
            throw new BaseException("未查询到相关销售区域");
        }else{
            //根据码属性ID获取对应的companyID和tenantID
            CodeAttr codeAttr=codeAttrService.selectCodeAttrById(area.getCodeAttrId());
            temp.setCompanyId(codeAttr.getCompanyId());
            temp.setTenantId(codeAttr.getTenantId());
        }
        List<CompanyArea> list = companyAreaService.selectCompanyAreaListV2(temp);
        if (area.getProvince() == null) {
            temp.setIsMix(2);
        } else{
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getProvince().equals("全部")) {//第一步判断销售地区是否未所有省；是则返回false,未窜货
                    temp.setIsMix(0);
                    break;
                } else if (list.get(i).getProvince().equals(area.getProvince())) {//否则判断是否有包含的省；有则往下判断
                    if (list.get(i).getCity().equals("全部")||list.get(i).getArea().equals("市辖区")) {//第二步判断销售地区是否未所有市；是则返回false,未窜货
                        temp.setIsMix(0);
                        break;
                    } else if (list.get(i).getCity().equals(area.getCity())) {//否则判断是否有包含的市；有则往下判断
                        if (list.get(i).getArea().equals("全部")||list.get(i).getArea().equals("市辖区")) {//第三步判断销售地区是否未所有区；是则返回false,未窜货
                            temp.setIsMix(0);
                            break;
                        } else if (list.get(i).getArea().equals(area.getArea())) {//否则判断是否有包含的区；有则返回false,未窜货
                            temp.setIsMix(0);
                            break;
                        } else {
                            temp.setIsMix(1);//区 没有返回true窜货
                        }
                    } else {
                        temp.setIsMix(1);//市 没有返回true窜货
                    }

                } else {
                    temp.setIsMix(1);//省 没有返回true窜货
                }
            }
        }
        String salesArea="";
        for (int j = 0; j < list.size(); j++) {
            salesArea+=list.get(j).getProvince().concat(" ")
                    .concat(list.get(j).getCity()==null?"":list.get(j).getCity()).concat(" ")
                    .concat(list.get(j).getArea()==null?"":list.get(j).getArea()).concat("\n");
        }
        temp.setSalesArea(salesArea);
        return temp;
    }


}
