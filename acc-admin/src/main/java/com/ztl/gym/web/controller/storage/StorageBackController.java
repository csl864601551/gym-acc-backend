package com.ztl.gym.web.controller.storage;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.storage.domain.StorageBack;
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.domain.vo.ProductBackVo;
import com.ztl.gym.storage.service.*;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 退货Controller
 *
 * @author ruoyi
 * @date 2021-04-19
 */
@RestController
@RequestMapping("/storage/back")
public class StorageBackController extends BaseController {
    @Autowired
    private IStorageService storageService;
    @Autowired
    private IStorageInService storageInService;
    @Autowired
    private IStorageBackService storageBackService;
    @Autowired
    private IStorageOutService storageOutService;
    @Autowired
    private ICodeService codeService;
    @Autowired
    private ICodeAttrService codeAttrService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IStorageTransferService storageTransferService;

    /**
     * 查询退货列表
     */
    @PreAuthorize("@ss.hasPermi('storage:back:list')")
    @GetMapping("/list")
    public TableDataInfo list(StorageBack storageBack) {
        startPage();
        List<StorageBack> list = storageBackService.selectStorageBackList(storageBack);
        return getDataTable(list);
    }

    /**
     * 导出退货列表
     */
    @PreAuthorize("@ss.hasPermi('storage:back:export')")
    @Log(title = "退货", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(StorageBack storageBack) {
        List<StorageBack> list = storageBackService.selectStorageBackList(storageBack);
        ExcelUtil<StorageBack> util = new ExcelUtil<StorageBack>(StorageBack.class);
        return util.exportExcel(list, "back");
    }

    /**
     * 获取退货详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:back:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        StorageBack storageBack = storageBackService.selectStorageBackById(id);
        return AjaxResult.success(storageBack);
    }

    /**
     * 查询退货码明细
     *
     * @param storageBack
     * @return
     */
    @PreAuthorize("@ss.hasPermi('storage:back:listCode')")
    @GetMapping("/listCode")
    public TableDataInfo listCode(StorageBack storageBack) {
        startPage();
        List<Code> codeList = commonService.selectCodeByStorage(SecurityUtils.getLoginUserTopCompanyId(), AccConstants.STORAGE_TYPE_BACK, storageBack.getId());
        for (Code code : codeList) {
            String typeName = "未知";
            if (code.getCode().startsWith("P")) {
                typeName = "箱码";
            } else {
                typeName = "单码";
            }
            code.setCodeTypeName(typeName);
        }
        return getDataTable(codeList);
    }


    /**
     * 退货录入-步骤1 【生成退货信息】
     */
    @PreAuthorize("@ss.hasPermi('storage:back:addFirst')")
    @PostMapping(value = "/addFirst")
    public AjaxResult addFirst(@RequestBody StorageBack storageBack) {
        //判断是否可以退货入库
        Code codeRes = judgeCodeCompanyCorrect(storageBack);

        //生成退货单号登记处信息
        String backNo = commonService.getStorageNo(AccConstants.STORAGE_TYPE_BACK);
        storageBack.setBackNo(backNo);
        storageBack.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        storageBack.setTenantId(codeRes.getCodeAttr().getTenantId());
        storageBack.setStorageFrom(codeRes.getCodeAttr().getTenantId());
        storageBack.setStorageTo(SecurityUtils.getLoginUserCompany().getDeptId());
        SysDept dept = deptService.selectDeptById(codeRes.getCodeAttr().getTenantId());
        storageBack.setStorageFromName(dept.getDeptName());
        storageBack.setCreateTime(new Date());

        StorageIn storageIn = storageInService.selectStorageInById(codeRes.getCodeAttr().getStorageRecordId());
        if (storageBack.getCodeStr().startsWith("P")) {
            storageBack.setCodeIndex(codeRes.getCodeAttr().getCodeRecord().getIndexStart() + "~" + codeRes.getCodeAttr().getCodeRecord().getIndexEnd());
            storageBack.setBackNum(storageIn.getActInNum());
            storageBack.setActBackNum(storageIn.getActInNum());
        } else {
            storageBack.setCodeIndex(String.valueOf(codeRes.getCodeIndex()));
            storageBack.setBackNum(1L);
            storageBack.setActBackNum(1L);
        }
        storageBack.setProductId(storageIn.getProductId());
        storageBack.setProductNo(storageIn.getProductNo());
        storageBack.setProductName(storageIn.getProductName());
        storageBack.setBatchNo(storageIn.getBatchNo());

        List<ProductBackVo> productList = new ArrayList<>();
        ProductBackVo vo = new ProductBackVo();
        vo.setCodeIndex(storageBack.getCodeIndex());
        vo.setProductNo(storageBack.getProductNo());
        vo.setProductName(storageBack.getProductName());
        vo.setBatchNo(storageBack.getBatchNo());
        vo.setBackNum(String.valueOf(storageBack.getBackNum()));
        productList.add(vo);
        storageBack.setProductBackVoList(productList);

        return AjaxResult.success(storageBack);
    }

    /**
     * 退货录入-步骤2 【保存退货信息】
     */
    @PreAuthorize("@ss.hasPermi('storage:back:add')")
    @Log(title = "退货", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StorageBack storageBack) {
        //判断是否可以退货入库
        judgeCodeCompanyCorrect(storageBack);
        //新增退货单
        return toAjax(storageBackService.insertStorageBack(storageBack));
    }

    /**
     * 判断是否可以退货入库
     */
    public Code judgeCodeCompanyCorrect(StorageBack storageBack) {
        //登录人企业id
        long companyId = 0;
        if (SecurityUtils.getLoginUserCompany().getDeptId() != AccConstants.ADMIN_DEPT_ID) {
            companyId = SecurityUtils.getLoginUserTopCompanyId();
        }
        //码所属企业id
        Long codeCompanyId = CodeRuleUtils.getCompanyIdByCode(storageBack.getCodeStr());

        //判断码
        if (codeCompanyId == null || codeCompanyId == 0) {
            throw new CustomException("码格式错误！", HttpStatus.ERROR);
        }

        //判断码企业是否一致
        if (companyId != codeCompanyId) {
            throw new CustomException("该码不属于当前用户企业！", HttpStatus.ERROR);
        }

        //查询码信息
        Code codeEntity = new Code();
        codeEntity.setCode(storageBack.getCodeStr());
        codeEntity.setCompanyId(CodeRuleUtils.getCompanyIdByCode(storageBack.getCodeStr()));
        Code codeRes = codeService.selectCode(codeEntity);

        //根据上一次流转信息获取产品物流数据
        if (codeRes.getCodeAttr().getStorageType() == null) {
            throw new CustomException("该货码尚未开始流转！");
        }
        //判断码当前所属是不是当前登录账户企业
        if (codeRes.getCodeAttr().getTenantId().equals(SecurityUtils.getLoginUserCompany().getDeptId())) {
            throw new CustomException("该码已属于当前登录账号");
        }
        //判断码当前是不是入库状态，如果是入库状态则无法退货入库
        if (codeRes.getCodeAttr().getStorageType() != AccConstants.STORAGE_TYPE_IN) {
            throw new CustomException("当前码状态不是入库状态，无法直接退货入库!");
        }
        return codeRes;
    }

    /**
     * 修改退货
     */
    @PreAuthorize("@ss.hasPermi('storage:back:edit')")
    @Log(title = "退货", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StorageBack storageBack) {
        return toAjax(storageBackService.updateStorageBack(storageBack));
    }

    /**
     * 删除退货
     */
    @PreAuthorize("@ss.hasPermi('storage:back:remove')")
    @Log(title = "退货", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(storageBackService.deleteStorageBackById(id));
    }
}
