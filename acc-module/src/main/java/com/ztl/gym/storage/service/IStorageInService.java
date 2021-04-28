package com.ztl.gym.storage.service;

import java.util.List;
import java.util.Map;

import com.ztl.gym.storage.domain.StorageBack;
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.domain.vo.StorageVo;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 入库Service接口
 *
 * @author ruoyi
 * @date 2021-04-09
 */
public interface IStorageInService {
    /**
     * 查询入库
     *
     * @param id 入库ID
     * @return 入库
     */
    public StorageIn selectStorageInById(Long id);

    /**
     * 查询入库列表
     *
     * @param storageIn 入库
     * @return 入库集合
     */
    public List<StorageIn> selectStorageInList(StorageIn storageIn);

    /**
     * 新增入库
     *
     * @param storageIn 入库
     * @return 结果
     */
    public int insertStorageIn(Map<String, Object> storageIn);

    /**
     * 修改入库
     *
     * @param storageIn 入库
     * @return 结果
     */
    public int updateStorageIn(StorageIn storageIn);

    /**
     * 批量删除入库
     *
     * @param ids 需要删除的入库ID
     * @return 结果
     */
    public int deleteStorageInByIds(Long[] ids);

    /**
     * 删除入库信息
     *
     * @param id 入库ID
     * @return 结果
     */
    public int deleteStorageInById(Long id);

    /**
     * 获取相关码产品信息
     *
     * @param id
     * @return
     */
    Map<String, Object> getCodeInfo(String id);

    /**
     * 企业确认入库
     *
     * @param map
     * @return
     */
    int updateInStatusByCode(Map<String, Object> map);

    /**
     * PC经销商确认入库
     *
     * @param map
     * @return 确认收货
     * 与上面的区别是否存在码信息
     */
    int updateTenantIn(Map<String, Object> map);

    /**
     * 查询入库单中的码明细
     *
     * @param companyId
     * @param id
     * @return
     */
    List<Map<String, Object>> getCodeDetailById(Long companyId, Integer id);

    /**
     * 根据相关单号查询入库单
     *
     * @param extraNo
     * @return
     */
    StorageIn selectStorageInByExtraNo(String extraNo);

    /**
     * 退货入库
     *
     * @param storageBack
     * @return
     */
    int insertStorageInForBack(StorageBack storageBack);
}
