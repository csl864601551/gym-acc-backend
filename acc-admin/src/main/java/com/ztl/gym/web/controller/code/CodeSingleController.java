package com.ztl.gym.web.controller.code;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.code.domain.CodeSingle;
import com.ztl.gym.code.domain.vo.CodeRecordDetailVo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return toAjax(codeSingleService.createCodeSingle(companyId, codeSingle.getCount(), codeSingle.getRemark()));

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
    public AjaxResult checkPackageCode(@RequestParam("code") String codeStr) {
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
            //获取并更新生码记录流水号
            String codeNoStr= CodeRuleUtils.getCodeIndex(companyId, 1, 0, CodeRuleUtils.CODE_PREFIX_B);
            String[] codeIndexs = codeNoStr.split("-");
            long codeIndex =Long.parseLong(codeIndexs[0]) + 1;

            String pCode=CodeRuleUtils.buildCode(companyId,CodeRuleUtils.CODE_PREFIX_B,codeIndex);

            Code temp = null;
            long codeAttrId=0;
            long singleId=0;

            for (int i = 0; i < list.size(); i++) {
                temp = new Code();
                temp.setCode(list.get(i));
                temp.setCompanyId(companyId);
                Code code=codeService.selectCode(temp);//查询单码数据
                if(code==null){
                    throw new CustomException("未查询到相关码数据！");
                }
                if(code.getCodeAttrId()!=null){
                    codeAttrId=code.getCodeAttrId();
                }
                if(code.getSingleId()!=null){
                    singleId=code.getSingleId();
                }
                if(code.getpCode()!=null){
                    throw new CustomException(code.getCode()+"该码已被扫描，请检查后重试！");
                }
                codeService.updatePCodeByCode(companyId,pCode,list.get(i));
            }//更新单码


            Code boxCode = new Code();
            boxCode.setCodeIndex(codeIndex);
            boxCode.setCompanyId(companyId);
            boxCode.setCodeType(AccConstants.CODE_TYPE_BOX);
            boxCode.setCode(pCode);
            boxCode.setCodeAttrId(codeAttrId);
            boxCode.setSingleId(singleId);
            codeService.insertCode(boxCode);//插入箱码


            commonService.updateVal(companyId, codeIndex);//更新code_index
            Map<String,Object> mapTemp = new HashMap<>();
            mapTemp.put("companyId",companyId);
            mapTemp.put("boxCode",pCode);
            mapTemp.put("codeIndex",codeIndex);
            commonService.insertPrintData(mapTemp);//插入打印数据

            ajax.put("data", pCode);
            return ajax;
        }else{
            throw new CustomException("未接收到单码数据！");
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
            throw new CustomException("产品信息为空！");
        }
    }
}
