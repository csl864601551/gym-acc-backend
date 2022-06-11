package com.ztl.gym.storage.mapper;

import java.util.List;
import com.ztl.gym.storage.domain.Erp;

/**
 * 对接ERP主Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-11
 */
public interface ErpMapper
{
    /**
     * 查询对接ERP主
     *
     * @param id 对接ERP主ID
     * @return 对接ERP主
     */
    public Erp selectErpById(Long id);

    /**
     * 查询对接ERP主列表
     *
     * @param erp 对接ERP主
     * @return 对接ERP主集合
     */
    public List<Erp> selectErpList(Erp erp);

    /**
     * 新增对接ERP主
     *
     * @param erp 对接ERP主
     * @return 结果
     */
    public int insertErp(Erp erp);

    /**
     * 修改对接ERP主
     *
     * @param erp 对接ERP主
     * @return 结果
     */
    public int updateErp(Erp erp);

    /**
     * 删除对接ERP主
     *
     * @param id 对接ERP主ID
     * @return 结果
     */
    public int deleteErpById(Long id);

    /**
     * 批量删除对接ERP主
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteErpByIds(Long[] ids);
}
