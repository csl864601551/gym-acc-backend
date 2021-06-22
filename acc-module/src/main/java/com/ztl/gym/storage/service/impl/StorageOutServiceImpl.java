package com.ztl.gym.storage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztl.gym.code.domain.Code;
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
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.domain.StorageTransfer;
import com.ztl.gym.storage.mapper.StorageInMapper;
import com.ztl.gym.storage.service.IStorageInService;
import com.ztl.gym.storage.service.IStorageService;
import com.ztl.gym.storage.service.IStorageTransferService;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.StorageOutMapper;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.service.IStorageOutService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 出库Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@Service
public class StorageOutServiceImpl implements IStorageOutService {
    @Autowired
    private StorageOutMapper storageOutMapper;
    @Autowired
    private StorageInMapper storageInMapper;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ICodeService codeService;
    @Autowired
    private IStorageService storageService;
    @Autowired
    private IStorageInService storageInService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IStorageTransferService storageTransferService;
    @Autowired
    private IProductStockService productStockService;

    /**
     * 查询出库
     *
     * @param id 出库ID
     * @return 出库
     */
    @Override
    public StorageOut selectStorageOutById(Long id) {
        return storageOutMapper.selectStorageOutById(id);
    }

    /**
     * 查询出库列表
     *
     * @param storageOut 出库
     * @return 出库
     */
    @Override
    public List<StorageOut> selectStorageOutList(StorageOut storageOut) {
        return storageOutMapper.selectStorageOutList(storageOut);
    }


    /**
     * 新增出库
     *
     * @param storageOut 出库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @DataSource(DataSourceType.SHARDING)
    public int insertStorageOut(StorageOut storageOut) {
        storageOut.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        storageOut.setStatus(StorageOut.STATUS_WAIT);
        storageOut.setTenantId(commonService.getTenantId());
        storageOut.setStorageFrom(commonService.getTenantId());
        storageOut.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        storageOut.setCreateTime(new Date());
        if(storageOut.getThirdPartyFlag()!=null){
            storageOut.setUpdateTime(DateUtils.getNowDate());
            storageOut.setOutTime(DateUtils.getNowDate());
            long codeBoxCount=codeService.getCodeCount(storageOut.getCodes().get(0));
            long count=codeBoxCount*storageOut.getCodes().size();
            storageOut.setActOutNum(count);
            storageOut.setOutNum(count);
            for (int i = 0; i < storageOut.getCodes().size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("code", storageOut.getCodes().get(i).toString());
                map.put("companyId", Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
                Long inId=storageInMapper.selectInIdByCode(map); //查询入库表ID
                storageInMapper.updateInStatusById(inId);//更新入库表状态
            }
        }else{
            storageInMapper.updateInStatusByOut(storageOut);//更新入库表状态
        }
        int res=storageOutMapper.insertStorageOut(storageOut);//插入t_storage_out出库表
        if(storageOut.getThirdPartyFlag()!=null){
            Map<String,Object> map =new HashMap<>();
            map.put("id",storageOut.getId());
            map.put("codes",storageOut.getCodes());
            map.put("outsFlag","1");
            updateOutStatusByCode(map);//PDA端使用

        }
//        long storageRecordId=storageInMapper.selectInIdByExtraNo(storageOut.getExtraNo());//最新入库单号
//        List<String> codes=codeService.selectCodeByStorage( storageOut.getCompanyId(),AccConstants.STORAGE_TYPE_IN,storageRecordId);
//        storageService.addCodeFlow(AccConstants.STORAGE_TYPE_OUT, storageOut.getId(), codes.get(0));//插入物流码，原先在PDA执行

        return res;
    }

    /**
     * 修改出库
     *
     * @param storageOut 出库
     * @return 结果
     */
    @Override
    public int updateStorageOut(StorageOut storageOut) {
        storageOut.setUpdateTime(DateUtils.getNowDate());
        return storageOutMapper.updateStorageOut(storageOut);
    }

    /**
     * 批量删除出库
     *
     * @param ids 需要删除的出库ID
     * @return 结果
     */
    @Override
    public int deleteStorageOutByIds(Long[] ids) {
        return storageOutMapper.deleteStorageOutByIds(ids);
    }
    /**
     * 撤销出库
     *
     * @param id 需要删除的出库ID
     * @return 结果
     */
    @Override
    public int backStorageOutById(Long id,int status) {
        return storageOutMapper.backStorageOutById(id,status);
    }

