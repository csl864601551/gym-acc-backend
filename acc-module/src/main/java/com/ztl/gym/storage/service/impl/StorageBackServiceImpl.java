package com.ztl.gym.storage.service.impl;

import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.storage.domain.StorageBack;
import com.ztl.gym.storage.mapper.StorageBackMapper;
import com.ztl.gym.storage.service.IStorageBackService;
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
    private ICodeService codeService;

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
    public int insertStorageBack(StorageBack storageBack) {
        storageBack.setCreateTime(DateUtils.getNowDate());
        int res = storageBackMapper.insertStorageBack(storageBack);
        if (res > 0) {
            List<String> codes = codeService.selectCodeByStorage(storageBack.getCompanyId(), AccConstants.STORAGE_TYPE_BACK, storageBack.getId());
            storageService.addCodeFlow(AccConstants.STORAGE_TYPE_BACK, storageBack.getId(), codes.get(0));
        }
        return storageBackMapper.insertStorageBack(storageBack);
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
