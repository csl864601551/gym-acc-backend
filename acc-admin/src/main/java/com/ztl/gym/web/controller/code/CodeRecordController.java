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
     * ????????????????????????
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
     * ????????????????????????
     */
    @PreAuthorize("@ss.hasPermi('code:record:export')")
    @Log(title = "????????????", businessType = BusinessType.EXPORT)
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
                statusName = "?????????";
            } else if (record.getStatus() == AccConstants.CODE_RECORD_STATUS_FINISH) {
                statusName = "?????????";
            } else if (record.getStatus() == AccConstants.CODE_RECORD_STATUS_EVA) {
                statusName = "?????????";
            }
            record.setStatusName(statusName);

            String typeName = "";
            if (record.getType() == AccConstants.GEN_CODE_TYPE_SINGLE) {
                typeName = "????????????";
            } else if (record.getType() == AccConstants.GEN_CODE_TYPE_BOX) {
                typeName = "????????????";
            }
            record.setTypeName(typeName);
        }
        ExcelUtil<CodeRecord> util = new ExcelUtil<CodeRecord>(CodeRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * ??????????????????????????????
     */
    @PreAuthorize("@ss.hasPermi('code:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        CodeRecord codeRecord = codeRecordService.selectCodeRecordById(id);
        CodeRecordDetailVo vo = new CodeRecordDetailVo();
        vo.setRecordId(id);
        if (codeRecord.getType() == AccConstants.GEN_CODE_TYPE_SINGLE) {
            vo.setType("??????");
            vo.setSizeNum(String.valueOf(codeRecord.getCount()));
        } else {
            vo.setType("??????");
            vo.setSizeNum(codeRecord.getBoxCount() + "???" + codeRecord.getSingleCount());
        }
        vo.setProductName(codeRecord.getProductName());
        vo.setBatchNo(codeRecord.getBatchNo());
        vo.setBarCode(codeRecord.getBarCode());
        if (codeRecord.getStatus() == AccConstants.CODE_RECORD_STATUS_WAIT) {
            vo.setStatus("?????????");
        } else if (codeRecord.getStatus() == AccConstants.CODE_RECORD_STATUS_FINISH) {
            vo.setStatus("?????????");
        } else if (codeRecord.getStatus() == AccConstants.CODE_RECORD_STATUS_EVA) {
            vo.setStatus("?????????");
        }
        vo.setCreateTime(DateUtils.dateTime(codeRecord.getCreateTime()));
        vo.setCodeIndexs(codeRecord.getIndexStart() + "~" + codeRecord.getIndexEnd());

        return AjaxResult.success(vo);
    }

    /**
     * ??????????????????id???????????????
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
     * ??????????????????
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
     * ??????????????????
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
        if (map.get("productNo") != "" && !"?????????".contains(map.get("productNo").toString())) {
            product.setProductName(map.get("productNo").toString());
        }
        List<Product> productList = productService.selectTProductList(product);
        FuzhiVo fuzhiVo = new FuzhiVo();
        fuzhiVo.setProducts(productList);
        return AjaxResult.success(fuzhiVo);
    }

    /**
     * ????????????????????????
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
     * ????????????????????????
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
//     * ??????????????????
//     */
//    @PreAuthorize("@ss.hasPermi('code:record:add')")
//    @Log(title = "????????????", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody CodeRecord codeRecord) {
//        return toAjax(codeRecordService.insertCodeRecord(codeRecord));
//    }

    /**
     * ??????????????????
     */
    @PreAuthorize("@ss.hasPermi('code:record:edit')")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CodeRecord codeRecord) {
        return toAjax(codeRecordService.updateCodeRecord(codeRecord));
    }

    /**
     * ??????????????????
     */
    @PreAuthorize("@ss.hasPermi('code:record:remove')")
    @Log(title = "????????????", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(codeRecordService.deleteCodeRecordByIds(ids));
    }

    /**
     * ??????
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:record:add')")
    @Log(title = "????????????", businessType = BusinessType.INSERT)
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
     * ?????????
     */
    @Log(title = "????????????", businessType = BusinessType.EXPORT)
    @CrossOrigin
    @GetMapping("/download")
    public void download(long id, long companyId, HttpServletResponse response) {
        List<Code> list = codeService.selectCodeListByRecord(companyId, id);
        for (Code code : list) {
            code.setCode(preFixUrl + code.getCode());
            if (code.getStatus() == AccConstants.CODE_STATUS_WAIT) {
                code.setStatusName("?????????");
            } else if (code.getStatus() == AccConstants.CODE_STATUS_FINISH) {
                code.setStatusName("?????????");
            }

            if (code.getCodeType().equals(AccConstants.CODE_TYPE_SINGLE)) {
                code.setCodeTypeName("??????");
                if (code.getpCode() != null) {
                    code.setpCode(preFixUrl + code.getpCode());
                }
            } else if (code.getCodeType().equals(AccConstants.CODE_TYPE_BOX)) {
                code.setCodeTypeName("??????");
            }
        }
        ExcelUtil<Code> util = new ExcelUtil<Code>(Code.class);
        String fileName = util.exportExcel(list, "-" + DateUtils.getDate() + "???").get("msg").toString();

        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("????????????({})??????????????????????????? ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            FileUtils.deleteFile(filePath);
        } catch (Exception e) {
            log.error("??????????????????", e);
        }

    }

    /**
     * ?????????TXT
     */
    @PreAuthorize("@ss.hasPermi('code:record:download')")
    @GetMapping("/downloadTxt")
    public AjaxResult downloadTxt(CodeRecord codeRecord, HttpServletResponse response) {
        List<Code> list = codeService.selectCodeListByRecord(SecurityUtils.getLoginUserTopCompanyId(), codeRecord.getId());
        StringBuilder temp = new StringBuilder();
        temp.append("?????????,???                                        \r\n");
        for (Code code : list) {
            code.setCode(preFixUrl + code.getCode());
            if (code.getStatus() == AccConstants.CODE_STATUS_WAIT) {
                code.setStatusName("?????????");
            } else if (code.getStatus() == AccConstants.CODE_STATUS_FINISH) {
                code.setStatusName("?????????");
            }

            if (code.getCodeType().equals(AccConstants.CODE_TYPE_SINGLE)) {
                code.setCodeTypeName("??????");
                temp.append("        ").append(code.getCodeIndex()).append(",").append("        ").append(code.getCode()).append("\r\n");//???????????????

            } else if (code.getCodeType().equals(AccConstants.CODE_TYPE_BOX)) {
                code.setCodeTypeName("??????");
                temp.append(code.getCodeIndex()).append(",").append((code.getpCode() == null ? code.getCode() : code.getpCode())).append("\r\n");//???????????????

            }
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", temp);
        return ajax;
    }

    /**
     * ????????????
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:record:fuzhi')")
    @Log(title = "????????????", businessType = BusinessType.OTHER)
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
        //????????????
        if (!CollectionUtil.isEmpty(codeAttrList)) {
            codeAttrService.updateCodeAttrBatch(codeAttrList);
            //????????????????????????
            List idList = codeAttrList.stream().map(CodeAttr::getId).collect(Collectors.toList());
            codeService.updateStatusByAttrId(codeAttrs.get(0).getCompanyId(), idList, AccConstants.CODE_STATUS_FINISH);

            //2022-05-27 ?????????????????????????????????????????????????????????ID
            storageRecordIds = codeService.selectStorageRecordIdsByAttrIds(codeAttrs.get(0).getCompanyId(), idList);
        }
        if (!CollectionUtil.isEmpty(storageRecordIds)) {
            //?????????????????????ID
            storageInService.updateProductIdByIds(fuzhiVo.getProductId(), storageRecordIds);
            //????????????
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
        //??????????????????????????????
        res = codeRecordService.updateCodeRecord(codeRecord);
        return toAjax(res);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????
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
     * ?????????????????????List??????
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
             * ???????????????????????????PCode
             */
            //????????????????????????????????????
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
                Code code = codeService.selectCode(temp);//??????????????????
                if (code == null) {
                    throw new CustomException("??????????????????????????????");
                }
                codeAttrId = code.getCodeAttrId();
                if (code.getpCode() != null) {
                    throw new CustomException(code.getCode() + "??????????????????????????????????????????");
                }
                codeService.updatePCodeByCode(companyId, pCode, list.get(i));
            }//????????????


            Code boxCode = new Code();
            boxCode.setCodeIndex(codeIndex);
            boxCode.setCompanyId(companyId);
            boxCode.setCodeType(AccConstants.CODE_TYPE_BOX);
            boxCode.setCode(pCode);
            boxCode.setCodeAttrId(codeAttrId);
            codeService.insertCode(boxCode);//????????????


            commonService.updateVal(companyId, codeIndex);//??????code_index
            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.put("companyId", companyId);
            mapTemp.put("boxCode", pCode);
            mapTemp.put("codeIndex", codeIndex);
            commonService.insertPrintData(mapTemp);//??????????????????

            ajax.put("data", pCode);
            return ajax;
        } else {
            throw new CustomException("???????????????????????????");
        }

    }


}
