package com.ztl.gym.storage.domain;

/**
 * 对接ERP明细对象 t_erp_detail
 *
 * @author ruoyi
 * @date 2022-06-11
 */
public class ErpDetail
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** erp主键 */
    private Long erpId;

    /** 规格型号 */
    private String productNo;

    /** 产品ID */
    private Long productId;

    /** 物料名称 */
    private String productName;

    /** 物料编码 */
    private String barCode;

    /** 出库数量 */
    private Long outNum;

    /** 实际出库数量 */
    private Long actNum;

    /** 仓库名称 */
    private String storageName;

    /** 状态（0，未完成，1已完成） */
    private Long status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setErpId(Long erpId)
    {
        this.erpId = erpId;
    }

    public Long getErpId()
    {
        return erpId;
    }
    public void setProductNo(String productNo)
    {
        this.productNo = productNo;
    }

    public String getProductNo()
    {
        return productNo;
    }
    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getProductId()
    {
        return productId;
    }
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductName()
    {
        return productName;
    }
    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
    }

    public String getBarCode()
    {
        return barCode;
    }
    public void setOutNum(Long outNum)
    {
        this.outNum = outNum;
    }

    public Long getOutNum()
    {
        return outNum;
    }
    public void setActNum(Long actNum)
    {
        this.actNum = actNum;
    }

    public Long getActNum()
    {
        return actNum;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }
}
