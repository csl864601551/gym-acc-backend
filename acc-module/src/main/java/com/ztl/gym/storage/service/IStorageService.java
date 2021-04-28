package com.ztl.gym.storage.service;

import java.util.List;

import com.ztl.gym.storage.domain.Storage;
import com.ztl.gym.storage.domain.vo.StorageVo;

/**
 * 仓库Service接口
 *
 * @author zhucl
 * @date 2021-04-13
 */
public interface IStorageService {
    /**
     * 查询仓库
     *
     * @param id 仓库ID
     * @return 仓库
     */
    public Storage selectStorageById(Long id);

    /**
     * 查询仓库列表
     *
     * @param storage 仓库
     * @return 仓库集合
     */
    public List<Storage> selectStorageList(Storage storage);

    /**
     * 新增仓库
     *
     * @param storage 仓库
     * @return 结果
     */
    public int insertStorage(Storage storage);

    /**
     * 修改仓库
     *
     * @param storage 仓库
     * @return 结果
     */
    public int updateStorage(Storage storage);

    /**
     * 批量删除仓库
     *
     * @param ids 需要删除的仓库ID
     * @return 结果
     */
    public int deleteStorageByIds(Long[] ids);

    /**
     * 删除仓库信息
     *
     * @param id 仓库ID
     * @return 结果
     */
    public int deleteStorageById(Long id);

    /**
     * 根据用户查询仓库
     *
     * @param storage
     * @return
     */
    List<Storage> selectStorageByUser(Storage storage);

    /**
     * 查询码流转信息 【包含码属性、产品、批次、流转信息】
     *
     * @param codeVal
     * @return
     */
    StorageVo selectLastStorageByCode(String codeVal);

    /**
     * 新增码流转明细-套标 【1.新增码流转明细 2.修改码属性codeAttr中最新流转信息】
     *
     * @return
     * @remark 【注意】该接口指针对套标，如果传值单码含有箱码，会自动关联整箱，单个退货不能调用此接口
     */
    int addCodeFlow(int storageType, long storageRecordId, String code);

}
