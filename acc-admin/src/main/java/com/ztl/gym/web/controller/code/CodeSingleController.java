package com.ztl.gym.web.controller.code;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.primitives.Longs;
import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.code.domain.CodeSingle;
import com.ztl.gym.code.domain.vo.CodeRecordDetailVo;
import com.ztl.gym.code.domain.vo.FuzhiVo;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeSingleService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
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
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.domain.ProductBatch;
import com.ztl.gym.product.domain.ProductCategory;
import com.ztl.gym.product.service.IProductBatchService;
import com.ztl.gym.product.service.IProductCategoryService;
import com.ztl.gym.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/code/single")
public class CodeSingleController extends BaseController {
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
    private IProductService tProductService;

    @Value("${ruoyi.preFixUrl}")
    private String preFixUrl;
    /**
     * 查询生码记录列表
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
     * 导出生码记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:single:export')")
    @Log(title = "生码记录", businessType = BusinessType.EXPORT)
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
                statusName = "创建中";
            } else if (single.getStatus() == AccConstants.CODE_RECORD_STATUS_FINISH) {
                statusName = "待赋值";
            } else if (single.getStatus() == AccConstants.CODE_RECORD_STATUS_EVA) {
                statusName = "已赋值";
            }
            single.setStatusName(statusName);

            String typeName = "";
            if (single.getType() == AccConstants.GEN_CODE_TYPE_SINGLE) {
                typeName = "普通生码";
            } else if (single.getType() == AccConstants.GEN_CODE_TYPE_BOX) {
                typeName = "套标生码";
            }
            single.setTypeName(typeName);
        }
        ExcelUtil<CodeSingle> util = new ExcelUtil<CodeSingle>(CodeSingle.class);
        return util.exportExcel(list, "single");
    }

    /**
     * 获取生码记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:single:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        CodeSingle codeSingle = codeSingleService.selectCodeSingleById(id);
        CodeRecordDetailVo vo = new CodeRecordDetailVo();
        vo.setRecordId(id);

        vo.setType("普通");
        vo.setSizeNum(String.valueOf(codeSingle.getCount()));


        if (codeSingle.getStatus() == AccConstants.CODE_RECORD_STATUS_WAIT) {
            vo.setStatus("创建中");
        } else if (codeSingle.getStatus() == AccConstants.CODE_RECORD_STATUS_FINISH) {
            vo.setStatus("待赋值");
        } else if (codeSingle.getStatus() == AccConstants.CODE_RECORD_STATUS_EVA) {
            vo.setStatus("已赋值");
        }
        vo.setCreateTime(DateUtils.dateTime(codeSingle.getCreateTime()));
        vo.setCodeIndexs(codeSingle.getIndexStart() + "~" + codeSingle.getIndexEnd());

        return AjaxResult.success(vo);
    }

    /**
     * 根据生码记录id查询码明细
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
     * 修改生码记录
     */
    @PreAuthorize("@ss.hasPermi('code:single:edit')")
    @Log(title = "生码记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CodeSingle codeSingle) {
        return toAjax(codeSingleService.updateCodeSingle(codeSingle));
    }

    /**
     * 删除生码记录
     */
    @PreAuthorize("@ss.hasPermi('code:single:remove')")
    @Log(title = "生码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(codeSingleService.deleteCodeSingleByIds(ids));
    }

