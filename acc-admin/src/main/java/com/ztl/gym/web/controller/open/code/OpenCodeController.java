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
     * 获取所有码
     *
     * @return 获取所有码
     */
    @PostMapping("getCodes")
    public AjaxResult getCodes(@RequestBody Map<String, Object> map) {
        Long companyId = Long.valueOf(SecurityUtils.getLoginUserTopCompanyId());
        map.put("companyId", companyId);
        //2022-05-26新增需求，扫码单码直接解绑箱码
        if (map.get("code") == null) {
            throw new CustomException("未获取到码信息！", HttpStatus.ERROR);
        } else {
            String code = map.get("code").toString();
            String codeType = CodeRuleUtils.getCodeType(code);
            //判定单码执行解绑箱码操作
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
                throw new CustomException("该码不在当前流转节点！", HttpStatus.ERROR);
            }
            for (Code codes : codeList) {
                String typeName = "未知";
                if (CodeRuleUtils.getCodeType(codes.getCode()).equals(AccConstants.CODE_TYPE_BOX)) {
                    typeName = "箱码";
                } else {
                    typeName = "单码";
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
            throw new CustomException("未查询到码数据！", HttpStatus.ERROR);
        }

    }

    /**
     * 扫码解绑
     *
     * @return
     */
    @Log(title = "扫码解绑", businessType = BusinessType.INSERT)
    @PostMapping("unBindCodes")
    public AjaxResult unBindCodes(@RequestBody Map<String, Object> map) {
        return AjaxResult.success(codeService.unBindCodes(map));
    }

    /**
     * 接口解绑
     *
     * @return
     */
    @Log(title = "接口解绑", businessType = BusinessType.INSERT)
    @PostMapping("unBindCodesByPCodes")
    public AjaxResult unBindCodesByPCodes(@RequestBody Map<String, List<String>> map) {
        return AjaxResult.success(codeService.unBindCodesByPCodes(map));
    }

    /**
     * 对接CRM开放接口
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
                Integer dayNum = Integer.parseInt(str.substring(0, str.lastIndexOf("天")));
                if (dayNum > 32) {
                    throw new CustomException("时间范围限制31天，请输入重新输入！", HttpStatus.ERROR);
                }
            }
        } catch (Exception e) {
            throw new CustomException("请输入正确时间范围！", HttpStatus.ERROR);
        }
        List<CRMInfoVo> crmInfo = codeService.getCRMInfo(preFixUrl, beginTime, endTime);
        if (crmInfo.size() > 0) {
            return AjaxResult.success(crmInfo);
        } else {
            throw new CustomException("该时间段无相关数据！", HttpStatus.ERROR);
        }
    }

    /**
     * 对接CRM开放接口
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
                Integer dayNum = Integer.parseInt(str.substring(0, str.lastIndexOf("天")));
                if (dayNum > 32) {
                    throw new CustomException("时间范围限制31天，请输入重新输入！", HttpStatus.ERROR);
                }
            }
        } catch (Exception e) {
            throw new CustomException("请输入正确时间范围！", HttpStatus.ERROR);
        }
        List<CRMInfoVo> crmInfo = codeService.getCRMInfoByProductIds(preFixUrl, beginTime, endTime, productIds);
        if (crmInfo.size() > 0) {
            return AjaxResult.success(crmInfo);
        } else {
            throw new CustomException("该时间段无相关数据！", HttpStatus.ERROR);
        }
    }


    /**
     * 对接CRM开放接口
     *
     * @return
     */
    @Log(title = "ERP对接", businessType = BusinessType.INSERT)
    @Transactional
    @PostMapping("getERPInfo")
    public AjaxResult getERPInfo(@RequestBody Erp erp) {
        //初始化数据
        if (erp.getErpOutNo() == null || erp.getErpOutNo() == "") {
            throw new CustomException("发货单据编号不能为空！", HttpStatus.ERROR);
        }
        if (erp.getDeptNo() == null || erp.getDeptNo() == "") {
            throw new CustomException("客户编号不能为空！", HttpStatus.ERROR);
        }
        if (erp.getDeptName() == null || erp.getDeptName() == "") {
            throw new CustomException("经销商名称不能为空！", HttpStatus.ERROR);
        }
        if (erp.getPostName() == null || erp.getPostName() == "") {
            throw new CustomException("出库组织不能为空！", HttpStatus.ERROR);
        }
        if (erp.getOutTime() == null) {
            throw new CustomException("发货日期不能为空！", HttpStatus.ERROR);
        }
        if (erp.getOutProductList() == null || erp.getOutProductList().size() == 0) {
            throw new CustomException("发货单产品明细不能为空！", HttpStatus.ERROR);
        }
        List<ErpDetail> outProductList = erp.getOutProductList();
        String deptNo = erp.getDeptNo();// 添加经销商编号
        String deptName = erp.getDeptName();


        SysDept sysDept = SecurityUtils.getLoginUserCompany();
        Long companyId = SecurityUtils.getLoginUserTopCompanyId();
        //step 判定经销商存在
        SysDept sysTenant = new SysDept();
        sysTenant.setParentId(companyId);
        sysTenant.setDeptNo(deptNo);
        List<SysDept> sysDeptList = sysDeptService.selectDeptList(sysTenant);
        if (sysDeptList.size() > 0) {
            sysTenant = sysDeptList.get(0);
        } else {
            throw new CustomException("不存在'" + deptNo + "：" + deptName + "'该经销商信息,请联系维护该经销商信息！", HttpStatus.ERROR);
//            sysTenant.setDeptNo(deptNo);
//            sysTenant.setOrderNum("1");
//            sysTenant.setDeptType(2);
//            sysTenant.setParentId(sysDept.getDeptId());
//            sysDeptService.insertDept(sysTenant);
        }
        Long tenantId = sysTenant.getDeptId();


        // step 判断仓库是否存在，设置默认仓库（提前处理仓库问题）
        //处理无仓库问题
        Storage tempStorage = new Storage();
        tempStorage.setCompanyId(companyId);
        tempStorage.setTenantId(companyId);
        long storageId;
        List<Storage> list = storageService.selectStorageList(tempStorage);
        if (list.size() > 0) {
            storageId = list.get(0).getId();
        } else {
            Storage storage = new Storage();
            storage.setStorageName("默认仓库");
            storage.setStorageNo("1");
            storage.setCompanyId(companyId);
            storage.setTenantId(companyId);
            storageService.insertStorage(storage);
            storageId = storage.getId();
        }

        // step 判断产品存在
        if (outProductList.size() > 0) {
            for (int i = 0; i < outProductList.size(); i++) {
                //处理产品
                Product productTemp = new Product();
                productTemp.setBarCode(outProductList.get(i).getBarCode());
                List<Product> listProduct = productService.selectTProductList(productTemp);
                if (listProduct.size() > 0) {
                    outProductList.get(i).setProductId(listProduct.get(0).getId());
                } else {
                    throw new CustomException("不存在'" + outProductList.get(i).getBarCode() + "：" + outProductList.get(i).getProductName() + "'该产品信息,请联系维护该产品信息！", HttpStatus.ERROR);
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
                    throw new CustomException("接收数据格式错误！", HttpStatus.ERROR);
                }
                outProductList.get(i).setErpId(erp.getId());
                outProductList.get(i).setActNum(0L);
                outProductList.get(i).setStatus(0L);
                erpDetailService.insertErpDetail(outProductList.get(i));

                if (erpList.size() > 0) {
                    //throw new CustomException("该发货单据编号已同步！", HttpStatus.ERROR);
                    //判断是否已出库
                    StorageOut storageOutQuery = new StorageOut();
                    storageOutQuery.setBatchNo(erp.getErpOutNo());
                    List<StorageOut> storageOutList = storageOutService.selectStorageOutList(storageOutQuery);
                    if (storageOutList.size() > 0 && storageOutList.get(0).getActOutNum() > 0) {
                        throw new CustomException("该发货单据编号已出库，不允许修改！", HttpStatus.ERROR);
                    }
                    //插入待出库单
                    //出库
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
                    //插入待出库单
                    //出库
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


        return AjaxResult.success("请求成功", "同步数据成功");
    }

    /**
     * 获取所有码
     *
     * @return 获取所有码
     */
    @PostMapping("getQualityCodes")
    public AjaxResult getQualityCodes(@RequestBody Map<String, Object> map) {
        Long companyId = Long.valueOf(SecurityUtils.getLoginUserTopCompanyId());
        map.put("companyId", companyId);
        //2022-05-26新增需求，扫码单码直接解绑箱码
        if (map.get("code") == null) {
            throw new CustomException("未获取到码信息！", HttpStatus.ERROR);
        }
        List<Code> codeList = codeService.selectCodeListByCodeOrIndex(map);
        List<Map<String, Object>> res = new ArrayList<>();
        Map<String, Object> temp = new HashMap<>();
        if (codeList.size() > 0) {
            for (Code codes : codeList) {
                String typeName = "未知";
                if (CodeRuleUtils.getCodeType(codes.getCode()).equals(AccConstants.CODE_TYPE_BOX)) {
                    typeName = "箱码";
                } else {
                    typeName = "单码";
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
            throw new CustomException("未查询到码数据！", HttpStatus.ERROR);
        }

    }

    /**
     * 查询码得单品数量
     *
     * @return
     */
    @GetMapping("/getCodesCount")
    public AjaxResult getCodesCount(@RequestBody List<String> codes) {
        return AjaxResult.success(codeService.getCodesCount(codes));
    }
}
