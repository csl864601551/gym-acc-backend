package com.ztl.gym.storage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.product.service.IProductStockService;
import com.ztl.gym.storage.domain.StorageBack;
import com.ztl.gym.storage.domain.StorageTransfer;
import com.ztl.gym.storage.domain.vo.StorageVo;
import com.ztl.gym.storage.service.IStorageBackService;
import com.ztl.gym.storage.service.IStorageService;
import com.ztl.gym.storage.service.IStorageTransferService;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.StorageInMapper;
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.service.IStorageInService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@Service
public class StorageInServiceImpl implements IStorageInService {
    @Autowired
    private StorageInMapper storageInMapper;
    @Autowired
    private IStorageService storageService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ICodeService codeService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IStorageBackService storageBackService;
    @Autowired
    private IStorageTransferService storageTransferService;
    @Autowired
    private IProductStockService productStockService;

    /**
     * 查询入库
     *
     * @param id 入库ID
     * @return 入库
     */
    @Override
    public StorageIn selectStorageInById(Long id) {
        return storageInMapper.selectStorageInById(id);
    }

    /**
     * 查询入库列表
     *
     * @param storageIn 入库
     * @return 入库
     */
    @Override
    public List<StorageIn> selectStorageInList(StorageIn storageIn) {
        return storageInMapper.selectStorageInList(storageIn);
    }

    /**
     * 新增入库
     *
     * @param map 入库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @DataSource(DataSourceType.SHARDING)
    public int insertStorageIn(Map<String, Object> map) {
        map.put("companyId", SecurityUtils.getLoginUserTopCompanyId());
        if (map.get("tenantId") == null) {
            map.put("tenantId", commonService.getTenantId());
        }else if(map.get("tenantId").toString().equals("0")){
            map.put("tenantId", commonService.getTenantId());
        }
        map.put("inType", StorageIn.IN_TYPE_COMMON);
        map.put("storageTo", SecurityUtils.getLoginUserCompany().getDeptId());
        map.put("createTime", DateUtils.getNowDate());
        if(map.get("thirdPartyFlag")!=null){
            map.put("updateTime", DateUtils.getNowDate());
            map.put("inTime", DateUtils.getNowDate());
            long codeBoxCount=codeService.getCodeCount(((List)map.get("codes")).get(0).toString());
            long count=codeBoxCount*(((List)map.get("codes")).size());
            map.put("inNum", count);
            map.put("actInNum", count);
        }
        map.put("createUser", SecurityUtils.getLoginUser().getUser().getUserId());
        int result = storageInMapper.insertStorageIn(map);//新增t_storage_in入库表
//        String id = map.get("id").toString();
//        System.out.println("id=="+id);
        if(map.get("thirdPartyFlag")!=null){
            List list=(List)map.get("codes");
            for (int i = 0; i < list.size(); i++) {
                map.put("code",list.get(i));
                updateInStatusByCode(map);//PDA端使用
            }
        }
        return result;
    }

    /**
     * 修改入库
     *
     * @param storageIn 入库
     * @return 结果
     */
    @Override
    public int updateStorageIn(StorageIn storageIn) {
        storageIn.setUpdateTime(DateUtils.getNowDate());
        return storageInMapper.updateStorageIn(storageIn);
    }

    /**
     * 批量删除入库
     *
     * @param ids 需要删除的入库ID
     * @return 结果
     */
    @Override
    public int deleteStorageInByIds(Long[] ids) {
        return storageInMapper.deleteStorageInByIds(ids);
    }

    /**
     * 删除入库信息
     *
     * @param id 入库ID
     * @return 结果
     */
    @Override
    public int deleteStorageInById(Long id) {
        return storageInMapper.deleteStorageInById(id);
    }

    /**
     * 获取相关码产品信息
     *
     * @param code
     * @return
     */
    @Override
    public Map<String, Object> getCodeInfo(String code) {
        Map<String, Object> map = new HashMap<>();
        map = storageInMapper.getCodeInfo(code);//获取码产品信息
        List<Map<String, Object>> listMap = storageInMapper.getCodeDetail(code);//获取码产品明细
        map.put("listMap", listMap);
        return map;
    }

