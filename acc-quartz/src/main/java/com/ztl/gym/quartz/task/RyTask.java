package com.ztl.gym.quartz.task;

import cn.hutool.core.date.DateUtil;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.domain.ProductBatch;
import com.ztl.gym.product.service.IProductBatchService;
import com.ztl.gym.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask {

    @Autowired
    private IProductService tProductService;

    @Autowired
    private IProductBatchService productBatchService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }


    /**
     * 定时任务 新增产品批次
     */
    public void addProductBatch() {
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            String datetime = df.format(calendar.getTime());
            long createUser = 3;
            // 查询商品表中所有的企业信息
            List<Long> companyList = tProductService.selectCompanyByTProduct();
            if (companyList.size() > 0) {
                for (int i = 0; i < companyList.size(); i++) {
                    Product product = new Product();
                    Long company_id = companyList.get(i);
                    product.setCompanyId(company_id);
                    //根据企业查询改企业的产品
                    List<Product> Productlist = tProductService.selectAllProductList(product);
                    if (Productlist.size() > 0) {
                        for (int h = 0; h < Productlist.size(); h++) {
                            Product productinfo = Productlist.get(h);
                            ProductBatch productBatch = new ProductBatch();
                            productBatch.setCompanyId(company_id);
                            productBatch.setProductId(productinfo.getId());
                            //计算编号
                            int no = h+1;
                            if (no > 9) {
                                productBatch.setBatchNo(datetime + "0" + String.valueOf(no));
                            } else if (no > 99) {
                                productBatch.setBatchNo(datetime + String.valueOf(no));
                            } else {
                                productBatch.setBatchNo(datetime + "00" + String.valueOf(no));
                            }
                            //根据企业查询改企业的产品
                            List<ProductBatch> productBatchList = productBatchService.selectProductBatchList(productBatch);
                            if (productBatchList.size() > 0) {
                                no = h+1 + Productlist.size();
                                if (no > 9) {
                                    productBatch.setBatchNo(datetime + "0" + String.valueOf(no));
                                } else if (no > 99) {
                                    productBatch.setBatchNo(datetime + String.valueOf(no));
                                } else {
                                    productBatch.setBatchNo(datetime + "00" + String.valueOf(no));
                                }
                            }
                            productBatch.setStatus(0L);
                            productBatch.setBatchTitle(productinfo.getProductName());
                            productBatch.setBatchDate(DateUtil.date());
                            productBatch.setCreateUser(createUser);
                            productBatch.setCreateTime(DateUtil.date());
                            productBatchService.insertProductBatchOne(productBatch);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("新增定时批次信息失败！！");
        }

    }

}
