package com.ztl.gym.web.controller.code;

import cn.hutool.core.collection.CollectionUtil;
import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.code.domain.vo.CodeRecordDetailVo;
import com.ztl.gym.code.domain.vo.FuzhiVo;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.config.RuoYiConfig;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.common.utils.file.FileUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.domain.ProductBatch;
import com.ztl.gym.product.domain.ProductCategory;
import com.ztl.gym.product.service.*;
import com.ztl.gym.storage.service.IStorageInService;
import com.ztl.gym.storage.service.IStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/code/record")
public class CodeRecordController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(CodeRecordController.class);

    @Autowired
    private CommonService commonService;
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
    @Autowired
    private IStorageInService storageInService;
    @Autowired
    private IProductStockFlowService productStockFlowService;
    @Autowired
    private IStorageService storageService;


    @Value("${ruoyi.preFixUrl}")
    private String preFixUrl;

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
            vo.setSizeNum(codeRecord.getBoxCount() + "拖" + codeRecord.getSingleCount());
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
        startPage();
        List<Code> list = codeService.selectCodeListByRecord(SecurityUtils.getLoginUserTopCompanyId(), codeRecord.getId());
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
     * 查询所有产品
     *
     * @return
     */
    @GetMapping("/listProductByProductNo")
    public AjaxResult listProductByProductNo(@RequestBody Map<String, Object> map) {
        Product product = new Product();
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            product.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        if (map.get("productNo") != "" && !"请选择".contains(map.get("productNo").toString())) {
            product.setProductName(map.get("productNo").toString());
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

    /**
     * 查询所有批次批次
     *
     * @return
     */
    @GetMapping("/listBatchAll")
    public AjaxResult listBatch() {
        ProductBatch productBatch = new ProductBatch();
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
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (codeRecord.getType().equals(AccConstants.GEN_CODE_TYPE_SINGLE)) {
            return toAjax(codeRecordService.createCodeRecord(companyId, codeRecord.getCount(), codeRecord.getRemark()));
        } else {
            return toAjax(codeRecordService.createPCodeRecord(companyId, codeRecord.getBoxCount(), codeRecord.getCount(), codeRecord.getRemark()));
        }
    }

    /**
     * 码下载
     */
    @Log(title = "生码记录", businessType = BusinessType.EXPORT)
    @CrossOrigin
    @GetMapping("/download")
    public void download(long id, long companyId, HttpServletResponse response) {
        List<Code> list = codeService.selectCodeListByRecord(companyId, id);
        for (Code code : list) {
            code.setCode(preFixUrl + code.getCode());
            if (code.getStatus() == AccConstants.CODE_STATUS_WAIT) {
                code.setStatusName("待赋值");
            } else if (code.getStatus() == AccConstants.CODE_STATUS_FINISH) {
                code.setStatusName("已赋值");
            }

            if (code.getCodeType().equals(AccConstants.CODE_TYPE_SINGLE)) {
                code.setCodeTypeName("单码");
                if (code.getpCode() != null) {
                    code.setpCode(preFixUrl + code.getpCode());
                }
            } else if (code.getCodeType().equals(AccConstants.CODE_TYPE_BOX)) {
                code.setCodeTypeName("箱码");
            }
        }
        ExcelUtil<Code> util = new ExcelUtil<Code>(Code.class);
        String fileName = util.exportExcel(list, "-" + DateUtils.getDate() + "码").get("msg").toString();

        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            FileUtils.deleteFile(filePath);
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }

    }

    /**
     * 码下载TXT
     */
    @PreAuthorize("@ss.hasPermi('code:record:download')")
    @GetMapping("/downloadTxt")
    public AjaxResult downloadTxt(CodeRecord codeRecord, HttpServletResponse response) {
        List<Code> list = codeService.selectCodeListByRecord(SecurityUtils.getLoginUserTopCompanyId(), codeRecord.getId());
        StringBuilder temp = new StringBuilder();
        temp.append("流水号,码                                        \r\n");
        for (Code code : list) {
            code.setCode(preFixUrl + code.getCode());
            if (code.getStatus() == AccConstants.CODE_STATUS_WAIT) {
                code.setStatusName("待赋值");
            } else if (code.getStatus() == AccConstants.CODE_STATUS_FINISH) {
                code.setStatusName("已赋值");
            }

            if (code.getCodeType().equals(AccConstants.CODE_TYPE_SINGLE)) {
                code.setCodeTypeName("单码");
                temp.append("        ").append(code.getCodeIndex()).append(",").append("        ").append(code.getCode()).append("\r\n");//流水号，码

            } else if (code.getCodeType().equals(AccConstants.CODE_TYPE_BOX)) {
                code.setCodeTypeName("箱码");
                temp.append(code.getCodeIndex()).append(",").append((code.getpCode() == null ? code.getCode() : code.getpCode())).append("\r\n");//流水号，码

            }
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", temp);
        return ajax;
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
        List<CodeAttr> codeAttrs = codeAttrService.selectCodeAttrByRecordId(fuzhiVo.getRecordId());
        Product product = productService.selectTProductById(fuzhiVo.getProductId());
        ProductBatch productBatch = productBatchService.selectProductBatchById(fuzhiVo.getBatchId());
        ProductCategory category1 = productCategoryService.selectProductCategoryById(product.getCategoryOne());
        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        String productCategory = category1.getCategoryName();
        Date inputTime = new Date();
        List<CodeAttr> codeAttrList = new LinkedList<>();
        CodeAttr attrParam = null;
        List<Long> storageRecordIds = new ArrayList<>();
        for (CodeAttr codeAttr : codeAttrs) {
            attrParam = new CodeAttr();
            attrParam.setId(codeAttr.getId());
            attrParam.setProductId(product.getId());
            attrParam.setProductName(product.getProductName());
            attrParam.setProductNo(product.getProductNo());
            attrParam.setBarCode(product.getBarCode());
            attrParam.setProductCategory(productCategory);
            attrParam.setProductUnit(product.getUnit());
            attrParam.setProductIntroduce(product.getProductIntroduce());
            attrParam.setBatchId(fuzhiVo.getBatchId());
            attrParam.setBatchNo(productBatch.getBatchNo());
            attrParam.setRemark(fuzhiVo.getRemark());
            attrParam.setInputBy(userId);
            attrParam.setInputTime(inputTime);
            attrParam.setUpdateTime(inputTime);
            codeAttrList.add(attrParam);


        }
        //批量更新
        if (!CollectionUtil.isEmpty(codeAttrList)) {
            codeAttrService.updateCodeAttrBatch(codeAttrList);
            //更新对应码的状态
            List idList = codeAttrList.stream().map(CodeAttr::getId).collect(Collectors.toList());
            codeService.updateStatusByAttrId(codeAttrs.get(0).getCompanyId(), idList, AccConstants.CODE_STATUS_FINISH);

            //2022-05-27 判定已经导入入库的标识更新库存中的产品ID
            storageRecordIds = codeService.selectStorageRecordIdsByAttrIds(codeAttrs.get(0).getCompanyId(), idList);
        }
        if (!CollectionUtil.isEmpty(storageRecordIds)) {
            //更新入库表产品ID
            storageInService.updateProductIdByIds(fuzhiVo.getProductId(), storageRecordIds);
            //更新库存
            for (int i = 0; i < storageRecordIds.size(); i++) {
                productStockFlowService.unBindProductStockFlowByInId(codeAttrs.get(0).getCompanyId(), storageRecordIds.get(i));
                storageService.updateProductStock(AccConstants.STORAGE_TYPE_IN, storageRecordIds.get(i));
            }
        }
        CodeRecord codeRecord = new CodeRecord();
        codeRecord.setId(fuzhiVo.getRecordId());
        codeRecord.setStatus(AccConstants.CODE_RECORD_STATUS_EVA);
        codeRecord.setProductId(fuzhiVo.getProductId());
        codeRecord.setBatchId(fuzhiVo.getBatchId());
        //更新生码记录赋值信息
        res = codeRecordService.updateCodeRecord(codeRecord);
        return toAjax(res);
    }

    /**
     * 查询该企业是否有状态为创建中的生码记录【用于前端生码记录页面自动刷新】
     *
     * @return
     */
    @GetMapping("/checkCodeStatus")
    public AjaxResult checkCodeStatus() {
        int res = 0;
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        CodeRecord codeRecord = new CodeRecord();
        codeRecord.setCompanyId(companyId);
        codeRecord.setStatus(AccConstants.CODE_RECORD_STATUS_WAIT);
        List<CodeRecord> codeRecords = codeRecordService.selectCodeRecordList(codeRecord);
        if (codeRecords.size() > 0) {
            res = 1;
        }
        return AjaxResult.success(res);
    }

    /**
     * 普通生码，单码List装箱
     */
    @PostMapping("/packageCode")
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult packageCode(@RequestBody Map<String, Object> map) {
        AjaxResult ajax = AjaxResult.success();

        List<String> list = (List) map.get("codes");
        if (list.size() > 0) {
            Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
            /**
             * 插入箱码，更新单码PCode
             */
            //获取并更新生码记录流水号
            String codeNoStr = CodeRuleUtils.getCodeIndex(companyId, 1, 0, CodeRuleUtils.CODE_PREFIX_B);
            String[] codeIndexs = codeNoStr.split("-");
            long codeIndex = Long.parseLong(codeIndexs[0]) + 1;

            String pCode = CodeRuleUtils.buildCode(companyId, CodeRuleUtils.CODE_PREFIX_B, codeIndex);

            Code temp = null;
            long codeAttrId = 0;

            for (int i = 0; i < list.size(); i++) {
                temp = new Code();
                temp.setCode(list.get(i));
                temp.setCompanyId(companyId);
                Code code = codeService.selectCode(temp);//查询单码数据
                if (code == null) {
                    throw new CustomException("未查询到相关码数据！");
                }
                codeAttrId = code.getCodeAttrId();
                if (code.getpCode() != null) {
                    throw new CustomException(code.getCode() + "该码已被扫描，请检查后重试！");
                }
                codeService.updatePCodeByCode(companyId, pCode, list.get(i));
            }//更新单码


            Code boxCode = new Code();
            boxCode.setCodeIndex(codeIndex);
            boxCode.setCompanyId(companyId);
            boxCode.setCodeType(AccConstants.CODE_TYPE_BOX);
            boxCode.setCode(pCode);
            boxCode.setCodeAttrId(codeAttrId);
            codeService.insertCode(boxCode);//插入箱码


            commonService.updateVal(companyId, codeIndex);//更新code_index
            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.put("companyId", companyId);
            mapTemp.put("boxCode", pCode);
            mapTemp.put("codeIndex", codeIndex);
            commonService.insertPrintData(mapTemp);//插入打印数据

            ajax.put("data", pCode);
            return ajax;
        } else {
            throw new CustomException("未接收到单码数据！");
        }

    }


}
