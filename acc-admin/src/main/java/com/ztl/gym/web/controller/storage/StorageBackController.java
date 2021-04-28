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
        return AjaxResult.success(storageBackService.selectStorageBackById(id));
    }

    /**
     * 退货录入-步骤1 【生成退货信息】
     */
    @PreAuthorize("@ss.hasPermi('storage:back:addFirst')")
    @PostMapping(value = "/addFirst")
    public AjaxResult addFirst(@RequestBody StorageBack storageBack) {
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

        //生成退货单号登记处信息
        String backNo = commonService.getStorageNo(AccConstants.STORAGE_TYPE_BACK);
        storageBack.setCompanyForceFlag(1);
        storageBack.setBackNo(backNo);
        storageBack.setCompanyId(companyId);
        storageBack.setStorageTo(SecurityUtils.getLoginUserCompany().getDeptId());
        storageBack.setCreateTime(new Date());

        //查询码属性信息
        Code codeEntity = new Code();
        codeEntity.setCode(storageBack.getCodeStr());
        codeEntity.setCompanyId(CodeRuleUtils.getCompanyIdByCode(storageBack.getCodeStr()));
        Code codeRes = codeService.selectCode(codeEntity);

        //根据上一次流转信息获取产品物流数据
        if (codeRes.getCodeAttr().getStorageType() == null) {
            throw new CustomException("该货码尚未开始流转！");
        }
        if (codeRes.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_IN) {
            storageBack.setTenantId(codeRes.getCodeAttr().getTenantId());
            storageBack.setStorageFrom(codeRes.getCodeAttr().getTenantId());
            SysDept dept = deptService.selectDeptById(codeRes.getCodeAttr().getTenantId());
            storageBack.setStorageFromName(dept.getDeptName());

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
        } else {
            throw new CustomException("当前码状态不是入库状态，无法直接退货入库!");
        }
        return AjaxResult.success(storageBack);
    }

    /**
     * 退货录入-步骤2 【保存退货信息】
     */
    @PreAuthorize("@ss.hasPermi('storage:back:add')")
    @Log(title = "退货", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StorageBack storageBack) {
        //新增退货单
        storageBackService.insertStorageBack(storageBack);

        //新增退货入库单 TODO
        StorageIn storageIn = new StorageIn();

        storageIn.setCompanyId(storageBack.getCompanyId());
        storageIn.setTenantId(storageBack.getTenantId());
        storageIn.setInType(StorageIn.IN_TYPE_BACK);
        storageIn.setExtraNo(storageBack.getBackNo());


        int res = storageInService.insertStorageInForBack(storageBack);
        //新增退货单
        return toAjax(storageBackService.insertStorageBack(storageBack));
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
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(storageBackService.deleteStorageBackByIds(ids));
    }
}
