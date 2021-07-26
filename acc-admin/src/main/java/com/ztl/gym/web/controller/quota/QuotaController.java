package com.ztl.gym.web.controller.quota;

import java.util.List;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.quota.domain.Quota;
import com.ztl.gym.quota.service.IQuotaService;
import com.ztl.gym.web.controller.payment.PaymentRecordController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 配额 Controller
 *
 * @author wujinhao
 * @date 2021-07-19
 */
@RestController
@RequestMapping("/quota")
public class QuotaController extends BaseController
{
    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(QuotaController.class);
    @Autowired
    private IQuotaService quotaService;

    /**
     * 根据paramkey获取配额 详细信息
     */
    @GetMapping(value = "/{key}")
    public AjaxResult getInfoByKey(@PathVariable("key") String key) {
        logger.info("the method getInfoByKey enter, param is {}", key);
        Quota quota = quotaService.selectQuotaByParamKey(key);
        logger.info("the method getInfoByKey end, result is {}", quota);
        return AjaxResult.success(quota);
    }

//    /**
//     * 查询配额 列表
//     */
//    @GetMapping("/list")
//    public TableDataInfo list(Quota quota)
//    {
//        startPage();
//        List<Quota> list = quotaService.selectQuotaList(quota);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出配额 列表
//     */
//    @PreAuthorize("@ss.hasPermi('product:quota:export')")
//    @Log(title = "配额 ", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(Quota quota)
//    {
//        List<Quota> list = quotaService.selectQuotaList(quota);
//        ExcelUtil<Quota> util = new ExcelUtil<Quota>(Quota.class);
//        return util.exportExcel(list, "quota");
//    }
//

//    /**
//     * 获取配额 详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('product:quota:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id) {
//        return AjaxResult.success(quotaService.selectQuotaById(id));
//    }
//
//    /**
//     * 新增配额
//     */
//    @PreAuthorize("@ss.hasPermi('product:quota:add')")
//    @Log(title = "配额 ", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody Quota quota)
//    {
//        return toAjax(quotaService.insertQuota(quota));
//    }
//
//    /**
//     * 修改配额
//     */
//    @PreAuthorize("@ss.hasPermi('product:quota:edit')")
//    @Log(title = "配额 ", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody Quota quota)
//    {
//        return toAjax(quotaService.updateQuota(quota));
//    }
//
//    /**
//     * 删除配额
//     */
//    @PreAuthorize("@ss.hasPermi('product:quota:remove')")
//    @Log(title = "配额 ", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(quotaService.deleteQuotaByIds(ids));
//    }
}
