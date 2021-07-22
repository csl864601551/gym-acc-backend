package com.ztl.gym.web.controller.payment;

import java.util.List;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.payment.domain.PurchaseRecord;
import com.ztl.gym.payment.service.IPurchaseRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.utils.poi.ExcelUtil;
import com.ztl.gym.common.core.page.TableDataInfo;

/**
 * 消费记录 Controller
 *
 * @author wujinhao
 * @date 2021-07-19
 */
@RestController
@RequestMapping("/payment/purchaseRecord")
public class PurchaseRecordController extends BaseController
{
    @Autowired
    private IPurchaseRecordService purchaseRecordService;

    /**
     * 查询消费记录 列表
     */
    @PreAuthorize("@ss.hasPermi('payment:purchase:list')")
    @GetMapping("/list")
    public TableDataInfo list(PurchaseRecord purchaseRecord)
    {
        logger.info("the method list enter,query purchaseRecordList param is {}", purchaseRecord);
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            purchaseRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }else{
            purchaseRecord.setCompanyId(companyId);
        }
        startPage();
        List<PurchaseRecord> list = purchaseRecordService.selectPurchaseRecordList(purchaseRecord);
        return getDataTable(list);
    }

    /**
     * 导出消费记录 列表
     */
//    @PreAuthorize("@ss.hasPermi('product:record:export')")
//    @Log(title = "消费记录 ", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(PurchaseRecord purchaseRecord)
//    {
//        List<PurchaseRecord> list = purchaseRecordService.selectPurchaseRecordList(purchaseRecord);
//        ExcelUtil<PurchaseRecord> util = new ExcelUtil<PurchaseRecord>(PurchaseRecord.class);
//        return util.exportExcel(list, "record");
//    }

    /**
     * 获取消费记录 详细信息
     */
//    @PreAuthorize("@ss.hasPermi('product:record:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return AjaxResult.success(purchaseRecordService.selectPurchaseRecordById(id));
//    }

    /**
     * 新增消费记录 
     */
    @PreAuthorize("@ss.hasPermi('payment:purchase:add')")
    @Log(title = "消费记录 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PurchaseRecord purchaseRecord) {
        logger.info("the method add enter,add purchaseRecord param is {}", purchaseRecord);
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            purchaseRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }else{
            purchaseRecord.setCompanyId(companyId);
        }
        long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        purchaseRecord.setCreateUser(userId);
        purchaseRecord.setUpdateUser(userId);
        return toAjax(purchaseRecordService.insertPurchaseRecord(purchaseRecord));
    }

    /**
     * 修改消费记录 
     */
//    @PreAuthorize("@ss.hasPermi('product:record:edit')")
//    @Log(title = "消费记录 ", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody PurchaseRecord purchaseRecord)
//    {
//        return toAjax(purchaseRecordService.updatePurchaseRecord(purchaseRecord));
//    }

    /**
     * 删除消费记录 
     */
//    @PreAuthorize("@ss.hasPermi('product:record:remove')")
//    @Log(title = "消费记录 ", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(purchaseRecordService.deletePurchaseRecordByIds(ids));
//    }
}
