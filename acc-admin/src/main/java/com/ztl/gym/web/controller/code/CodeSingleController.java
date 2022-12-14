package com.ztl.gym.web.controller.code;

import com.ztl.gym.code.domain.*;
import com.ztl.gym.code.domain.vo.CodeRecordDetailVo;
import com.ztl.gym.code.domain.vo.FuzhiToOutVo;
import com.ztl.gym.code.domain.vo.FuzhiVo;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeSingleService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.config.RuoYiConfig;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.*;
import com.ztl.gym.common.utils.file.FileUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.print.domain.PrintData;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.domain.ProductBatch;
import com.ztl.gym.product.domain.ProductCategory;
import com.ztl.gym.product.service.IProductBatchService;
import com.ztl.gym.product.service.IProductCategoryService;
import com.ztl.gym.product.service.IProductService;
import com.ztl.gym.storage.domain.InCodeFlow;
import com.ztl.gym.storage.domain.Storage;
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.domain.vo.FlowVo;
import com.ztl.gym.storage.service.IStorageInService;
import com.ztl.gym.storage.service.IStorageOutService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/code/single")
public class CodeSingleController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CodeRecordController.class);
    @Autowired
    private CommonService commonService;
    @Autowired
    private ICodeSingleService codeSingleService;
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
    private IStorageService storageService;
    @Autowired
    private IStorageInService storageInService;
    @Autowired
    private IStorageOutService storageOutService;

    @Value("${ruoyi.preFixUrl}")
    private String preFixUrl;

    /**
     * ????????????????????????
     */
    @PreAuthorize("@ss.hasPermi('code:single:list')")
    @GetMapping("/list")
    public TableDataInfo list(CodeSingle codeSingle) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            codeSingle.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        startPage();
        List<CodeSingle> list = codeSingleService.selectCodeSingleList(codeSingle);
        return getDataTable(list);
    }

    /**
     * ????????????????????????
     */
    @PreAuthorize("@ss.hasPermi('code:single:export')")
    @Log(title = "????????????", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CodeSingle codeSingle) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            codeSingle.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        List<CodeSingle> list = codeSingleService.selectCodeSingleList(codeSingle);
        for (CodeSingle single : list) {
            String statusName = "";
            if (single.getStatus() == AccConstants.CODE_RECORD_STATUS_WAIT) {
                statusName = "?????????";
            } else if (single.getStatus() == AccConstants.CODE_RECORD_STATUS_FINISH) {
                statusName = "?????????";
            } else if (single.getStatus() == AccConstants.CODE_RECORD_STATUS_EVA) {
                statusName = "?????????";
            }
            single.setStatusName(statusName);

            String typeName = "";
            if (single.getType() == AccConstants.GEN_CODE_TYPE_SINGLE) {
                typeName = "????????????";
            } else if (single.getType() == AccConstants.GEN_CODE_TYPE_BOX) {
                typeName = "????????????";
            }
            single.setTypeName(typeName);
        }
        ExcelUtil<CodeSingle> util = new ExcelUtil<CodeSingle>(CodeSingle.class);
        return util.exportExcel(list, "single");
    }

    /**
     * ??????????????????????????????
     */
    @PreAuthorize("@ss.hasPermi('code:single:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        CodeSingle codeSingle = codeSingleService.selectCodeSingleById(id);
        CodeRecordDetailVo vo = new CodeRecordDetailVo();
        vo.setRecordId(id);

        vo.setType("??????");
        vo.setSizeNum(String.valueOf(codeSingle.getCount()));


        if (codeSingle.getStatus() == AccConstants.CODE_RECORD_STATUS_WAIT) {
            vo.setStatus("?????????");
        } else if (codeSingle.getStatus() == AccConstants.CODE_RECORD_STATUS_FINISH) {
            vo.setStatus("?????????");
        } else if (codeSingle.getStatus() == AccConstants.CODE_RECORD_STATUS_EVA) {
            vo.setStatus("?????????");
        }
        vo.setCreateTime(DateUtils.dateTime(codeSingle.getCreateTime()));
        vo.setCodeIndexs(codeSingle.getIndexStart() + "~" + codeSingle.getIndexEnd());

        return AjaxResult.success(vo);
    }

    /**
     * ??????????????????id???????????????
     *
     * @param codeSingle
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:single:listSon')")
    @GetMapping("/listSon")
    public TableDataInfo listSon(CodeSingle codeSingle) {
        startPage();
        List<Code> list = codeService.selectCodeListBySingle(SecurityUtils.getLoginUserTopCompanyId(), codeSingle.getId());
        return getDataTable(list);
    }


    /**
     * ??????????????????
     */
    @PreAuthorize("@ss.hasPermi('code:single:edit')")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CodeSingle codeSingle) {
        return toAjax(codeSingleService.updateCodeSingle(codeSingle));
    }

    /**
     * ??????????????????
     */
    @PreAuthorize("@ss.hasPermi('code:single:remove')")
    @Log(title = "????????????", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(codeSingleService.deleteCodeSingleByIds(ids));
    }

    /**
     * ??????
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:single:add')")
    @Log(title = "????????????", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CodeSingle codeSingle) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        return toAjax(codeSingleService.createCodeSingle(companyId, codeSingle.getCount(), codeSingle.getRemark()));

    }

    /**
     * ??????2022-03-21
     *
     * @return
     */
    @PostMapping("/addSingleCode")
    public AjaxResult addSingleCode(@RequestBody CodeRule codeRule) {
        if (codeRule.getCodeNo() == null || codeRule.getCodeNo() == "") {
            throw new CustomException("?????????????????????????????????");
        } else if (codeRule.getCodeDate() == null || codeRule.getCodeDate() == "") {
            throw new CustomException("???????????????????????????");
        } else if (codeRule.getLineNo() == null || codeRule.getLineNo() == "") {
            throw new CustomException("?????????????????????");
        } else if (codeRule.getFactoryNo() == null || codeRule.getFactoryNo() == "") {
            throw new CustomException("???????????????????????????");
        }
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        return AjaxResult.success(codeSingleService.createCodeSingleByRule(companyId, codeRule));

    }

    /**
     * ??????????????????
     *
     * @param lsCode
     * @return
     */
    @PostMapping("/insertCode")
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertCode(@RequestBody Map<String, Object> lsCode) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        List<Map<String, Object>> listObj = (List<Map<String, Object>>) lsCode.get("codeData");
        //????????????
        List<Code> listCode = new ArrayList<>();
        for (Object obj : listObj) {
            Map<String, Object> ob = (Map<String, Object>) obj;
            Code code = new Code();
            code.setCodeIndex(Long.valueOf(ob.get("codeIndex").toString()));
            code.setCompanyId(Long.valueOf(ob.get("companyId").toString()));
//            code.setTenantId(Long.valueOf(ob.get("tenantId").toString()));
//            code.setStorageType(Integer.valueOf(ob.get("storageType").toString()));
//            code.setStorageRecordId(Long.valueOf(ob.get("storageRecordId").toString()));
//            code.setStatus(Integer.valueOf(ob.get("status").toString()));
            code.setCode(ob.get("code").toString());
//            code.setCodeAcc(ob.get("codeAcc").toString());
            code.setCodeType(ob.get("codeType").toString());
//            code.setpCode(ob.get("pCode").toString());
//            code.setCodeAttrId(Long.valueOf(ob.get("codeAttrId").toString()));
            listCode.add(code);
        }
        if (listObj.size() > 0) {
            //??????Code??????
            int retInsertCodeCount = codeSingleService.insertCodeAll(listCode, companyId);
            if (listObj.size() == retInsertCodeCount) {
                return AjaxResult.success("????????????????????????");
            } else {
                return AjaxResult.success("??????????????????????????????");
            }
        } else {
            throw new CustomException("????????????????????????????????????");
        }
    }


    @PostMapping("/insertAllData")
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertAllData(@RequestBody Map<String, Object> data) throws ParseException {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        List<Map<String, Object>> listCodeObj = (List<Map<String, Object>>) data.get("codeData");
        List<Map<String, Object>> listCodeAttrData = (List<Map<String, Object>>) data.get("codeAttrData");
        List<Map<String, Object>> listStorageInData = (List<Map<String, Object>>) data.get("storageInData");
        List<Map<String, Object>> listPrintData = (List<Map<String, Object>>) data.get("printData");
        List<Map<String, Object>> listInCodeFlow = (List<Map<String, Object>>) data.get("inCodeFlowData");
        Map<String, Object> codeSequenceNew = (Map<String, Object>) data.get("codeSequenceNew");
        //????????????
        List<Code> listCode = new ArrayList<>();
        for (Object obj : listCodeObj) {
            Map<String, Object> ob = (Map<String, Object>) obj;
            Code code = new Code();
            code.setCodeIndex(Long.valueOf(ob.get("codeIndex").toString()));
            code.setCompanyId(Long.valueOf(ob.get("companyId").toString()));
            code.setCode(ob.get("code").toString());
            code.setCodeType(ob.get("codeType").toString());
            if (ob.get("tenantId") != null) {
                code.setTenantId(Long.valueOf(ob.get("tenantId").toString()));
            }
            if (ob.get("status") != null) {
                code.setStatus(Integer.valueOf(ob.get("status").toString()));
            }
            if (ob.get("singleId") != null) {
                code.setSingleId(Long.valueOf(ob.get("singleId").toString()));
            }
            if (ob.get("storageType") != null) {
                code.setStorageType(Integer.valueOf(ob.get("storageType").toString()));
            }
            if (ob.get("storageRecordId") != null) {
                code.setStorageRecordId(Long.valueOf(ob.get("storageRecordId").toString()));
            }
            if (ob.get("pCode") != null) {
                code.setpCode(ob.get("pCode").toString());
            }
            if (ob.get("codeAttrId") != null) {
                code.setCodeAttrId(Long.valueOf(ob.get("codeAttrId").toString()));
            }
            listCode.add(code);
        }
        List<CodeAttr> listCodeAttr = new ArrayList<>();
        for (Object obj : listCodeAttrData) {
            Map<String, Object> ob = (Map<String, Object>) obj;
            CodeAttr codeAttr = new CodeAttr();
            codeAttr.setId(Long.valueOf(ob.get("id").toString()));
            codeAttr.setCompanyId(Long.valueOf(ob.get("companyId").toString()));
            codeAttr.setProductId(Long.valueOf(ob.get("productId").toString()));
            codeAttr.setProductNo(ob.get("productNo").toString());
            codeAttr.setProductName(ob.get("productName").toString());
            codeAttr.setBatchId(Long.valueOf(ob.get("batchId").toString()));
            codeAttr.setBatchNo(ob.get("batchNo").toString());
            listCodeAttr.add(codeAttr);
        }
        List<StorageIn> listStorageIn = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (Object obj : listStorageInData) {
            Map<String, Object> ob = (Map<String, Object>) obj;
            StorageIn storageIn = new StorageIn();
            storageIn.setId(Long.valueOf(ob.get("id").toString()));
            storageIn.setCompanyId(Long.valueOf(ob.get("companyId").toString()));
            storageIn.setTenantId(Long.valueOf(ob.get("tenantId").toString()));
            storageIn.setStatus(Integer.valueOf(ob.get("status").toString()));
            storageIn.setInType(Integer.valueOf(ob.get("inType").toString()));
            storageIn.setInNo(ob.get("inNo").toString());
            storageIn.setProductId(Long.valueOf(ob.get("productId").toString()));
            storageIn.setInNum(Long.valueOf(ob.get("inNum").toString()));
            storageIn.setActInNum(Long.valueOf(ob.get("actInNum").toString()));
            storageIn.setStorageTo(Long.valueOf(ob.get("storageTo").toString()));
            storageIn.setToStorageId(Long.valueOf(ob.get("toStorageId").toString()));
            storageIn.setCreateUser(Long.valueOf(ob.get("createUser").toString()));
            storageIn.setCreateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(ob.get("createTime").toString()));
            storageIn.setUpdateUser(Long.valueOf(ob.get("updateUser").toString()));
            storageIn.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(ob.get("updateTime").toString()));
            storageIn.setInTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(ob.get("inTime").toString()));
            listStorageIn.add(storageIn);
        }
        List<PrintData> listPrint = new ArrayList<>();
        for (Object obj : listPrintData) {
            Map<String, Object> ob = (Map<String, Object>) obj;
            PrintData printData = new PrintData();
            printData.setCompanyId(Long.valueOf(ob.get("companyId").toString()));
            printData.setBoxCode(ob.get("boxCode").toString());
            printData.setCodeIndex(Long.valueOf(ob.get("codeIndex").toString()));
            printData.setPrintStatus(Integer.valueOf(ob.get("printStatus").toString()));
            printData.setProductLine(ob.get("productLine").toString());
            printData.setBoxNum(Long.valueOf(ob.get("boxNum").toString()));
            printData.setProductName(ob.get("productName").toString());
            printData.setProductModel(ob.get("productModel").toString());
            printData.setBatchName(ob.get("batchName").toString());
            printData.setProduceDate(ob.get("produceDate").toString());
            printData.setCodeCount(Long.valueOf(ob.get("codeCount").toString()));
            printData.setGrossWeight(ob.get("grossWeight").toString());
            printData.setNetWeight(ob.get("netWeight").toString());
            printData.setOrderNo(ob.get("orderNo").toString());
            printData.setBarCode(ob.get("barCode").toString());
            printData.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ob.get("createTime").toString()));
            listPrint.add(printData);
        }
        List<InCodeFlow> listFlow = new ArrayList<>();
        for (Object obj : listInCodeFlow) {
            Map<String, Object> ob = (Map<String, Object>) obj;
            InCodeFlow flow = new InCodeFlow();
            flow.setCompanyId(Long.valueOf(ob.get("companyId").toString()));
            flow.setCode(ob.get("code").toString());
            flow.setStorageRecordId(Long.valueOf(ob.get("storageRecordId").toString()));
            flow.setCreateUser(Long.valueOf(ob.get("createUser").toString()));
            flow.setCreateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(ob.get("createTime").toString()));
            listFlow.add(flow);
        }
        //?????????????????????
        if (listCode.size() > 0 && listCodeAttr.size() > 0 && listStorageIn.size() > 0 && listPrint.size() > 0 && listFlow.size() > 0 && codeSequenceNew.size() > 0) {
            //??????????????????
            int retInsertCodeSequenceNew = codeSingleService.insertCodeSequenceNew(codeSequenceNew);
            //??????Code??????
            int retInsertCodeCount = codeSingleService.insertCodeAll(listCode, companyId);
            int retInsertCodeAttrCount = codeAttrService.insertCodeAttrAll(listCodeAttr);
            int retInsertStorageInCount = storageInService.insertStorageInAll(listStorageIn);
            int retInsertPrintCount = commonService.insertPrintAll(listPrint);
            int retInsertInCodeFlowCount = storageInService.insertInCodeFlowAll(listFlow, companyId);
            if (listCodeObj.size() == retInsertCodeCount && listCodeAttrData.size() == retInsertCodeAttrCount &&
                    listStorageInData.size() == retInsertStorageInCount && listPrintData.size() == retInsertPrintCount && listInCodeFlow.size() == retInsertInCodeFlowCount) {
                return AjaxResult.success("????????????????????????");
            } else {
                return AjaxResult.success("??????????????????????????????");
            }
        } else {
            throw new CustomException("????????????????????????????????????");
        }
    }

    /**
     * ?????????
     */
    @Log(title = "????????????", businessType = BusinessType.EXPORT)
    @CrossOrigin
    @GetMapping("/download")
    public void download(long id, long companyId, HttpServletResponse response) {
        List<Code> list = codeService.selectCodeListBySingle(companyId, id);
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
    @PreAuthorize("@ss.hasPermi('code:single:download')")
    @GetMapping("/downloadTxt")
    public AjaxResult downloadTxt(CodeSingle codeSingle, HttpServletResponse response) {
        List<Code> list = codeService.selectCodeListBySingle(SecurityUtils.getLoginUserTopCompanyId(), codeSingle.getId());
        StringBuilder temp = new StringBuilder();
        temp.append("?????????,???                                     \r\n");
        for (Code code : list) {
            code.setCode(preFixUrl + code.getCode());
            if (code.getStatus() == AccConstants.CODE_STATUS_WAIT) {
                code.setStatusName("?????????");
            } else if (code.getStatus() == AccConstants.CODE_STATUS_FINISH) {
                code.setStatusName("?????????");
            }

            if (code.getCodeType().equals(AccConstants.CODE_TYPE_SINGLE)) {
                code.setCodeTypeName("??????");
                temp.append("        ").append(code.getCodeIndex()).append(",").append(code.getCode()).append("\r\n");//???????????????
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
     * ????????????,???????????????????????????
     *
     * @return
     */
    @Log(title = "????????????", businessType = BusinessType.INSERT)
    @PostMapping("/fuzhi")
    @Transactional
    @DataSource(DataSourceType.SHARDING)
    public AjaxResult fuzhi(@RequestBody FuzhiToOutVo fuzhiVo) {
        int res = 0;
        List<String> list1 = new ArrayList<String>();//?????????
        Product product = productService.selectTProductById(fuzhiVo.getProductId());
        ProductBatch productBatch = productBatchService.selectProductBatchById(fuzhiVo.getBatchId());
        ProductCategory category1 = productCategoryService.selectProductCategoryById(product.getCategoryOne());

        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        Long companyId = SecurityUtils.getLoginUserTopCompanyId();
        String productCategory = category1.getCategoryName();
        java.util.Date inputTime = new Date();

        CodeAttr codeAttr = new CodeAttr();
        codeAttr = new CodeAttr();
        codeAttr.setCompanyId(companyId);
        codeAttr.setTenantId(SecurityUtils.getLoginUserCompany().getDeptId());

        Long recordId = fuzhiVo.getRecordId();
        Long indexStart = fuzhiVo.getIndexStart();
        Long indexEnd = fuzhiVo.getIndexEnd();
        // ????????????????????????
        if (recordId == 0) {
            //????????????????????????????????????
            //step1???????????????????????????????????????
            Map<String, Object> map = new HashMap<>();
            map.put("companyId", companyId);
            map.put("indexBegin", indexStart);
            map.put("indexEnd", indexEnd);
            List<Code> list = codeService.selectCodeListByIndex(map);
            if (list.size() > 0) {

                if (list.get(list.size() - 1).getCodeIndex() < indexEnd) {
                    throw new CustomException("?????????????????????????????????????????????????????????", HttpStatus.ERROR);
                }
                for (int i = 0; i < list.size(); i++) {
                    list1.add(list.get(i).getCode());
                    if (list.get(i).getCodeAttr().getProductId() != null) {
                        throw new CustomException("?????????????????????????????????????????????????????????????????????", HttpStatus.ERROR);
                    }
                }
                recordId = list.get(0).getCodeAttr().getRecordId();//????????????recordId
            } else {
                throw new CustomException("?????????????????????????????????????????????????????????????????????", HttpStatus.ERROR);
            }
        }
        codeAttr.setRecordId(recordId);
        codeAttr.setIndexStart(indexStart);
        codeAttr.setIndexEnd(indexEnd);

        codeAttr.setProductId(fuzhiVo.getProductId());
        codeAttr.setProductName(product.getProductName());
        codeAttr.setProductNo(product.getProductNo());
        codeAttr.setBarCode(product.getBarCode());
        codeAttr.setProductCategory(productCategory);
        codeAttr.setProductUnit(product.getUnit());
        codeAttr.setProductIntroduce(product.getProductIntroduce());
        codeAttr.setBatchId(fuzhiVo.getBatchId());
        codeAttr.setBatchNo(productBatch.getBatchNo());
        codeAttr.setRemark(fuzhiVo.getRemark());
        codeAttr.setInputBy(userId);
        codeAttr.setCreateUser(userId);
        codeAttr.setInputTime(inputTime);
        codeAttr.setUpdateTime(inputTime);
        //????????????id
        Long attrId = commonService.updateGeneratorVal(codeAttr.getCompanyId(), 1, 1);
        codeAttr.setId(attrId + 1);
        //?????????????????????
        Long codeAttrId = codeAttrService.insertCodeAttr(codeAttr);

        //????????????????????????
        res = codeService.updateStatusByIndex(companyId, codeAttrId, recordId, indexStart, indexEnd, AccConstants.CODE_STATUS_FINISH);

        // step ???????????????????????????????????????????????????????????????????????????
        //?????????????????????
        Storage temp = new Storage();
        temp.setCompanyId(companyId);
        temp.setTenantId(companyId);
        long storageId;
        List<Storage> list = storageService.selectStorageList(temp);
        if (list.size() > 0) {
            storageId = list.get(0).getId();
        } else {
            Storage storage = new Storage();
            storage.setStorageName("????????????");
            storage.setStorageNo("1");
            storage.setCompanyId(companyId);
            storage.setTenantId(companyId);
            storageService.insertStorage(storage);
            storageId = storage.getId();
        }

        //??????

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("inNo", commonService.getStorageNo(1));
        map.put("productId", fuzhiVo.getProductId());
        map.put("batchNo", productBatch.getBatchNo());
        map.put("toStorageId", storageId);
        map.put("remark", "");
        map.put("thirdPartyFlag", "1");
        map.put("codes", list1);
        int show = storageInService.insertStorageIn(map);

        //??????
        StorageOut storageOut = new StorageOut();
        storageOut.setOutNo(commonService.getStorageNo(2));
        storageOut.setProductId(fuzhiVo.getProductId());
        storageOut.setBatchNo(productBatch.getBatchNo());
        storageOut.setStorageTo(fuzhiVo.getStorageTo());
        storageOut.setFromStorageId(storageId);
        storageOut.setThirdPartyFlag("1");
        storageOut.setOutTime(DateUtils.getNowDate());
        storageOut.setCodes(list1);//?????????
        storageOutService.insertStorageOut(storageOut);

        return AjaxResult.success("??????????????????");

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
        CodeSingle codeSingle = new CodeSingle();
        codeSingle.setCompanyId(companyId);
        codeSingle.setStatus(AccConstants.CODE_RECORD_STATUS_WAIT);
        List<CodeSingle> codeSingles = codeSingleService.selectCodeSingleList(codeSingle);
        if (codeSingles.size() > 0) {
            res = 1;
        }
        return AjaxResult.success(res);
    }

    /**
     * ?????????????????????List??????,?????????????????????
     */
    @GetMapping("/checkPackageCode")
    @DataSource(DataSourceType.SHARDING)
    public AjaxResult checkPackageCode(@RequestParam("code") String codeStr) {
        AjaxResult ajax = AjaxResult.success();
        codeStr = codeStr.trim();
        if (!codeStr.equals("")) {
            Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
            if (CodeRuleUtils.getCodeType(codeStr).equals(AccConstants.CODE_TYPE_BOX)) {
                throw new CustomException("????????????????????????", HttpStatus.ERROR);
            }
            Code temp = new Code();
            temp.setCode(codeStr);
            temp.setCompanyId(companyId);
            Code code = codeService.selectCode(temp);//??????????????????
            if (code == null) {
                throw new CustomException("??????????????????????????????", HttpStatus.ERROR);
            }
            if (code.getpCode() != null) {
                throw new CustomException(code.getCode() + "??????????????????????????????????????????", HttpStatus.ERROR);
            }
            if (code.getTenantId() != null && code.getCompanyId() != code.getTenantId()) {
                throw new CustomException(code.getCode() + "??????????????????????????????????????????", HttpStatus.ERROR);
            }
            ajax.put("data", code);
            return ajax;
        } else {
            throw new CustomException("???????????????????????????", HttpStatus.ERROR);
        }

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
            long singleId = 0;

            for (int i = 0; i < list.size(); i++) {
                temp = new Code();
                temp.setCode(list.get(i));
                temp.setCompanyId(companyId);
                Code code = codeService.selectCode(temp);//??????????????????
                if (code == null) {
                    throw new CustomException("??????????????????????????????");
                }
                if (code.getCodeAttrId() != null) {
                    codeAttrId = code.getCodeAttrId();
                }
                if (code.getSingleId() != null) {
                    singleId = code.getSingleId();
                }
                if (code.getpCode() != null) {
                    throw new CustomException(code.getCode() + "??????????????????????????????????????????");
                }
                if (code.getTenantId() != null && code.getCompanyId() != code.getTenantId()) {
                    throw new CustomException(code.getCode() + "??????????????????????????????????????????", HttpStatus.ERROR);
                }
                codeService.updatePCodeByCode(companyId, pCode, list.get(i));
            }//????????????


            Code boxCode = new Code();
            boxCode.setCodeIndex(codeIndex);
            boxCode.setCompanyId(companyId);
            boxCode.setCodeType(AccConstants.CODE_TYPE_BOX);
            boxCode.setCode(pCode);
            boxCode.setCodeAttrId(codeAttrId);
            boxCode.setSingleId(singleId);
            codeService.insertCode(boxCode);//????????????


            commonService.updateVal(companyId, codeIndex);//??????code_index
            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.put("companyId", companyId);
            mapTemp.put("boxCode", pCode);
            mapTemp.put("codeIndex", codeIndex);
            mapTemp.put("productLine", map.get("productLine"));
            mapTemp.put("boxNum", map.get("boxCount"));
            mapTemp.put("productName", map.get("productName"));
            mapTemp.put("productModel", map.get("productModel"));
            mapTemp.put("batchName", map.get("batchName"));
            mapTemp.put("printStatus", map.get("printStatus"));
            mapTemp.put("produceDate", map.get("date"));
            mapTemp.put("codeCount", map.get("codeCount"));
            mapTemp.put("grossWeight", map.get("grossWeight"));
            mapTemp.put("netWeight", map.get("netWeight"));
            mapTemp.put("orderNo", map.get("orderNo"));
            mapTemp.put("barCode", map.get("barCode"));
            mapTemp.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            commonService.insertPrintData(mapTemp);//??????????????????

            ajax.put("data", pCode);
            return ajax;
        } else {
            throw new CustomException("???????????????????????????");
        }

    }

    /**
     * ??????????????????
     *
     * @param map
     * @return
     */
    @PostMapping("/bindCodeAttr")
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult bindProductAttr(@RequestBody Map<String, Object> map) {
        if (map != null) {
            CodeAttr codeAttr = new CodeAttr();
            codeAttr.setCompanyId(Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
            codeAttr.setProductId(Long.valueOf(map.get("productId").toString()));
            codeAttr.setProductNo(map.get("productNo").toString());
            codeAttr.setProductName(map.get("productName").toString());
            codeAttr.setBatchId(Long.valueOf(map.get("batchId").toString()));
            codeAttr.setBatchNo(map.get("batchNo").toString());
            //?????????????????????
            Long codeAttrId = codeAttrService.insertCodeAttr(codeAttr);
            //?????????????????????
            Map<String, Object> params = new HashMap<>();
            params.put("pCode", map.get("pCode"));
            params.put("codeAttrId", Long.valueOf(codeAttrId));
            params.put("companyId", Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
            codeService.updateCodeAttrIdByPCode(params);
            return AjaxResult.success();
        } else {
            throw new CustomException("?????????????????????");
        }
    }
}
