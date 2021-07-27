package com.ztl.gym.web.controller.payment;

import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.payment.domain.PaymentRecord;
import com.ztl.gym.payment.service.IPaymentRecordService;
import com.ztl.gym.quota.service.IQuotaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 充值记录 Controller
 *
 * @author wujinhao
 * @date 2021-07-19
 */
@RestController
@RequestMapping("/payment/record")
public class PaymentRecordController extends BaseController
{
    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(PaymentRecordController.class);
    @Autowired
    private IPaymentRecordService paymentRecordService;
    @Autowired
    private IQuotaService quotaService;

    /**
     * 查询充值记录 列表
     */
    @PreAuthorize("@ss.hasPermi('payment:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaymentRecord paymentRecord)
    {
        logger.info("the method list enter,query paymentRecordList param is {}", paymentRecord);
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            paymentRecord.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
        }
        startPage();
        List<PaymentRecord> list = paymentRecordService.selectPaymentRecordList(paymentRecord);
        return getDataTable(list);
    }

    /**
     * 获取充值记录 详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(paymentRecordService.selectPaymentRecordById(id));
    }

    /**
     * 新增充值记录 
     */
    @PreAuthorize("@ss.hasPermi('payment:record:add')")
    @Log(title = "充值记录 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Validated PaymentRecord paymentRecord)
    {
        logger.info("the method add enter,add paymentRecord param is {}", paymentRecord);
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        if (!companyId.equals(AccConstants.ADMIN_DEPT_ID)) {
            throw new CustomException("企业或经销商没权限充值.", HttpStatus.ERROR);
        }
        long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        paymentRecord.setCreateUser(userId);
        paymentRecord.setUpdateUser(userId);
        return toAjax(paymentRecordService.insertPaymentRecord(paymentRecord));
    }

    /**
     * 获取统计数值
     * 获取充值总数和可用金额总数
     */
    @GetMapping("/totalAmount")
    public AjaxResult getTotalAmount(PaymentRecord paymentRecord) {
        logger.info("the method getTotalAmount enter,param is {}", paymentRecord);
        Map<String, Object> result = paymentRecordService.getStatistics(paymentRecord);
        logger.info("the method getTotalAmount end,result is {}", result);
        return AjaxResult.success(result);
    }

//    /**
//     * 修改充值记录
//     */
//    @PreAuthorize("@ss.hasPermi('product:record:edit')")
//    @Log(title = "充值记录 ", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody PaymentRecord paymentRecord)
//    {
//        return toAjax(paymentRecordService.updatePaymentRecord(paymentRecord));
//    }
//
//    /**
//     * 删除充值记录
//     */
//    @PreAuthorize("@ss.hasPermi('product:record:remove')")
//    @Log(title = "充值记录 ", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(paymentRecordService.deletePaymentRecordByIds(ids));
//    }



}
