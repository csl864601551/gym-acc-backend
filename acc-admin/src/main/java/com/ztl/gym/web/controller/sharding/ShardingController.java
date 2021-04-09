package com.ztl.gym.web.controller.sharding;

import com.ztl.gym.common.core.controller.BaseController;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.utils.uuid.IdUtils;
import com.ztl.gym.sharding.domain.SysOrder;
import com.ztl.gym.sharding.service.ISysOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分表测试
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/order")
public class ShardingController extends BaseController {
    @Autowired
    private ISysOrderService sysOrderService;

    @GetMapping("/add/{userId}")
    public AjaxResult add(@PathVariable("userId") Long userId) {
        SysOrder sysOrder = new SysOrder();
        sysOrder.setUserId(userId);
        sysOrder.setStatus("0");
        sysOrder.setOrderNo(IdUtils.fastSimpleUUID());
        return AjaxResult.success(sysOrderService.insertSysOrder(sysOrder));
    }

    @GetMapping("/list")
    public AjaxResult list(SysOrder sysOrder) {
        return AjaxResult.success(sysOrderService.selectSysOrderList(sysOrder));
    }

    @GetMapping("/query/{orderId}")
    public AjaxResult query(@PathVariable("orderId") Long orderId) {
        return AjaxResult.success(sysOrderService.selectSysOrderById(orderId));
    }
}
