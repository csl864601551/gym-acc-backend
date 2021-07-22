package com.ztl.gym.web.controller.payment;

import java.util.List;

import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.payment.domain.PriceConfig;
import com.ztl.gym.payment.service.IPriceConfigService;
import com.ztl.gym.payment.service.impl.PaymentRecordServiceImpl;
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
 * 价格 平台设置码包价格Controller
 *
 * @author wujinhao
 * @date 2021-07-19
 */
@RestController
@RequestMapping("/payment/priceConfig")
public class PriceConfigController extends BaseController
{
    /**
     * 定义日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(PriceConfigController.class);
    @Autowired
    private IPriceConfigService priceConfigService;

    /**
     * 查询价格 平台设置码包价格列表
     */
    @PreAuthorize("@ss.hasPermi('payment:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(PriceConfig priceConfig)
    {
        logger.info("the method list enter,query priceConfigList param is {}", priceConfig);
        startPage();
        List<PriceConfig> list = priceConfigService.selectPriceConfigList(priceConfig);
        return getDataTable(list);
    }



    /**
     * 获取价格 平台设置码包价格详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        logger.info("the method getInfo enter,query priceConfig param is {}", id);
        return AjaxResult.success(priceConfigService.selectPriceConfigById(id));
    }

    /**
     * 新增价格 平台设置码包价格
     */
    @PreAuthorize("@ss.hasPermi('payment:config:add')")
    @Log(title = "价格 平台设置码包价格", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PriceConfig priceConfig)
    {
        logger.info("the method add enter,add priceConfig param is {}", priceConfig);
        return toAjax(priceConfigService.insertPriceConfig(priceConfig));
    }

    /**
     * 修改价格 平台设置码包价格
     */
    @PreAuthorize("@ss.hasPermi('payment:config:edit')")
    @Log(title = "价格 平台设置码包价格", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PriceConfig priceConfig)
    {
        logger.info("the method edit enter,edit priceConfig param is {}", priceConfig);
        return toAjax(priceConfigService.updatePriceConfig(priceConfig));
    }

    /**
     * 删除价格 平台设置码包价格
     */
    @PreAuthorize("@ss.hasPermi('payment:config:remove')")
    @Log(title = "价格 平台设置码包价格", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        logger.info("the method remove enter,remove priceConfig param is {}", ids);
        return toAjax(priceConfigService.deletePriceConfigByIds(ids));
    }
}
