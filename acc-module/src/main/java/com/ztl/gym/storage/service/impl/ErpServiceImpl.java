package com.ztl.gym.storage.service.impl;

import java.util.List;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.storage.mapper.ErpMapper;
import com.ztl.gym.storage.domain.Erp;
import com.ztl.gym.storage.service.IErpService;

/**
 * 对接ERP主Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-11
 */
@Service
public class ErpServiceImpl implements IErpService
{
    @Autowired
    private ErpMapper erpMapper;

    /**
     * 查询对接ERP主
     *
     * @param id 对接ERP主ID
     * @return 对接ERP主
     */
    @Override
    public Erp selectErpById(Long id)
    {
        return erpMapper.selectErpById(id);
    }

    /**
     * 查询对接ERP主列表
     *
     * @param erp 对接ERP主
     * @return 对接ERP主
     */
    @Override
    public List<Erp> selectErpList(Erp erp)
    {
        return erpMapper.selectErpList(erp);
    }

    /**
     * 新增对接ERP主
     *
     * @param erp 对接ERP主
     * @return 结果
     */
    @Override
    public int insertErp(Erp erp)
    {
        erp.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        erp.setCreateTime(DateUtils.getNowDate());
        return erpMapper.insertErp(erp);
    }

    /**
     * 修改对接ERP主
     *
     * @param erp 对接ERP主
     * @return 结果
     */
    @Override
    public int updateErp(Erp erp)
    {
        return erpMapper.updateErp(erp);
    }

    /**
     * 批量删除对接ERP主
     *
     * @param ids 需要删除的对接ERP主ID
     * @return 结果
     */
    @Override
    public int deleteErpByIds(Long[] ids)
    {
        return erpMapper.deleteErpByIds(ids);
    }

    /**
     * 删除对接ERP主信息
     *
     * @param id 对接ERP主ID
     * @return 结果
     */
    @Override
    public int deleteErpById(Long id)
    {
        return erpMapper.deleteErpById(id);
    }
}
