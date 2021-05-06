package com.ztl.gym.web.controller.storage;

import java.util.List;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.storage.domain.StorageBack;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.storage.domain.Storage;
import com.ztl.gym.storage.service.IStorageService;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 仓库Controller
 *
 * @author zhucl
 * @date 2021-04-13
 */
@RestController
@RequestMapping("/storage/storage")
public class StorageController extends BaseController {
    @Autowired
    private IStorageService storageService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ICodeService codeService;

    /**
     * 查询仓库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:list')")
    @GetMapping("/list")
    public TableDataInfo list(Storage storage) {
        startPage();
        List<Storage> list = storageService.selectStorageList(storage);
        return getDataTable(list);
    }

    /**
     * 导出仓库列表
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:export')")
    @Log(title = "仓库", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Storage storage) {
        List<Storage> list = storageService.selectStorageList(storage);
        ExcelUtil<Storage> util = new ExcelUtil<Storage>(Storage.class);
        return util.exportExcel(list, "storage");
    }

    /**
     * 获取仓库详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(storageService.selectStorageById(id));
    }

    /**
     * 新增仓库
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:add')")
    @Log(title = "仓库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Storage storage) {
        return toAjax(storageService.insertStorage(storage));
    }

    /**
     * 修改仓库
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:edit')")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Storage storage) {
        return toAjax(storageService.updateStorage(storage));
    }

    /**
     * 删除仓库
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:remove')")
    @Log(title = "仓库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(storageService.deleteStorageByIds(ids));
    }

    /**
     * 获取用户仓库信息
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:listByUser')")
    @GetMapping(value = "/listStorageByUser")
    public AjaxResult getUserStotageList() {
        Storage storage = new Storage();
        storage.setStatus(0L);
        if (!SecurityUtils.getLoginUserCompany().getDeptId().equals(AccConstants.ADMIN_DEPT_ID)) {
            //判断用户是企业还是经销商
            String[] ancestors = SecurityUtils.getLoginUserCompany().getAncestors().split(",");
            if (ancestors.length > 2) {
                storage.setLevel(AccConstants.STORAGE_LEVEL_TENANT);
                storage.setTenantId(SecurityUtils.getLoginUserCompany().getDeptId());
            } else {
                storage.setLevel(AccConstants.STORAGE_LEVEL_COMPANY);
                storage.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
            }
        }
        List<Storage> list = storageService.selectStorageByUser(storage);
        return AjaxResult.success(list);
    }

    /**
     * 根据tenant获取用户仓库信息
     */
    @PreAuthorize("@ss.hasPermi('storage:storage:listByTenant')")
    @GetMapping(value = "/getStoragesByTenant/{tenantId}")
    public AjaxResult getStoragesByTenant(@PathVariable("tenantId") Long tenantId) {
        Storage storage = new Storage();
        storage.setStatus(0L);
        if (tenantId != null && tenantId > 0) {
            storage.setTenantId(tenantId);
        }
        List<Storage> list = storageService.selectStorageByUser(storage);
        return AjaxResult.success(list);
    }

    /**
     * 根据码号查询相关产品和码信息
     */
    @GetMapping(value = "/getRecordByCode")
    public AjaxResult getRecordByCode(@RequestParam("storageType") Integer storageType, @RequestParam("code") String code) {
        long companyId = 0;
        if (SecurityUtils.getLoginUserCompany().getDeptId() != AccConstants.ADMIN_DEPT_ID) {
            companyId = SecurityUtils.getLoginUserTopCompanyId();
        }
        if (commonService.judgeStorageIsIllegalByValue(companyId, storageType, code)) {

            return AjaxResult.success(storageService.selectLastStorageByCode(code));
        }
        return AjaxResult.error();

    }

    /**
     * 查询流转单的码明细列表
     *
     * @param storageType
     * @param storageRecordId
     * @return
     */
    @GetMapping("/listCode")
    public TableDataInfo listCode(int storageType, long storageRecordId) {
        Code codeParam = commonService.selectCodeByStorageForPage(SecurityUtils.getLoginUserTopCompanyId(), storageType, storageRecordId);
        startPage();
        List<Code> codeList = codeService.selectCodeList(codeParam);
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
}
