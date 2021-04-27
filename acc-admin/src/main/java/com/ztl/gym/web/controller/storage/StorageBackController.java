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
     * 退货录入-步骤1 【生成退货信息】
     */
    @PreAuthorize("@ss.hasPermi('storage:back:addFirst')")
    @PostMapping(value = "/addFirst")
    public AjaxResult addFirst(@RequestBody StorageBack storageBack) {
        long companyId = 0;
        long currentUserDeptId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (SecurityUtils.getLoginUserCompany().getDeptId() != AccConstants.ADMIN_DEPT_ID) {
            companyId = SecurityUtils.getLoginUserTopCompanyId();
        }

        //生成退货单号登记处信息
        String backNo = commonService.getStorageNo(AccConstants.STORAGE_TYPE_BACK);
        storageBack.setBackNo(backNo);
        storageBack.setCompanyId(companyId);
        storageBack.setTenantId(currentUserDeptId);
        storageBack.setStorageTo(SecurityUtils.getLoginUser().getUser().getUserId());
        storageBack.setCreateTime(new Date());
        if (storageBack.getBackType() == StorageBack.BACK_TYPE_COMMON) {
            //普通退货-判断码
            //查询码属性信息
            Code codeEntity = new Code();
            codeEntity.setCode(storageBack.getValue());
            codeEntity.setCompanyId(CodeRuleUtils.getCompanyIdByCode(storageBack.getValue()));
            Code codeRes = codeService.selectCode(codeEntity);

            SysDept dept = deptService.selectDeptById(codeRes.getCodeAttr().getTenantId());
            storageBack.setStorageFrom(dept.getDeptId());
            storageBack.setStorageFromName(dept.getDeptName());

            //根据上一次流转信息获取产品物流数据
            Long productId = null;
            String productName = null;
            String batchNo = null;
            Long backNum = null;
            if (codeRes.getCodeAttr().getStorageType() == AccConstants.STORAGE_TYPE_IN) {
                SysDept sysDept = new SysDept();
                sysDept.setParentId(currentUserDeptId);
                List<SysDept> son = deptService.selectDeptList(sysDept);

                //判断码是不是当前登录用户的下级所有
                boolean flag = false;
                for (SysDept childDept : son) {
                    if (childDept.getDeptId() == codeRes.getCodeAttr().getTenantId()) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    StorageIn storageIn = storageInService.selectStorageInById(codeRes.getCodeAttr().getStorageRecordId());
                    productId = storageIn.getProductId();
                    productName = storageIn.getProductName();
                    batchNo = storageIn.getBatchNo();
                    backNum = storageIn.getActInNum();
                    storageBack.setCompanyForceFlag(1);
                } else {
                    throw new CustomException("当前码状态所属企业经销商不是当前登录用户的下级!");
                }
            } else {
                throw new CustomException("当前码状态不是入库状态，企业无法直接退货入库!");
            }
            storageBack.setProductName(productName);
            storageBack.setProductId(productId);
            storageBack.setBatchNo(batchNo);
            storageBack.setBackNum(backNum);
            storageBack.setActBackNum(backNum);
        } else {
            //调拨退货-判断调拨单
            //有流转单说明是调拨退货操作
            StorageTransfer storageTransfer = storageTransferService.selectStorageTransferByNo(storageBack.getValue());
            if (storageTransfer == null) {
                throw new CustomException("该调拨单号查找不到！", HttpStatus.ERROR);
            }

            //判断调拨入库方是否是当前用户
            if (storageTransfer.getStorageTo() != currentUserDeptId) {
                throw new CustomException("调拨接收方不属于当前登录企业！", HttpStatus.ERROR);
            }
            storageBack.setProductId(storageTransfer.getProductId());
            storageBack.setProductName(storageTransfer.getProductName());
            storageBack.setBatchNo(storageTransfer.getBatchNo());
            storageBack.setBackNum(storageTransfer.getActTransferNum());
            storageBack.setActBackNum(storageTransfer.getActTransferNum());
            storageBack.setExtraNo(storageBack.getValue());
            storageBack.setStorageFrom(storageTransfer.getStorageTo());

            SysDept dept = deptService.selectDeptById(storageTransfer.getStorageTo());
            storageBack.setStorageFromName(dept.getDeptName());
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
