package com.ztl.gym.web.controller.code;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.code.domain.vo.CodeRecordDetailVo;
import com.ztl.gym.code.domain.vo.FuzhiVo;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.domain.ProductBatch;
import com.ztl.gym.product.domain.ProductCategory;
import com.ztl.gym.product.service.IProductBatchService;
import com.ztl.gym.product.service.IProductCategoryService;
import com.ztl.gym.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/code/record")
public class CodeRecordController extends BaseController {
    @Autowired
    private ICodeRecordService codeRecordService;
    @Autowired
    private ICodeService codeService;
    @Autowired
    private ICodeAttrService codeAttrService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IProductBatchService productBatchService;
    @Autowired
    private IProductCategoryService productCategoryService;

    /**
     * 查询生码记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(CodeRecord codeRecord) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            codeRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        startPage();
        List<CodeRecord> list = codeRecordService.selectCodeRecordList(codeRecord);
        return getDataTable(list);
    }

    /**
     * 导出生码记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:record:export')")
    @Log(title = "生码记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CodeRecord codeRecord) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            codeRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        List<CodeRecord> list = codeRecordService.selectCodeRecordList(codeRecord);
        for (CodeRecord record : list) {
            String statusName = "";
            if (record.getStatus() == AccConstants.CODE_RECORD_STATUS_WAIT) {
                statusName = "创建中";
            } else if (record.getStatus() == AccConstants.CODE_RECORD_STATUS_FINISH) {
                statusName = "待赋值";
            } else if (record.getStatus() == AccConstants.CODE_RECORD_STATUS_EVA) {
                statusName = "已赋值";
            }
            record.setStatusName(statusName);

            String typeName = "";
            if (record.getType() == AccConstants.GEN_CODE_TYPE_SINGLE) {
                typeName = "普通生码";
            } else if (record.getType() == AccConstants.GEN_CODE_TYPE_BOX) {
                typeName = "套标生码";
            }
            record.setTypeName(typeName);
        }
        ExcelUtil<CodeRecord> util = new ExcelUtil<CodeRecord>(CodeRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 获取生码记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        CodeRecord codeRecord = codeRecordService.selectCodeRecordById(id);
        CodeRecordDetailVo vo = new CodeRecordDetailVo();
        vo.setRecordId(id);
        if (codeRecord.getType() == AccConstants.GEN_CODE_TYPE_SINGLE) {
            vo.setType("普通");
            vo.setSizeNum(String.valueOf(codeRecord.getCount()));
        } else {
            vo.setType("套标");
            vo.setSizeNum("1" + "拖" + codeRecord.getCount());
        }
        vo.setProductName(codeRecord.getProductName());
        vo.setBatchNo(codeRecord.getBatchNo());
        vo.setBarCode(codeRecord.getBarCode());
        if (codeRecord.getStatus() == AccConstants.CODE_RECORD_STATUS_WAIT) {
            vo.setStatus("创建中");
        } else if (codeRecord.getStatus() == AccConstants.CODE_RECORD_STATUS_FINISH) {
            vo.setStatus("待赋值");
        } else if (codeRecord.getStatus() == AccConstants.CODE_RECORD_STATUS_EVA) {
            vo.setStatus("已赋值");
        }
        vo.setCreateTime(DateUtils.dateTime(codeRecord.getCreateTime()));
        vo.setCodeIndexs(codeRecord.getIndexStart() + "~" + codeRecord.getIndexEnd());

        return AjaxResult.success(vo);
    }

    /**
     * 根据生码记录id查询码明细
     *
     * @param codeRecord
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:record:listSon')")
    @GetMapping("/listSon")
    public TableDataInfo listSon(CodeRecord codeRecord) {
        CodeAttr codeAttr = codeAttrService.selectCodeAttrByRecordId(codeRecord.getId());
        startPage();
        Code code = new Code();
        code.setCodeAttrId(codeAttr.getId());
        code.setCompanyId(codeAttr.getCompanyId());
        List<Code> list = codeService.selectCodeList(code);
        return getDataTable(list);
    }

    /**
     * 查询所有产品
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:record:listProduct')")
    @GetMapping("/listProduct")
    public AjaxResult listProduct() {
        Product product = new Product();
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            product.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        List<Product> productList = productService.selectTProductList(product);
        FuzhiVo fuzhiVo = new FuzhiVo();
        fuzhiVo.setProducts(productList);
        return AjaxResult.success(fuzhiVo);
    }

    /**
     * 根据产品查询批次
     *
     * @param productId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:record:listBatch')")
    @GetMapping("/listBatch/{productId}")
    public AjaxResult listBatch(@PathVariable("productId") long productId) {
        ProductBatch productBatch = new ProductBatch();
        productBatch.setProductId(productId);
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            productBatch.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        List<ProductBatch> productBatchList = productBatchService.selectProductBatchList(productBatch);
        FuzhiVo fuzhiVo = new FuzhiVo();
        fuzhiVo.setProductBatchs(productBatchList);
        return AjaxResult.success(fuzhiVo);
    }


//    /**
//     * 新增生码记录
//     */
//    @PreAuthorize("@ss.hasPermi('code:record:add')")
//    @Log(title = "生码记录", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody CodeRecord codeRecord) {
//        return toAjax(codeRecordService.insertCodeRecord(codeRecord));
//    }