    /**
     * 生码
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:single:add')")
    @Log(title = "生码记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CodeSingle codeSingle) {
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (codeSingle.getType().equals(AccConstants.GEN_CODE_TYPE_SINGLE)) {
            return toAjax(codeSingleService.createCodeSingle(companyId,codeSingle.getIsAcc(), codeSingle.getCount(), codeSingle.getRemark()));
        } else{
            return toAjax(codeSingleService.createAccCodeSingle(companyId, codeSingle.getIsAcc(),codeSingle.getCount(), codeSingle.getRemark()));
        }
    }

    /**
     * 码下载
     */
    @PreAuthorize("@ss.hasPermi('code:single:download')")
    @Log(title = "生码记录", businessType = BusinessType.EXPORT)
    @GetMapping("/download")
    public AjaxResult download(CodeSingle codeSingle) {
        List<Code> list = codeService.selectCodeListBySingle(SecurityUtils.getLoginUserTopCompanyId(), codeSingle.getId());
        for (Code code : list) {
            code.setCode(preFixUrl + code.getCode());
            if (code.getStatus() == AccConstants.CODE_STATUS_WAIT) {
                code.setStatusName("待赋值");
            } else if (code.getStatus() == AccConstants.CODE_STATUS_FINISH) {
                code.setStatusName("已赋值");
            }

            if (code.getCodeType().equals(AccConstants.CODE_TYPE_SINGLE)) {
                code.setCodeTypeName("单码");
                if(code.getpCode()!=null){
                    code.setpCode(preFixUrl + code.getpCode());
                }
            } else if (code.getCodeType().equals(AccConstants.CODE_TYPE_BOX)) {
                code.setCodeTypeName("箱码");
            }
        }
        ExcelUtil<Code> util = new ExcelUtil<Code>(Code.class);
        return util.exportExcel(list,"-"+DateUtils.getDate()+"码");
    }

