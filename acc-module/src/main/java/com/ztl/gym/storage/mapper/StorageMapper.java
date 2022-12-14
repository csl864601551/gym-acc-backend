package com.ztl.gym.storage.mapper;

import java.util.List;
import com.ztl.gym.storage.domain.Storage;
import org.springframework.stereotype.Repository;

/**
 * 仓库Mapper接口
 *
 * @author zhucl
 * @date 2021-04-13
 */
@Repository
public interface StorageMapper
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
     * 删除仓库
     *
     * @param id 仓库ID
     * @return 结果
     */
    public int deleteStorageById(Long id);

    /**
     * 批量删除仓库
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStorageByIds(Long[] ids);

    /**
     * 统计仓库数
     *
     * @param storage 仓库
     * @return 仓库
     */
    public Integer countStorage(Storage storage);

    /**
     * 根据用户查询仓库
     *
     * @param storage
     * @return
     */
    List<Storage> selectStorageByUser(Storage storage);
}