    /**
     * 删除出库信息
     *
     * @param id 出库ID
     * @return 结果
     */
    @Override
    public int deleteStorageOutById(Long id) {
        return storageOutMapper.deleteStorageOutById(id);
    }

    /**
     * PDA出库动作
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @DataSource(DataSourceType.SHARDING)
    public int updateOutStatusByCode(Map<String, Object> map) {
        map.put("updateTime", DateUtils.getNowDate());
        map.put("updateUser", SecurityUtils.getLoginUser().getUser().getUserId());
        storageOutMapper.updateOutStatusByCode(map);//更新出库数量
        if(map.get("outsFlag")==null){
            storageService.addCodeFlow(AccConstants.STORAGE_TYPE_OUT, Long.valueOf(map.get("id").toString()), map.get("code").toString());//插入物流码，转移到PC执行
        }else{
            List list=(List)map.get("codes");
            for (int i = 0; i < list.size(); i++) {
                map.put("code",list.get(i));
                storageService.addCodeFlow(AccConstants.STORAGE_TYPE_OUT, Long.valueOf(map.get("id").toString()), map.get("code").toString());//插入物流码，转移到PC执行
            }
        }

        //查询出库单需要的相关信息
        StorageOut storageOut=storageOutMapper.selectStorageOutById(Long.valueOf(map.get("id").toString()));
        //判断是否调拨,执行更新调拨单
        String extraNo=storageOut.getExtraNo();
        if(extraNo!=null) {//判断非空
            if (extraNo.substring(0, 2).equals("DB")) {
                StorageTransfer storageTransfer = storageTransferService.selectStorageTransferByNo(extraNo);
                storageTransfer.setStatus(StorageTransfer.STATUS_DEALING);
                storageTransfer.setBatchNo(storageOut.getBatchNo());
                storageTransfer.setActTransferNum(storageOut.getActOutNum());
                storageTransferService.updateStorageTransfer(storageTransfer);
            }
        }

        //查询插入入库单需要的相关信息
        Map<String, Object> inMap = new HashMap<>();
        inMap.put("companyId", storageOut.getCompanyId());
        inMap.put("tenantId", storageOut.getStorageTo());
        inMap.put("extraNo", storageOut.getOutNo());
        inMap.put("inNo", commonService.getStorageNo(AccConstants.STORAGE_TYPE_IN));
        inMap.put("productId", storageOut.getProductId());
        inMap.put("batchNo", storageOut.getBatchNo());
        inMap.put("inNum", storageOut.getOutNum());
        inMap.put("storageFrom", commonService.getTenantId());
        inMap.put("code",map.get("code").toString());//新增插入物流码所需要的码信息
        return storageInService.insertStorageIn(inMap);//插入入库
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Map<String, Object>> getCodeDetailById(Long companyId, Integer id) {
        List<Map<String, Object>> list = storageOutMapper.getCodeDetailById(companyId, id);
        return list;
    }

    /**
     * 根据调拨单新增一条出库单
     *
     * @param transferId
     * @return
     */
    @Override
    public int insertByTransfer(long transferId) {
        StorageTransfer storageTransfer = storageTransferService.selectStorageTransferById(transferId);
        StorageOut storageOut = new StorageOut();
        storageOut.setCompanyId(storageTransfer.getCompanyId());
        storageOut.setTenantId(storageTransfer.getStorageFrom());
        storageOut.setStatus(StorageOut.STATUS_WAIT);
        storageOut.setOutNo(commonService.getStorageNo(AccConstants.STORAGE_TYPE_OUT));
        storageOut.setExtraNo(storageTransfer.getTransferNo());
        storageOut.setProductId(storageTransfer.getProductId());
        storageOut.setOutNum(storageTransfer.getTransferNum());
        storageOut.setStorageFrom(storageTransfer.getStorageFrom());
        storageOut.setStorageTo(storageTransfer.getStorageTo());
        storageOut.setFromStorageId(storageTransfer.getFromStorageId());
        storageOut.setRemark("调拨出库，调拨单号：" + storageTransfer.getTransferNo());
        return storageOutMapper.insertStorageOut(storageOut);
    }

    /**
     * 根据调拨单删除对应的出库单
     *
     * @param transferId
     * @return
     */
    @Override
    public int deleteByTransfer(long transferId) {
        StorageTransfer storageTransfer = storageTransferService.selectStorageTransferById(transferId);
        return storageOutMapper.deleteByTransfer(storageTransfer.getTransferNo());
    }
}
