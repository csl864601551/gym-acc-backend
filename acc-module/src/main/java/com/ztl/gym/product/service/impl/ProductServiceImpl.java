package com.ztl.gym.product.service.impl;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.product.domain.Attr;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.mapper.AttrMapper;
import com.ztl.gym.product.mapper.ProductMapper;
import com.ztl.gym.product.service.IAttrService;
import com.ztl.gym.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-10
 */
@Service
public class ProductServiceImpl implements IProductService
{
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private AttrMapper attrMapper;
    @Autowired
    private IAttrService attrService;


    /**
     * 查询产品
     *
     * @param id 产品ID
     * @return 产品
     */
    @Override
    public Product selectTProductById(Long id)
    {
        Product product=productMapper.selectTProductById(id);
        List<Map<String,Object>> list=productMapper.getAttributeList(id);
        for (int i = 0; i < list.size(); i++) {
            Attr attr=attrService.selectAttrByName(list.get(i).get("attrNameCn").toString());
            List<Map<String, Object>> listValues=new ArrayList<>();
            if(attr!=null){
                String attrValue=attr.getAttrValue();
                if(attrValue!=null){
                    String[] sourceArray = attrValue.split("\n");
                    Map<String,Object> temp=new HashMap<>();
                    for (int j = 0; j < sourceArray.length; j++) {
                        temp=new HashMap<>();
                        temp.put("value",sourceArray[j]);
                        temp.put("label",sourceArray[j]);
                        listValues.add(temp);
                    }
                }
            }
            list.get(i).put("arrList",listValues);
        }
        product.setAttributeList(list);
        return product;
    }

    /**
     * 查询产品列表
     *
     * @param product 产品
     * @return 产品
     */
    @Override
    public List<Product> selectTProductList(Product product)
    {
        Long company_id=SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            product.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        return productMapper.selectTProductList(product);
    }

    /**
     * 查询产品列表
     *
     * @param product 产品
     * @return 产品
     */
    @Override
    public List<Product> selectTProductList1(Product product)
    {
        Long company_id=SecurityUtils.getLoginUserCompany().getDeptId();
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            product.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        return productMapper.selectTProductList1(product);
    }

    /**
     * 新增产品
     *
     * @param product 产品
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int insertTProduct(Product product)
    {
        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        Long company_temp=null;//公司ID
        Long createUser=SecurityUtils.getLoginUser().getUser().getUserId();//用户ID
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            company_temp=SecurityUtils.getLoginUserTopCompanyId();
            product.setCompanyId(company_temp);
        }
        product.setCreateUser(createUser);
        product.setCreateTime(DateUtils.getNowDate());
        int result=productMapper.insertTProduct(product);//插入product主表
        Long id=product.getId();
        List<Map<String,Object>> list=product.getAttributeList();
        Map<String,Object> map=new HashMap<>();
        for(int i=0;i<list.size();i++){
            try {
                map=list.get(i);
                map.put("productId",id);
                map.put("companyId",company_temp);
                map.put("createUser",createUser);
                map.put("createTime",DateUtils.getNowDate());
                if(map.get("sort")==null||map.get("sort")==""){
                    map.put("sort",1);
                }
                try{
                    Long attr_id= Long.parseLong(map.get("attrNameCn").toString());
                    map.put("attrNameCn",attrMapper.selectAttrById(attr_id).getAttrNameCn());
                }catch (Exception e){}
                productMapper.insertProductAttr(map);
            }catch (Exception e){
                throw new CustomException("产品属性异常，请检查填写格式！", HttpStatus.ERROR);
            }
        }
        return result;
    }

    /**
     * 修改产品
     *
     * @param product 产品selectTProductList
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int updateTProduct(Product product)
    {
        Long id=product.getId();
        product.setUpdateTime(DateUtils.getNowDate());

        Long company_id= SecurityUtils.getLoginUserCompany().getDeptId();
        Long company_temp=null;//公司ID
        Long createUser=SecurityUtils.getLoginUser().getUser().getUserId();//用户ID
        if(!company_id.equals(AccConstants.ADMIN_DEPT_ID)){
            company_temp=SecurityUtils.getLoginUserTopCompanyId();
            product.setCompanyId(company_temp);
        }
        product.setCreateUser(createUser);
        product.setCreateTime(DateUtils.getNowDate());

        int result=productMapper.updateTProduct(product);//更新product

        List<Map<String,Object>> list=product.getAttributeList();
        if(list!=null){
            if(list.size()>0){
                productMapper.deleteProductAttrById(id);//删除product_attr
                Map<String,Object> map=new HashMap<>();
                for(int i=0;i<list.size();i++){
                    map=list.get(i);
                    map.put("productId",id);
                    map.put("companyId",company_temp);
                    map.put("createUser",createUser);
                    map.put("createTime",DateUtils.getNowDate());
                    if(map.get("sort")==null||map.get("sort")==""){
                        map.put("sort",1);
                    }
                    try {
                        Long attr_id= Long.parseLong(map.get("attrNameCn").toString());
                        map.put("attrNameCn",attrMapper.selectAttrById(attr_id).getAttrNameCn());
                    }catch (Exception e){

                    }

                    productMapper.insertProductAttr(map);//插入product_attr
                }
            }
        }



        return result;
    }

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的产品ID
     * @return 结果
     */
    @Override
    public int deleteTProductByIds(Long[] ids)
    {
        return productMapper.deleteTProductByIds(ids);
    }
    @Override
    public int deleteTProductTrueByIds(Long[] ids)
    {
        return productMapper.deleteTProductTrueByIds(ids);
    }

    /**
     * 删除产品信息
     *
     * @param id 产品ID
     * @return 结果
     */
    @Override
    public int deleteTProductById(Long id)
    {
        return productMapper.deleteTProductById(id);
    }

    /**
     * 根据信息 获取产品数量
     *
     * @param map 产品
     * @return 结果
     */
    @Override
    public int selectProductNum(Map<String, Object> map)
    {
        return productMapper.selectProductNum(map);
    }


    /**
     * 查询产品
     *
     * @param id 产品ID
     * @return 产品
     */
    @Override
    public Product selectTProductByIdOne(Long id)
    {
        Product product=productMapper.selectTProductById(id);
        return product;
    }
}