    /**
     * 企业确认入库
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @DataSource(DataSourceType.SHARDING)
    public int updateInStatusByCode(Map<String, Object> map) {
        map.put("status", StorageIn.STATUS_NORMAL);
        map.put("inTime", DateUtils.getNowDate());
        map.put("updateTime", DateUtils.getNowDate());
        map.put("updateUser", SecurityUtils.getLoginUser().getUser().getUserId());
        int res = storageInMapper.updateInStatusByCode(map);//更新企业入库信息
        StorageIn storageIn = storageInMapper.selectStorageInById(Long.valueOf(map.get("id").toString()));//查询入库单信息
        String extraNo = storageIn.getExtraNo();//相关单号
        storageService.addCodeFlow(AccConstants.STORAGE_TYPE_IN, Long.valueOf(map.get("id").toString()), map.get("code").toString());//插入码流转明细，转移到PDA执行
        if (extraNo != null) {//判断非空
            //判断是否调拨,执行更新调拨单
            if (extraNo.substring(0, 2).equals("DB")) {
                StorageTransfer storageTransfer = storageTransferService.selectStorageTransferByNo(extraNo);
                storageTransfer.setStatus(StorageTransfer.STATUS_FINISH);
                storageTransfer.setToStorageId(storageIn.getToStorageId());
                storageTransferService.updateStorageTransfer(storageTransfer);//更新调拨表
            }
        }
        return res;
    }

    /**
     * PC经销商确认入库
     *
     * @param map
     * @return 确认收货
     * 与上面的区别是否存在码信息
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @DataSource(DataSourceType.SHARDING)
    public int updateTenantIn(Map<String, Object> map) {
        map.put("status", StorageIn.STATUS_NORMAL);
        map.put("inTime", DateUtils.getNowDate());
        map.put("updateTime", DateUtils.getNowDate());
        map.put("updateUser", SecurityUtils.getLoginUser().getUser().getUserId());
        int res = storageInMapper.updateTenantIn(map);//更新入库表
        StorageIn storageIn = storageInMapper.selectStorageInById(Long.valueOf(map.get("id").toString()));//查询入库单信息
        String extraNo = storageIn.getExtraNo();//相关单号
        Long companyId = storageIn.getCompanyId();//顶级企业ID

        long storageRecordId = 0;
        try {
            storageRecordId = storageInMapper.selectOutIdByExtraNo(extraNo);//最新入库单号
        }catch (Exception e){
            throw new CustomException("未查询到相关单号,请检查入库单来源");
        }
        List<String> codes = codeService.selectCodeByStorage(companyId, AccConstants.STORAGE_TYPE_OUT, storageRecordId);
        boolean flag=true;
        for (int i = 0; i < codes.size(); i++) {
            if(codes.get(i).startsWith("20")){
                storageService.addCodeFlow(AccConstants.STORAGE_TYPE_IN, Long.valueOf(map.get("id").toString()), codes.get(i));//插入码流转明细，转移到PDA执行
                flag=false;
            }
        }
        if(flag){
            for (int i = 0; i < codes.size(); i++) {
                storageService.addCodeFlow(AccConstants.STORAGE_TYPE_IN, Long.valueOf(map.get("id").toString()), codes.get(i));//插入码流转明细，转移到PDA执行
            }

        }
        //判断是否调拨,执行更新调拨单
        if (extraNo.substring(0, 2).equals("DB")) {
            StorageTransfer storageTransfer = storageTransferService.selectStorageTransferByNo(extraNo);
            storageTransfer.setStatus(StorageTransfer.STATUS_FINISH);
            storageTransfer.setToStorageId(storageIn.getToStorageId());
            storageTransferService.updateStorageTransfer(storageTransfer);//更新调拨表
        }
        return res;
    }

    /**
     * 查询入库单中的码明细
     *
     * @param companyId
     * @param id
     * @return
     */
    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Map<String, Object>> getCodeDetailById(Long companyId, Integer id) {
        List<Map<String, Object>> list = storageInMapper.getCodeDetailById(companyId, id);
        return list;
    }

    /**
     * 根据相关单号查询入库单
     *
     * @param extraNo
     * @return
     */
    @Override
    public StorageIn selectStorageInByExtraNo(String extraNo) {
        return storageInMapper.selectStorageInByExtraNo(extraNo);
    }

    /**
     * 退货入库
     *
     * @param storageBack
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long insertStorageInForBack(StorageBack storageBack) {
        StorageIn storageIn = new StorageIn();
        storageIn.setCompanyId(storageBack.getCompanyId());
        storageIn.setTenantId(SecurityUtils.getLoginUserCompany().getDeptId());
        storageIn.setInType(StorageIn.IN_TYPE_BACK);
        storageIn.setExtraNo(storageBack.getBackNo());
        storageIn.setStatus(StorageIn.STATUS_NORMAL);
        storageIn.setInNo(commonService.getStorageNo(AccConstants.STORAGE_TYPE_IN));
        storageIn.setProductId(storageBack.getProductId());
        storageIn.setBatchNo(storageBack.getBatchNo());
        storageIn.setInNum(storageBack.getBackNum());
        storageIn.setActInNum(storageBack.getBackNum());
        storageIn.setStorageFrom(storageBack.getStorageFrom());
        storageIn.setStorageTo(storageBack.getStorageTo());
        storageIn.setFromStorageId(storageBack.getFromStorageId());
        storageIn.setToStorageId(storageBack.getToStorageId());
        storageIn.setRemark(storageBack.getRemark());
        storageIn.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        storageIn.setCreateTime(new Date());
        storageIn.setUpdateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        storageIn.setUpdateTime(new Date());
        int inres = storageInMapper.insertStorageInV2(storageIn);
        if (inres > 0) {
            return storageIn.getId();
        }
        return 0L;
    }
}
