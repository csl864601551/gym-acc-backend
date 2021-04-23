package com.ztl.gym.storage.service.impl;

import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.storage.domain.StorageBack;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.mapper.StorageBackMapper;
import com.ztl.gym.storage.service.IStorageBackService;
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
            //如果是企业绕过经销商直接退货入库，则自动给对应经销商出具一条出库单
            if (storageBack.getCompanyForceFlag() != null && storageBack.getCompanyForceFlag() == 1) {
                StorageOut storageOut = new StorageOut();
                storageOut.setCompanyId(storageBack.getCompanyId());
                storageOut.setTenantId(storageBack.getStorageFrom());
                String outNo = commonService.getStorageNo(AccConstants.STORAGE_TYPE_OUT);
                storageOut.setOutNo(outNo);
                storageBack.setExtraNo(outNo);

                storageOut.setProductId(storageBack.getProductId());
                storageOut.setBatchNo(storageBack.getBatchNo());
                storageOut.setOutNum(storageBack.getActBackNum());
                storageOut.setActOutNum(storageBack.getActBackNum());
                storageOut.setStorageFrom(storageBack.getStorageFrom());
                storageOut.setStorageTo(storageBack.getCompanyId());
                storageOut.setToStorageId(storageBack.getToStorageId());
                storageOut.setRemark("企业直接退货入库，强制出库");
                storageOutService.insertStorageOut(storageOut);
            }

            //退货入库 新增码明细
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