    /**
     * 修改生码记录
     */
    @PreAuthorize("@ss.hasPermi('code:record:edit')")
    @Log(title = "生码记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CodeRecord codeRecord) {
        return toAjax(codeRecordService.updateCodeRecord(codeRecord));
    }

    /**
     * 删除生码记录
     */
    @PreAuthorize("@ss.hasPermi('code:record:remove')")
    @Log(title = "生码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(codeRecordService.deleteCodeRecordByIds(ids));
    }

    /**
     * 生码
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:record:add')")
    @Log(title = "生码记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CodeRecord codeRecord) {
        if (codeRecord.getType().equals(AccConstants.GEN_CODE_TYPE_SINGLE)) {
            return toAjax(codeRecordService.createCodeRecord(SecurityUtils.getLoginUserCompany().getDeptId(), codeRecord.getCount(), codeRecord.getRemark()));
        } else {
            long tayCount=1;
            if(codeRecord.getpType()==1){
                tayCount=codeRecord.getTrayCount();
            }
            return toAjax(codeRecordService.createPCodeRecord(SecurityUtils.getLoginUserCompany().getDeptId(),codeRecord.getpType() , tayCount, codeRecord.getBoxCount(),codeRecord.getCount(), codeRecord.getRemark()));
        }
    }

    /**
     * 码下载
     */
    @PreAuthorize("@ss.hasPermi('code:record:download')")
    @Log(title = "生码记录", businessType = BusinessType.EXPORT)
    @GetMapping("/download")
    public AjaxResult download(CodeRecord codeRecord) {
        CodeAttr codeAttr = codeAttrService.selectCodeAttrByRecordId(codeRecord.getId());
        Code codeParam = new Code();
        codeParam.setCodeAttrId(codeAttr.getId());
        codeParam.setCompanyId(codeAttr.getCompanyId());
        List<Code> list = codeService.selectCodeList(codeParam);
        for (Code code : list) {
            if (code.getStatus() == AccConstants.CODE_STATUS_WAIT) {
                code.setStatusName("待赋值");
            } else if (code.getStatus() == AccConstants.CODE_STATUS_FINISH) {
                code.setStatusName("已赋值");
            }

            if (code.getCodeType().equals(AccConstants.CODE_TYPE_SINGLE)) {
                code.setCodeTypeName("单码");
            } else if (code.getCodeType().equals(AccConstants.CODE_TYPE_BOX)) {
                code.setCodeTypeName("箱码");
            }
        }
        ExcelUtil<Code> util = new ExcelUtil<Code>(Code.class);
        return util.exportExcel(list, "Code");
    }

    /**
     * 生码赋值
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:record:fuzhi')")
    @Log(title = "生码记录", businessType = BusinessType.OTHER)
    @PostMapping("/fuzhi")
    public AjaxResult fuzhi(@RequestBody FuzhiVo fuzhiVo) {
        int res = 0;
        CodeAttr codeAttr = codeAttrService.selectCodeAttrByRecordId(fuzhiVo.getRecordId());
        Product product = productService.selectTProductById(fuzhiVo.getProductId());
        ProductBatch productBatch = productBatchService.selectProductBatchById(fuzhiVo.getBatchId());

        CodeAttr attrParam = new CodeAttr();
        attrParam.setId(codeAttr.getId());
        attrParam.setProductId(product.getId());
        attrParam.setProductName(product.getProductName());
        attrParam.setProductNo(product.getProductNo());
        attrParam.setBarCode(product.getBarCode());
        ProductCategory category1 = productCategoryService.selectProductCategoryById(product.getCategoryOne());
        ProductCategory category2 = productCategoryService.selectProductCategoryById(product.getCategoryTwo());
        attrParam.setProductCategory(category1.getCategoryName() + "-" + category2.getCategoryName());
        attrParam.setProductUnit(product.getUnit());
        attrParam.setProductIntroduce(product.getProductIntroduce());
        attrParam.setBatchId(fuzhiVo.getBatchId());
        attrParam.setBatchNo(productBatch.getBatchNo());
        attrParam.setRemark(fuzhiVo.getRemark());
        attrParam.setInputBy(SecurityUtils.getLoginUser().getUser().getUserId());
        attrParam.setInputTime(new Date());

        int updres = codeAttrService.updateCodeAttr(attrParam);
        if (updres > 0) {
            CodeRecord codeRecord = new CodeRecord();
            codeRecord.setId(fuzhiVo.getRecordId());
            codeRecord.setStatus(AccConstants.CODE_RECORD_STATUS_EVA);
            codeRecord.setProductId(fuzhiVo.getProductId());
            codeRecord.setBatchId(fuzhiVo.getBatchId());
            codeRecordService.updateCodeRecord(codeRecord);
            res = codeService.updateStatusByAttrId(codeAttr.getCompanyId(), codeAttr.getId(), AccConstants.CODE_STATUS_FINISH);
        }
        return toAjax(res);
    }
}
