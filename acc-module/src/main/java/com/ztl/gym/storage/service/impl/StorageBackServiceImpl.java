package com.ztl.gym.storage.service.impl;

import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.storage.domain.StorageBack;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.mapper.StorageBackMapper;
import com.ztl.gym.storage.service.IStorageBackService;
import com.ztl.gym.storage.service.IStorageInService;
import com.ztl.gym.storage.service.IStorageOutService;
import com.ztl.gym.storage.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 退货Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-19
 */
@Service
public class StorageBackServiceImpl implements IStorageBackService {
    @Autowired
    private StorageBackMapper storageBackMapper;
    @Autowired
    private IStorageService storageService;
    @Autowired
    private IStorageInService storageInService;
    @Autowired
    private IStorageOutService storageOutService;
    @Autowired
    private ICodeService codeService;
    @Autowired
    private CommonService commonService;

    /**
     * 查询退货
     *
     * @param id 退货ID
     * @return 退货
     */
    @Override
    public StorageBack selectStorageBackById(Long id) {
        return storageBackMapper.selectStorageBackById(id);
    }

    /**
     * 查询退货列表
     *
     * @param storageBack 退货
     * @return 退货
     */
    @Override
    public List<StorageBack> selectStorageBackList(StorageBack storageBack) {
        storageBack.setTenantId(SecurityUtils.getLoginUserCompany().getDeptId());
        return storageBackMapper.selectStorageBackList(storageBack);
    }

    /**
     * 新增退货入库
     *
     * @param storageBack 退货
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DataSource(DataSourceType.SHARDING)
    public int insertStorageBack(StorageBack storageBack) {
        storageBack.setCreateTime(DateUtils.getNowDate());
        //先新增退货单
        storageBack.setRemark("退货入库自动创建该退货单");
        storageBack.setStatus(StorageBack.STATUS_NORMAL);
        storageBack.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        int res = storageBackMapper.insertStorageBack(storageBack);
        //退货、新增码明细 FIXME 单码退货时，码流转明细会把整套给加进去
        storageService.addCodeFlow(AccConstants.STORAGE_TYPE_BACK, storageBack.getId(), storageBack.getCodeStr());

        if (res > 0) {
            //添加退货入库单
            long inId = storageInService.insertStorageInForBack(storageBack);
            //退货入库 新增码明细
            storageService.addCodeFlow(AccConstants.STORAGE_TYPE_IN, inId, storageBack.getCodeStr());
        }
        return res;
    }

    /**
     * 修改退货
     *
     * @param storageBack 退货
     * @return 结果
     */
    @Override
    public int updateStorageBack(StorageBack storageBack) {
        storageBack.setUpdateTime(DateUtils.getNowDate());
        return storageBackMapper.updateStorageBack(storageBack);
    }

    /**
     * 批量删除退货
     *
     * @param ids 需要删除的退货ID
     * @return 结果
     */
    @Override
    public int deleteStorageBackByIds(Long[] ids) {
        return storageBackMapper.deleteStorageBackByIds(ids);
    }

    /**
     * 删除退货信息
     *
     * @param id 退货ID
     * @return 结果
     */
    @Override
    public int deleteStorageBackById(Long id) {
        return storageBackMapper.deleteStorageBackById(id);
    }
}
