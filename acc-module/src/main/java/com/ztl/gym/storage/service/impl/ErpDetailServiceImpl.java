package com.ztl.gym.storage.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.ErpDetailMapper;
import com.ztl.gym.storage.domain.ErpDetail;
import com.ztl.gym.storage.service.IErpDetailService;

/**
 * 对接ERP明细Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-11
 */
@Service
public class ErpDetailServiceImpl implements IErpDetailService
{
    @Autowired
    private ErpDetailMapper erpDetailMapper;

    /**
     * 查询对接ERP明细
     *
     * @param id 对接ERP明细ID
     * @return 对接ERP明细
     */
    @Override
    public ErpDetail selectErpDetailById(Long id)
    {
        return erpDetailMapper.selectErpDetailById(id);
    }

    /**
     * 查询对接ERP明细列表
     *
     * @param erpDetail 对接ERP明细
     * @return 对接ERP明细
     */
    @Override
    public List<ErpDetail> selectErpDetailList(ErpDetail erpDetail)
    {
        return erpDetailMapper.selectErpDetailList(erpDetail);
    }

    /**
     * 新增对接ERP明细
     *
     * @param erpDetail 对接ERP明细
     * @return 结果
     */
    @Override
    public int insertErpDetail(ErpDetail erpDetail)
    {
        return erpDetailMapper.insertErpDetail(erpDetail);
    }

    /**
     * 修改对接ERP明细
     *
     * @param erpDetail 对接ERP明细
     * @return 结果
     */
    @Override
    public int updateErpDetail(ErpDetail erpDetail)
    {
        return erpDetailMapper.updateErpDetail(erpDetail);
    }

    /**
     * 批量删除对接ERP明细
     *
     * @param ids 需要删除的对接ERP明细ID
     * @return 结果
     */
    @Override
    public int deleteErpDetailByIds(Long[] ids)
    {
        return erpDetailMapper.deleteErpDetailByIds(ids);
    }

    /**
     * 删除对接ERP明细信息
     *
     * @param id 对接ERP明细ID
     * @return 结果
     */
    @Override
    public int deleteErpDetailById(Long id)
    {
        return erpDetailMapper.deleteErpDetailById(id);
    }
}
