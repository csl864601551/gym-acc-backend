package com.ztl.gym.storage.service;

import java.util.List;
import com.ztl.gym.storage.domain.Storage;

/**
 * 仓库Service接口
 * 
 * @author zhucl
 * @date 2021-04-13
 */
public interface IStorageService 
{
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
}
