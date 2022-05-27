package com.ztl.gym.storage.mapper;

import java.util.List;
import java.util.Map;

import com.ztl.gym.storage.domain.InCodeFlow;
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.domain.StorageOut;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 入库Mapper接口
 *
 * @author ruoyi
 * @date 2021-04-09
 */
@Repository
public interface StorageInMapper
{
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
    public int insertStorageInV2(StorageIn storageIn);

    /**
     * 修改入库
     *
     * @param storageIn 入库
     * @return 结果
     */
    public int updateStorageIn(StorageIn storageIn);

    /**
     * 删除入库
     *
     * @param id 入库ID
     * @return 结果
     */
    public int deleteStorageInById(Long id);

    /**
     * 批量删除入库
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStorageInByIds(Long[] ids);

    void insertPcodeFlow(Map<String, Object> storageIn);

    void insertCodeFlow(Map<String, Object> storageIn);

    void updateProductStock(Map<String, Object> storageIn);

    StorageIn selectStorageCodeById(Long id);

    Map<String, Object> getCodeInfo(String code);

    List<Map<String, Object>> getCodeDetail(String code);

    int updateInStatusByCode(Map<String, Object> map);

    int updateTenantIn(Map<String, Object> map);

    long selectOutIdByExtraNo(String extraNo);

    long selectInIdByExtraNo(String extraNo);

    void updateInStatusByOut(StorageOut storageOut);

    List<Map<String, Object>> getCodeDetailById(@Param("companyId")Long companyId, @Param("id")Integer id);

    StorageIn selectStorageInByExtraNo(String extraNo);

    void updateInStatusById(Long map);

    Long selectInIdByCode(Map<String, Object> map);

    void deleteInCodeFlowByInId(Map<String, Object> map);

    /**
     * 批量新增入库数据
     * @param listStorageIn
     * @return
     */
    int insertStorageInAll(@Param("listStorageIn") List<StorageIn> listStorageIn);

    /**
     * 批量新增流转数据
     * @param listFlowVo
     * @param companyId
     * @return
     */
    int insertInCodeFlowAll(@Param("listFlowVo") List<InCodeFlow> listFlowVo, @Param("companyId") Long companyId);

    int updateProductIdByIds(Map<String, Object> params);
}
