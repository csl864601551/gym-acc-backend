package com.ztl.gym.code.domain.vo;

import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.domain.ProductBatch;

import java.util.List;

/**
 * 赋值vo
 */
public class FuzhiVo {
    private long recordId;
    private long productId;
    private long batchId;
    private String remark;
    private List<Product> products;
    private List<ProductBatch> productBatchs;

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ProductBatch> getProductBatchs() {
        return productBatchs;
    }

    public void setProductBatchs(List<ProductBatch> productBatchs) {
        this.productBatchs = productBatchs;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
