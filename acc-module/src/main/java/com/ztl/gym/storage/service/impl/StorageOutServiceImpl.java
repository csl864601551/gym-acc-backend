package com.ztl.gym.storage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.enums.DataSourceType;
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
     * @param map 出库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int insertStorageOut(Map<String, Object> map) {
        map.put("tenantId", commonService.getTenantId());
        map.put("createTime", (DateUtils.getNowDate()));
        map.put("createUser", SecurityUtils.getLoginUser().getUser().getUserId());

        //storageInMapper.updateProductStock(map);//TODO 更新t_product_stock库存统计表
        return storageOutMapper.insertStorageOut(map);//插入t_storage_out出库表
    }

    /**
     * 新增出库
     *
     * @param storageOut 出库
     * @return 结果
     */
    @Override
    public int insertStorageOut(StorageOut storageOut) {
        storageOut.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        storageOut.setCreateTime(new Date());
        storageOut.setUpdateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        storageOut.setUpdateTime(new Date());
        storageInMapper.updateInStatusByOut(storageOut);//更新入库表状态
        //storageInMapper.updateProductStock(map);//TODO 更新t_product_stock库存统计表

        return storageOutMapper.insertStorageOutV2(storageOut);//插入t_storage_out出库表
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
        storageService.addCodeFlow(AccConstants.STORAGE_TYPE_OUT, Long.valueOf(map.get("id").toString()), map.get("code").toString());//插入物流码
        storageOutMapper.updateOutStatusByCode(map);//更新出库数量
        //查询出库单需要的相关信息
        StorageOut storageOut=storageOutMapper.selectStorageOutById(Long.valueOf(map.get("id").toString()));
        //判断是否调拨,执行更新调拨单
        String extraNo=storageOut.getExtraNo();
        if(extraNo.substring(0,2).equals("DB")){
            StorageTransfer storageTransfer=storageTransferService.selectStorageTransferByNo(extraNo);
            storageTransfer.setStatus(StorageTransfer.STATUS_DEALING);
            storageTransfer.setBatchNo(storageOut.getBatchNo());
            storageTransfer.setActTransferNum(storageOut.getActOutNum());
            storageTransferService.updateStorageTransfer(storageTransfer);
        }
        //更新t_product_stock库存统计表
        productStockService.insertProductStock(storageOut.getFromStorageId(),storageOut.getProductId(),AccConstants.STORAGE_TYPE_OUT,storageOut.getId(),storageOut.getActOutNum().intValue());

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
        return storageOutMapper.insertStorageOutV2(storageOut);
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
