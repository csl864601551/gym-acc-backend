package com.ztl.gym.storage.service.impl;

import java.util.List;
import java.util.Queue;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.exception.BaseException;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.StorageMapper;
import com.ztl.gym.storage.domain.Storage;
import com.ztl.gym.storage.service.IStorageService;

/**
 * 仓库Service业务层处理
 *
 * @author zhucl
 * @date 2021-04-13
 */
@Service
public class StorageServiceImpl implements IStorageService
{
    @Autowired
    private StorageMapper storageMapper;

    /**
     * 查询仓库
     *
     * @param id 仓库ID
     * @return 仓库
     */
    @Override
    public Storage selectStorageById(Long id)
    {
        return storageMapper.selectStorageById(id);
    }

    /**
     * 查询仓库列表
     *
     * @param storage 仓库
     * @return 仓库
     */
    @Override
    public List<Storage> selectStorageList(Storage storage)
    {
        LoginUser user = SecurityUtils.getLoginUser();
        SysDept dept = user.getUser().getDept();
        Long deptId = dept.getDeptId();
        if (deptId != 100L ) {
            String ancestors = dept.getAncestors();
            int count = (ancestors.length()-ancestors.replace(",","").length())/",".length();
            if (count == 1) {
                storage.setCompanyId(deptId);
            } else {
                storage.setTenantId(deptId);
            }
        }
        storage.setStatus(AccConstants.STORAGE_DELETE_NO);
        return storageMapper.selectStorageList(storage);
    }

    /**
     * 查询仓库列表
     *
     * @param storage 仓库
     * @return 仓库
     */
    public Integer countStorage(Storage storage)
    {
        return storageMapper.countStorage(storage);
    }

    /**
     * 新增仓库
     *
     * @param storage 仓库
     * @return 结果
     */
    @Override
    public int insertStorage(Storage storage)
    {
        Storage queryStorage = new Storage();
        queryStorage.setStorageNo(storage.getStorageNo());
        queryStorage.setStatus(AccConstants.STORAGE_DELETE_NO);
        Integer storageCount = this.countStorage(queryStorage);
        if (storageCount > 0) {
            throw new BaseException("仓库编号已重复，请重新命名");
        }

        storage.setCreateTime(DateUtils.getNowDate());
        Long companyId = SecurityUtils.getLoginUserTopCompanyId();
        storage.setCompanyId(companyId);
        Long tenantId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!tenantId.equals(companyId)) {
            storage.setLevel(AccConstants.STORAGE_LEVEL_TENANT);
            storage.setTenantId(tenantId);
        } else {
            storage.setLevel(AccConstants.STORAGE_LEVEL_COMPANY);
        }
        storage.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return storageMapper.insertStorage(storage);
    }

    /**
     * 修改仓库
     *
     * @param storage 仓库
     * @return 结果
     */
    @Override
    public int updateStorage(Storage storage)
    {
        Storage queryStorage = new Storage();
        queryStorage.setId(storage.getId());
        queryStorage.setStorageNo(storage.getStorageNo());
        queryStorage.setStatus(AccConstants.STORAGE_DELETE_NO);
        Integer storageCount = this.countStorage(queryStorage);
        if (storageCount > 0) {
            throw new BaseException("仓库编号已重复，请重新命名");
        }

        storage.setUpdateTime(DateUtils.getNowDate());
        storage.setUpdateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return storageMapper.updateStorage(storage);
    }

    /**
     * 批量删除仓库
     *
     * @param ids 需要删除的仓库ID
     * @return 结果
     */
    @Override
    public int deleteStorageByIds(Long[] ids)
    {
        return storageMapper.deleteStorageByIds(ids);
    }

    /**
     * 删除仓库信息
     *
     * @param id 仓库ID
     * @return 结果
     */
    @Override
    public int deleteStorageById(Long id)
    {
        return storageMapper.deleteStorageById(id);
    }
}