    /**
     * 码下载TXT
     */
    @PreAuthorize("@ss.hasPermi('code:single:download')")
    @GetMapping("/downloadTxt")
    public AjaxResult downloadTxt(CodeSingle codeSingle, HttpServletResponse response) {
        List<Code> list = codeService.selectCodeListBySingle(SecurityUtils.getLoginUserTopCompanyId(), codeSingle.getId());
        String temp = "码" + "                                        " + "\r\n";
        for (Code code : list) {
            code.setCode(preFixUrl + code.getCode());
            if (code.getStatus() == AccConstants.CODE_STATUS_WAIT) {
                code.setStatusName("待赋值");
            } else if (code.getStatus() == AccConstants.CODE_STATUS_FINISH) {
                code.setStatusName("已赋值");
            }

            if (code.getCodeType().equals(AccConstants.CODE_TYPE_SINGLE)) {
                code.setCodeTypeName("单码");
                temp += "        " + code.getCode() + "\r\n";
            } else if (code.getCodeType().equals(AccConstants.CODE_TYPE_BOX)) {
                code.setCodeTypeName("箱码");
                temp += (code.getpCode() == null ? code.getCode() : code.getpCode()) + "\r\n";
            }
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", temp);
        return ajax;
    }

    /**
     * 生码赋值,按单赋值、分段赋值
     *
     * @return
     */
    @Log(title = "生码记录", businessType = BusinessType.OTHER)
    @PostMapping("/fuzhi")
    public AjaxResult fuzhi(@RequestBody FuzhiVo fuzhiVo) {
        int res = 0;
        Product product = productService.selectTProductById(fuzhiVo.getProductId());
        ProductBatch productBatch = productBatchService.selectProductBatchById(fuzhiVo.getBatchId());
        ProductCategory category1 = productCategoryService.selectProductCategoryById(product.getCategoryOne());
        ProductCategory category2 = productCategoryService.selectProductCategoryById(product.getCategoryTwo());
        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        Long companyId=SecurityUtils.getLoginUserTopCompanyId();
        String productCategory = category1.getCategoryName() + "-" + category2.getCategoryName();
        Date inputTime = new Date();
        CodeAttr codeAttr = new CodeAttr();
        codeAttr = new CodeAttr();
        codeAttr.setCompanyId(companyId);
        codeAttr.setTenantId(SecurityUtils.getLoginUserCompany().getDeptId());
        Long singleId=fuzhiVo.getRecordId();
        Long indexStart=fuzhiVo.getIndexStart();
        Long indexEnd=fuzhiVo.getIndexEnd();
        // 处理分段赋值逻辑
        if(singleId==0){
            //判断流水号区间是否已赋值
            //step1判断是否存在于两个生码记录
            Map<String,Object> map =new HashMap<>();
            map.put("companyId",companyId);
            map.put("indexBegin",indexStart);
            map.put("indexEnd",indexEnd);
            List<Code> list=codeService.selectCodeListByIndex(map);
            if(list.size()>0){
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).getSingleId()==null){
                        throw new CustomException("流水区间存在套标码，请重新输入流水区间！",HttpStatus.ERROR);
                    }
                    if(list.get(i).getCodeAttrId()!=null){
                        throw new CustomException("流水区间存在已赋值产品码，请重新输入流水区间！",HttpStatus.ERROR);
                    }
                    if(i==list.size()){
                        if(list.get(0).getSingleId()!=list.get(i).getSingleId()){
                            throw new CustomException("不允许跨生码区间赋值，请缩小赋值范围！",HttpStatus.ERROR);
                        }
                    }
                }
                singleId=list.get(0).getSingleId();//正确赋值singleId
            }else{
                throw new CustomException("未查询到相关码数据，请检查流水号区间是否正确！",HttpStatus.ERROR);
            }
        }
        codeAttr.setSingleId(singleId);
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
        //插入编码属性表
        Long codeAttrId = codeAttrService.insertCodeAttr(codeAttr);

        //更新对应码的状态
        res=codeService.updateStatusByIndex(companyId, codeAttrId,singleId,indexStart, indexEnd,AccConstants.CODE_STATUS_FINISH);

        CodeSingle codeSingle = new CodeSingle();
        codeSingle.setId(singleId);

        //计算是否全部赋值完成
        if(fuzhiVo.getRecordId()==0){//分段赋值
            CodeSingle codeSingle1=codeSingleService.selectCodeSingleById(singleId);
            long totalNum=codeSingle1.getIndexEnd()-codeSingle1.getIndexStart()+1;//统计生码记录总码量
            List<CodeAttr> codeSingle2=codeAttrService.selectCodeAttrBySingleId(singleId);
            long countNum=0;//统计已赋值记录总码量
            for(CodeAttr attr:codeSingle2){
                countNum+=(attr.getIndexEnd()-attr.getIndexStart()+1);
            }
            if(totalNum!=countNum){//判断两个码量是否相等
                codeSingle.setStatus(AccConstants.CODE_RECORD_STATUS_ON);
            }else {
                codeSingle.setStatus(AccConstants.CODE_RECORD_STATUS_EVA);
            }
        }else {
            codeSingle.setStatus(AccConstants.CODE_RECORD_STATUS_EVA);
        }
        //更新生码记录赋值信息
        res = codeSingleService.updateCodeSingle(codeSingle);
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
     * 普通生码，单码List装箱,每次校验码状态
     */
    @GetMapping("/checkPackageCode")
    @DataSource(DataSourceType.SHARDING)
    public AjaxResult checkPackageCode(@RequestParam("code") String codeStr,@RequestParam("flag") Boolean flag) {
        AjaxResult ajax = AjaxResult.success();
        codeStr=codeStr.trim();
        if(!codeStr.equals("")){
            Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();

            Code temp = new Code();
            temp.setCode(codeStr);
            temp.setCompanyId(companyId);
            Code code=codeService.selectCode(temp);//查询单码数据
            if(code==null){
                throw new CustomException("未查询到相关码数据！", HttpStatus.ERROR);
            }
            if(code.getpCode()!=null){
                throw new CustomException(code.getCode()+"该码已被扫描，请检查后重试！", HttpStatus.ERROR);
            }
            if(code.getCodeAttrId()!=null){
                throw new CustomException(code.getCode()+"该码已被赋值产品，请检查后重试！",HttpStatus.ERROR);
            }
            if(flag&&code.getCodeAcc()==null){
                throw new CustomException(code.getCode()+"该码未开启防伪码，请检查后重试！",HttpStatus.ERROR);
            }
            if(!flag&&code.getCodeAcc()!=null){
                throw new CustomException(code.getCode()+"该码已开启防伪码，请检查后重试！",HttpStatus.ERROR);
            }
            ajax.put("data", code);
            return ajax;
        }else{
            throw new CustomException("未接收到单码数据！", HttpStatus.ERROR);
        }

    }
    /**
     * 普通生码，单码List装箱
     */
    @PostMapping("/packageCode")
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult packageCode(@RequestBody Map<String,Object> map) {
        AjaxResult ajax = AjaxResult.success();

        List<String> list=(List)map.get("codes");
        if(list.size()>0){
            Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
            /**
             * 插入箱码，更新单码PCode
             */
            //最后一个码变成箱码
            String oldCode=list.get(list.size()-1);
            String pCode=oldCode.replaceFirst("3","2");

            Code temp = null;
            long singleId=0;

            for (int i = 0; i < list.size(); i++) {
                temp = new Code();
                temp.setCode(list.get(i));
                temp.setCompanyId(companyId);
                Code code=codeService.selectCode(temp);//查询单码数据
                if(code==null){
                    throw new CustomException("未查询到相关码数据！",HttpStatus.ERROR);
                }
                if(code.getCodeAttrId()!=null){
                    throw new CustomException("存在已赋值产品码，请重新扫码！",HttpStatus.ERROR);
                }
                if(code.getSingleId()!=null){
                    if(i > 0&singleId!=code.getSingleId()){
                        throw new CustomException("不允许跨生码区间装箱，请重新扫码！",HttpStatus.ERROR);
                    }
                    singleId=code.getSingleId();
                }
                if(code.getpCode()!=null){
                    throw new CustomException(code.getCode()+"该码已被扫描，请检查后重试！",HttpStatus.ERROR);
                }
                if(i<list.size()-1){//更新单码
                    codeService.updatePCodeByCode(companyId,pCode,list.get(i));
                }else{//最后一个码变成箱码
                    Code boxCode = new Code();
                    boxCode.setCodeIndex(code.getCodeIndex());
                    boxCode.setCompanyId(companyId);
                    boxCode.setCode(pCode);
                    boxCode.setCodeType(AccConstants.CODE_TYPE_BOX);
                    codeService.updateCode(boxCode);
                }
            }
            ajax.put("data", pCode);
            return ajax;
        }else{
            throw new CustomException("未接收到单码数据！",HttpStatus.ERROR);
        }

    }

    /**
     * 绑定产品信息
     * @param map
     * @return
     */
    @PostMapping("/bindCodeAttr")
    @DataSource(DataSourceType.SHARDING)
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult bindProductAttr(@RequestBody Map<String,Object> map) {
        if(map != null) {
            CodeAttr codeAttr = new CodeAttr();
            codeAttr.setCompanyId(Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
            codeAttr.setProductId(Long.valueOf(map.get("productId").toString()));
            codeAttr.setProductNo(map.get("productNo").toString());
            codeAttr.setProductName(map.get("productName").toString());
            codeAttr.setBatchId(Long.valueOf(map.get("batchId").toString()));
            codeAttr.setBatchNo(map.get("batchNo").toString());
            //插入编码属性表
            Long codeAttrId = codeAttrService.insertCodeAttr(codeAttr);
            //更新编码信息表
            Map<String, Object> params = new HashMap<>();
            params.put("pCode", map.get("pCode"));
            params.put("codeAttrId", Long.valueOf(codeAttrId));
            params.put("companyId", Long.valueOf(SecurityUtils.getLoginUserTopCompanyId()));
            codeService.updateCodeAttrIdByPCode(params);
            return AjaxResult.success();
        } else {
            throw new CustomException("产品信息为空！",HttpStatus.ERROR);
        }
    }

}
