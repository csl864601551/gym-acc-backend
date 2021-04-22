package com.ztl.gym.web.controller.storage;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.storage.domain.StorageBack;
import com.ztl.gym.storage.domain.StorageIn;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.domain.StorageTransfer;
import com.ztl.gym.storage.service.*;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
     * 退货录入（整箱退货）-步骤1 【生成退货单号】
     */
    @PreAuthorize("@ss.hasPermi('storage:back:addFirst')")
    @PostMapping(value = "/addFirst")
    public AjaxResult addFirst(@RequestBody StorageBack storageBack) {
        long companyId = 0;
        if(SecurityUtils.getLoginUserCompany().getDeptId() != AccConstants.ADMIN_DEPT_ID) {
            companyId = SecurityUtils.getLoginUserTopCompanyId();
        }
        //判断当前货码能否退货
        boolean res = commonService.judgeStorageIsIllegalByValue(companyId, AccConstants.STORAGE_TYPE_BACK, storageBack.getBackType(), storageBack.getValue());
        if (res) {
            //生成退货单号
            String backNo = commonService.getStorageNo(AccConstants.STORAGE_TYPE_BACK);
            storageBack.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
            storageBack.setTenantId(SecurityUtils.getLoginUser().getUser().getUserId());
            storageBack.setBackNo(backNo);
            storageBack.setStorageTo(SecurityUtils.getLoginUser().getUser().getUserId());
            storageBack.setCreateTime(new Date());
            if (storageBack.getBackType() == AccConstants.BACK_TYPE_COMMON) {
                //普通退货-经销商退给企业
                Code codeEntity = new Code();
                codeEntity.setCode(storageBack.getValue());
                codeEntity.setCompanyId(CodeRuleUtils.getCompanyIdByCode(storageBack.getValue()));
                Code codeRes = codeService.selectCode(codeEntity);
                SysDept dept = deptService.selectDeptById(codeRes.getCodeAttr().getTenantId());
                storageBack.setStorageFrom(dept.getDeptId());
                storageBack.setStorageFromName(dept.getDeptName());

                //根据上一次流转信息获取产品物流数据
                Long productId = null;
                String batchNo = null;
                Long actTransferNum = null;
                if (codeRes.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_IN) {
                    StorageIn storageIn = storageInService.selectStorageInById(codeRes.getCodeAttr().getStorageRecordId());
                    productId = storageIn.getProductId();
                    batchNo = storageIn.getBatchNo();
                    actTransferNum = storageIn.getActInNum();
                } else {
                    StorageOut storageOut = storageOutService.selectStorageOutById(codeRes.getCodeAttr().getStorageRecordId());
                    productId = storageOut.getProductId();
                    batchNo = storageOut.getBatchNo();
                    actTransferNum = storageOut.getActOutNum();
                }
                storageBack.setProductId(productId);
                storageBack.setBatchNo(batchNo);
                storageBack.setBackNum(actTransferNum);
                storageBack.setActBackNum(actTransferNum);
            } else {
                //调拨退货-企业退给企业根据调拨单号查询退货人信息
                StorageTransfer storageTransfer = storageTransferService.selectStorageTransferByNo(storageBack.getValue());
                SysDept dept = deptService.selectDeptById(storageTransfer.getStorageTo());

                storageBack.setProductId(storageTransfer.getProductId());
                storageBack.setBatchNo(storageTransfer.getBatchNo());
                storageBack.setBackNum(storageTransfer.getActTransferNum());
                storageBack.setActBackNum(storageTransfer.getActTransferNum());

                storageBack.setExtraNo(storageBack.getValue());
                storageBack.setStorageFrom(storageTransfer.getStorageTo());
                storageBack.setStorageFromName(dept.getDeptName());
            }
        }
        return AjaxResult.success(storageBack);
    }

    /**
     * 退货录入（整箱退货）-步骤2 【退货单更新仓库、备注信息】
     */
    @PreAuthorize("@ss.hasPermi('storage:back:add')")
    @Log(title = "退货", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StorageBack storageBack) {
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
