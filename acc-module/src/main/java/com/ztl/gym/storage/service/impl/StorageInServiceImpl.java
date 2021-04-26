package com.ztl.gym.storage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.storage.domain.vo.StorageVo;
import com.ztl.gym.storage.service.IStorageService;
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
public class StorageInServiceImpl implements IStorageInService
{
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
    /**
     * 查询入库
     *
     * @param id 入库ID
     * @return 入库
     */
    @Override
    public StorageIn selectStorageInById(Long id)
    {
        return storageInMapper.selectStorageInById(id);
    }

    /**
     * 查询入库列表
     *
     * @param storageIn 入库
     * @return 入库
     */
    @Override
    public List<StorageIn> selectStorageInList(StorageIn storageIn)
    {
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
    public int insertStorageIn(Map<String, Object> map)
    {
        if(map.get("tenantId").toString().equals("")||map.get("tenantId")==null||map.get("tenantId").toString().equals("0")){
            map.put("tenantId",commonService.getTenantId());
        }
        map.put("createTime",DateUtils.getNowDate());
        map.put("inTime",DateUtils.getNowDate());
        map.put("createUser",SecurityUtils.getLoginUser().getUser().getUserId());
        int result=storageInMapper.insertStorageIn(map);//新增t_storage_in入库表
        Long id=Long.valueOf(map.get("id").toString());
        //storageService.addCodeFlow(AccConstants.STORAGE_TYPE_IN, id ,map.get("code").toString());//转移到PDA执行
        //storageInMapper.updateProductStock(map);//TODO 更新t_product_stock库存统计表
        return result;
    }

    /**
     * 修改入库
     *
     * @param storageIn 入库
     * @return 结果
     */
    @Override
    public int updateStorageIn(StorageIn storageIn)
    {
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
    public int deleteStorageInByIds(Long[] ids)
    {
        return storageInMapper.deleteStorageInByIds(ids);
    }

    /**
     * 删除入库信息
     *
     * @param id 入库ID
     * @return 结果
     */
    @Override
    public int deleteStorageInById(Long id)
    {
        return storageInMapper.deleteStorageInById(id);
    }

    /**
     * 获取相关码产品信息
     * @param code
     * @return
     */
    @Override
    public Map<String, Object> getCodeInfo(String code) {
        Map<String, Object> map=new HashMap<>();
        map=storageInMapper.getCodeInfo(code);//获取码产品信息
        List<Map<String, Object>> listMap=storageInMapper.getCodeDetail(code);//获取码产品明细
        map.put("listMap",listMap);
        return map;
    }

    /**
     * 企业确认入库
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @DataSource(DataSourceType.SHARDING)
    public int updateInStatusByCode(Map<String, Object> map) {
        map.put("status",StorageIn.STATUS_NORMAL);
        map.put("updateTime",DateUtils.getNowDate());
        map.put("updateUser",SecurityUtils.getLoginUser().getUser().getUserId());
        storageService.addCodeFlow(AccConstants.STORAGE_TYPE_IN, Long.valueOf(map.get("id").toString()) ,map.get("code").toString());//转移到PDA执行
        return storageInMapper.updateInStatusByCode(map);
    }

    /**
     * 经销商确认入库
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    @DataSource(DataSourceType.SHARDING)
    public int updateTenantIn(Map<String, Object> map) {
        map.put("status",StorageIn.STATUS_NORMAL);
        map.put("updateTime",DateUtils.getNowDate());
        map.put("updateUser",SecurityUtils.getLoginUser().getUser().getUserId());
        long storageRecordId=storageInMapper.selectOutIdByExtraNo(map.get("extraNo").toString());
        List<String> codes=codeService.selectCodeByStorage(Long.valueOf(map.get("companyId").toString()) ,AccConstants.STORAGE_TYPE_OUT,storageRecordId);
        storageService.addCodeFlow(AccConstants.STORAGE_TYPE_IN, Long.valueOf(map.get("id").toString()) ,codes.get(0));//转移到PDA执行
        return storageInMapper.updateTenantIn(map);
    }

    @Override
    @DataSource(DataSourceType.SHARDING)
    public List<Map<String,Object>> getCodeDetailById(Long companyId,Integer id) {
        List<Map<String,Object>> list=storageInMapper.getCodeDetailById(companyId,id);
        return list;
    }
}
