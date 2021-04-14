package com.ztl.gym.product.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ztl.gym.product.mapper.AttrMapper;
import com.ztl.gym.product.domain.Attr;
import com.ztl.gym.product.service.IAttrService;

/**
 * 规格属性Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-13
 */
@Service
public class AttrServiceImpl implements IAttrService
{
    @Autowired
    private AttrMapper attrMapper;

    /**
     * 查询规格属性
     *
     * @param id 规格属性ID
     * @return 规格属性
     */
    @Override
    public Attr selectAttrById(Long id)
    {
        return attrMapper.selectAttrById(id);
    }

    /**
     * 查询规格属性列表
     *
     * @param attr 规格属性
     * @return 规格属性
     */
    @Override
    public List<Attr> selectAttrList(Attr attr)
    {
        return attrMapper.selectAttrList(attr);
    }

    /**
     * 新增规格属性
     *
     * @param attr 规格属性
     * @return 结果
     */
    @Override
    public int insertAttr(Attr attr)
    {
        attr.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        attr.setCreateTime(DateUtils.getNowDate());
        attr.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return attrMapper.insertAttr(attr);
    }

    /**
     * 修改规格属性
     *
     * @param attr 规格属性
     * @return 结果
     */
    @Override
    public int updateAttr(Attr attr)
    {
        attr.setUpdateTime(DateUtils.getNowDate());
        attr.setUpdateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return attrMapper.updateAttr(attr);
    }

    /**
     * 批量删除规格属性
     *
     * @param ids 需要删除的规格属性ID
     * @return 结果
     */
    @Override
    public int deleteAttrByIds(Long[] ids)
    {
        return attrMapper.deleteAttrByIds(ids);
    }

    /**
     * 删除规格属性信息
     *
     * @param id 规格属性ID
     * @return 结果
     */
    @Override
    public int deleteAttrById(Long id)
    {
        return attrMapper.deleteAttrById(id);
    }

    @Override
    public List<Map<String, Object>> getAttrValuesById(Long id) {
        String attrValue=attrMapper.selectAttrById(id).getAttrValue();
        List<Map<String, Object>> list=new ArrayList<>();
        String[] sourceArray = attrValue.split("\n");
        Map<String,Object> temp=new HashMap<>();
        for (int i = 0; i < sourceArray.length; i++) {
            temp=new HashMap<>();
            temp.put("value",sourceArray[i]);
            temp.put("label",sourceArray[i]);
            list.add(temp);
        }
        return list;
    }
}
