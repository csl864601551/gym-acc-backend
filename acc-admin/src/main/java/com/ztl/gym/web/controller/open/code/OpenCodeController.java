package com.ztl.gym.web.controller.open.code;

import com.alibaba.fastjson.JSONArray;
import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.vo.CRMInfoVo;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.service.IProductService;
import com.ztl.gym.storage.domain.Erp;
import com.ztl.gym.storage.domain.ErpDetail;
import com.ztl.gym.storage.domain.Storage;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.service.IErpDetailService;
import com.ztl.gym.storage.service.IErpService;
import com.ztl.gym.storage.service.IStorageOutService;
import com.ztl.gym.storage.service.IStorageService;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/open/code")
public class OpenCodeController {

    @Autowired
    private ICodeService codeService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IErpService erpService;
    @Autowired
    private IErpDetailService erpDetailService;
    @Autowired
    private IStorageService storageService;
    @Autowired
    private IStorageOutService storageOutService;

    @Value("${ruoyi.preFixUrl}")
    private String preFixUrl;

    /**
     * ???????????????
     *
     * @return ???????????????
     */
    @PostMapping("getCodes")
    public AjaxResult getCodes(@RequestBody Map<String, Object> map) {
        Long companyId = Long.valueOf(SecurityUtils.getLoginUserTopCompanyId());
        map.put("companyId", companyId);
        //2022-05-26?????????????????????????????????????????????
        if (map.get("code") == null) {
            throw new CustomException("????????????????????????", HttpStatus.ERROR);
        } else {
            String code = map.get("code").toString();
            String codeType = CodeRuleUtils.getCodeType(code);
            //????????????????????????????????????
            if (AccConstants.CODE_TYPE_SINGLE.equals(codeType)) {
                codeService.deletePCodeBycode(code, companyId);
            }
        }


        List<Code> codeList = codeService.selectCodeListByCodeOrIndex(map);
        List<Map<String, Object>> res = new ArrayList<>();
        Map<String, Object> temp = new HashMap<>();
        if (codeList.size() > 0) {
            String code = codeList.get(0).getCode();
            if (!commonService.judgeStorageIsIllegalByValue(Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()), Integer.valueOf(map.get("storageType").toString()), code)) {
                throw new CustomException("?????????????????????????????????", HttpStatus.ERROR);
            }
            for (Code codes : codeList) {
                String typeName = "??????";
                if (CodeRuleUtils.getCodeType(codes.getCode()).equals(AccConstants.CODE_TYPE_BOX)) {
                    typeName = "??????";
                } else {
                    typeName = "??????";
                }
                codes.setCodeTypeName(typeName);
                temp = new HashMap<>();
                temp.put("codeIndex", codes.getCodeIndex());
                temp.put("companyId", codes.getCompanyId());
                temp.put("code", codes.getCode());
                temp.put("codeType", codes.getCodeType());
                temp.put("pCode", codes.getpCode());
                temp.put("codeTypeName", typeName);
                if (codes.getCodeAttr() != null) {
                    temp.put("productId", codes.getCodeAttr().getProductId());
                    temp.put("productName", codes.getCodeAttr().getProductName());
                    temp.put("productNo", codes.getCodeAttr().getProductNo());
                } else {
                    temp.put("productId", "0");
                    temp.put("productName", "");
                    temp.put("productNo", "");
                }
                res.add(temp);
            }
            return AjaxResult.success(res);
        } else {
            throw new CustomException("????????????????????????", HttpStatus.ERROR);
        }

    }

    /**
     * ????????????
     *
     * @return
     */
    @Log(title = "????????????", businessType = BusinessType.INSERT)
    @PostMapping("unBindCodes")
    public AjaxResult unBindCodes(@RequestBody Map<String, Object> map) {
        return AjaxResult.success(codeService.unBindCodes(map));
    }

    /**
     * ????????????
     *
     * @return
     */
    @Log(title = "????????????", businessType = BusinessType.INSERT)
    @PostMapping("unBindCodesByPCodes")
    public AjaxResult unBindCodesByPCodes(@RequestBody Map<String, List<String>> map) {
        return AjaxResult.success(codeService.unBindCodesByPCodes(map));
    }

    /**
     * ??????CRM????????????
     *
     * @return
     */
    @GetMapping("getCRMInfo")
    public AjaxResult getCRMInfo(@RequestBody Map<String, Object> map) {
        Date beginTime = null;
        Date endTime = null;
        try {
            beginTime = DateUtils.parseDate(map.get("beginTime").toString());
            endTime = DateUtils.parseDate(map.get("endTime").toString());
            if (map.get("dayFlag") == null) {
                String str = DateUtils.getDatePoor(endTime, beginTime);
                Integer dayNum = Integer.parseInt(str.substring(0, str.lastIndexOf("???")));
                if (dayNum > 32) {
                    throw new CustomException("??????????????????31??????????????????????????????", HttpStatus.ERROR);
                }
            }
        } catch (Exception e) {
            throw new CustomException("??????????????????????????????", HttpStatus.ERROR);
        }
        List<CRMInfoVo> crmInfo = codeService.getCRMInfo(preFixUrl, beginTime, endTime);
        if (crmInfo.size() > 0) {
            return AjaxResult.success(crmInfo);
        } else {
            throw new CustomException("??????????????????????????????", HttpStatus.ERROR);
        }
    }

    /**
     * ??????CRM????????????
     *
     * @return
     */
    @GetMapping("getCRMInfoByProductIds")
    public AjaxResult getCRMInfoByProductIds(@RequestBody Map<String, Object> map) {
        Date beginTime = null;
        Date endTime = null;
        List<String> productIds = new ArrayList<>();
        try {
            beginTime = DateUtils.parseDate(map.get("beginTime").toString());
            endTime = DateUtils.parseDate(map.get("endTime").toString());
            productIds = JSONArray.parseObject(JSONArray.toJSONString(map.get("productIds")), List.class);
            if (map.get("dayFlag") == null) {
                String str = DateUtils.getDatePoor(endTime, beginTime);
                Integer dayNum = Integer.parseInt(str.substring(0, str.lastIndexOf("???")));
                if (dayNum > 32) {
                    throw new CustomException("??????????????????31??????????????????????????????", HttpStatus.ERROR);
                }
            }
        } catch (Exception e) {
            throw new CustomException("??????????????????????????????", HttpStatus.ERROR);
        }
        List<CRMInfoVo> crmInfo = codeService.getCRMInfoByProductIds(preFixUrl, beginTime, endTime, productIds);
        if (crmInfo.size() > 0) {
            return AjaxResult.success(crmInfo);
        } else {
            throw new CustomException("??????????????????????????????", HttpStatus.ERROR);
        }
    }


    /**
     * ??????CRM????????????
     *
     * @return
     */
    @Log(title = "ERP??????", businessType = BusinessType.INSERT)
    @Transactional
    @PostMapping("getERPInfo")
    public AjaxResult getERPInfo(@RequestBody Erp erp) {
        //???????????????
        if (erp.getErpOutNo() == null || erp.getErpOutNo() == "") {
            throw new CustomException("?????????????????????????????????", HttpStatus.ERROR);
        }
        if (erp.getDeptNo() == null || erp.getDeptNo() == "") {
            throw new CustomException("???????????????????????????", HttpStatus.ERROR);
        }
        if (erp.getDeptName() == null || erp.getDeptName() == "") {
            throw new CustomException("??????????????????????????????", HttpStatus.ERROR);
        }
        if (erp.getPostName() == null || erp.getPostName() == "") {
            throw new CustomException("???????????????????????????", HttpStatus.ERROR);
        }
        if (erp.getOutTime() == null) {
            throw new CustomException("???????????????????????????", HttpStatus.ERROR);
        }
        if (erp.getOutProductList() == null || erp.getOutProductList().size() == 0) {
            throw new CustomException("????????????????????????????????????", HttpStatus.ERROR);
        }
        List<ErpDetail> outProductList = erp.getOutProductList();
        String deptNo = erp.getDeptNo();// ?????????????????????
        String deptName = erp.getDeptName();


        SysDept sysDept = SecurityUtils.getLoginUserCompany();
        Long companyId = SecurityUtils.getLoginUserTopCompanyId();
        //step ?????????????????????
        SysDept sysTenant = new SysDept();
        sysTenant.setParentId(companyId);
        sysTenant.setDeptNo(deptNo);
        List<SysDept> sysDeptList = sysDeptService.selectDeptList(sysTenant);
        if (sysDeptList.size() > 0) {
            sysTenant = sysDeptList.get(0);
        } else {
            throw new CustomException("?????????'" + deptNo + "???" + deptName + "'??????????????????,????????????????????????????????????", HttpStatus.ERROR);
//            sysTenant.setDeptNo(deptNo);
//            sysTenant.setOrderNum("1");
//            sysTenant.setDeptType(2);
//            sysTenant.setParentId(sysDept.getDeptId());
//            sysDeptService.insertDept(sysTenant);
        }
        Long tenantId = sysTenant.getDeptId();


        // step ???????????????????????????????????????????????????????????????????????????
        //?????????????????????
        Storage tempStorage = new Storage();
        tempStorage.setCompanyId(companyId);
        tempStorage.setTenantId(companyId);
        long storageId;
        List<Storage> list = storageService.selectStorageList(tempStorage);
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

        // step ??????????????????
        if (outProductList.size() > 0) {
            for (int i = 0; i < outProductList.size(); i++) {
                //????????????
                Product productTemp = new Product();
                productTemp.setBarCode(outProductList.get(i).getBarCode());
                List<Product> listProduct = productService.selectTProductList(productTemp);
                if (listProduct.size() > 0) {
                    outProductList.get(i).setProductId(listProduct.get(0).getId());
                } else {
                    throw new CustomException("?????????'" + outProductList.get(i).getBarCode() + "???" + outProductList.get(i).getProductName() + "'???????????????,?????????????????????????????????", HttpStatus.ERROR);
                }

                Erp temp = new Erp();
                temp.setErpOutNo(erp.getErpOutNo());
                List<Erp> erpList = erpService.selectErpList(temp);
                try {
                    erp.setCompanyId(companyId);
                    erp.setDeptId(tenantId);
                    erp.setStatus(0L);
                    erpService.insertErp(erp);
                } catch (Exception e) {
                    throw new CustomException("???????????????????????????", HttpStatus.ERROR);
                }
                outProductList.get(i).setErpId(erp.getId());
                outProductList.get(i).setActNum(0L);
                outProductList.get(i).setStatus(0L);
                erpDetailService.insertErpDetail(outProductList.get(i));

                if (erpList.size() > 0) {
                    //throw new CustomException("?????????????????????????????????", HttpStatus.ERROR);
                    //?????????????????????
                    StorageOut storageOutQuery = new StorageOut();
                    storageOutQuery.setBatchNo(erp.getErpOutNo());
                    List<StorageOut> storageOutList = storageOutService.selectStorageOutList(storageOutQuery);
                    if (storageOutList.size() > 0 && storageOutList.get(0).getActOutNum() > 0) {
                        throw new CustomException("???????????????????????????????????????????????????", HttpStatus.ERROR);
                    }
                    //??????????????????
                    //??????
                    StorageOut storageOut = new StorageOut();
                    storageOut.setProductId(listProduct.get(0).getId());
                    storageOut.setBatchNo(erp.getErpOutNo());
                    storageOut.setStorageTo(sysTenant.getDeptId());
                    storageOut.setOutNum(outProductList.get(i).getOutNum());
                    storageOut.setFromStorageId(storageId);
                    storageOut.setOutTime(DateUtils.getNowDate());
                    storageOut.setRemark(erp.getPostName());
                    storageOutService.updateStorageOutByErpCode(storageOut);
                } else {
                    //??????????????????
                    //??????
                    StorageOut storageOut = new StorageOut();
                    storageOut.setOutNo(commonService.getStorageNo(2));
                    storageOut.setProductId(listProduct.get(0).getId());
                    storageOut.setBatchNo(erp.getErpOutNo());
                    storageOut.setStorageTo(sysTenant.getDeptId());
                    storageOut.setOutNum(outProductList.get(i).getOutNum());
                    storageOut.setFromStorageId(storageId);
                    storageOut.setOutTime(DateUtils.getNowDate());
                    storageOut.setRemark(erp.getPostName());
                    storageOutService.insertStorageOut(storageOut);
                }

            }
        }


        return AjaxResult.success("????????????", "??????????????????");
    }

    /**
     * ???????????????
     *
     * @return ???????????????
     */
    @PostMapping("getQualityCodes")
    public AjaxResult getQualityCodes(@RequestBody Map<String, Object> map) {
        Long companyId = Long.valueOf(SecurityUtils.getLoginUserTopCompanyId());
        map.put("companyId", companyId);
        //2022-05-26?????????????????????????????????????????????
        if (map.get("code") == null) {
            throw new CustomException("????????????????????????", HttpStatus.ERROR);
        }
        List<Code> codeList = codeService.selectCodeListByCodeOrIndex(map);
        List<Map<String, Object>> res = new ArrayList<>();
        Map<String, Object> temp = new HashMap<>();
        if (codeList.size() > 0) {
            for (Code codes : codeList) {
                String typeName = "??????";
                if (CodeRuleUtils.getCodeType(codes.getCode()).equals(AccConstants.CODE_TYPE_BOX)) {
                    typeName = "??????";
                } else {
                    typeName = "??????";
                }
                codes.setCodeTypeName(typeName);
                temp = new HashMap<>();
                temp.put("codeIndex", codes.getCodeIndex());
                temp.put("companyId", codes.getCompanyId());
                temp.put("code", codes.getCode());
                temp.put("codeType", codes.getCodeType());
                temp.put("pCode", codes.getpCode());
                temp.put("codeTypeName", typeName);
                if (codes.getCodeAttr() != null) {
                    temp.put("productId", codes.getCodeAttr().getProductId());
                    temp.put("productName", codes.getCodeAttr().getProductName());
                    temp.put("productNo", codes.getCodeAttr().getProductNo());
                } else {
                    temp.put("productId", "0");
                    temp.put("productName", "");
                    temp.put("productNo", "");
                }
                res.add(temp);
            }
            return AjaxResult.success(res);
        } else {
            throw new CustomException("????????????????????????", HttpStatus.ERROR);
        }

    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @GetMapping("/getCodesCount")
    public AjaxResult getCodesCount(@RequestBody List<String> codes) {
        return AjaxResult.success(codeService.getCodesCount(codes));
    }
}
