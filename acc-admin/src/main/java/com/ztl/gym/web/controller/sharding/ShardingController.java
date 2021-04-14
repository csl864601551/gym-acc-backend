package com.ztl.gym.web.controller.sharding;

import com.alibaba.druid.pool.DruidDataSource;
import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.CodeTestService;
import com.ztl.gym.common.constant.AccConstants;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 分表测试
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/order")
public class ShardingController extends BaseController {
    @Autowired
    private DruidDataSource dataSource;
    @Autowired
    private ISysOrderService sysOrderService;
    @Autowired
    private CodeTestService codeService;

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

    @GetMapping("/query/test")
    public AjaxResult query1() {
        long id = 1;
        return AjaxResult.success(sysOrderService.selectSysOrder(id));
    }

    @GetMapping("/table/{orderId}")
    public AjaxResult addTable(@PathVariable("orderId") Long orderId) {
        int res = -1;
        try {
            String sql = "" +
                    "CREATE TABLE sys_order_" + orderId + " (\n" +
                    "  `order_id` bigint(20) NOT NULL COMMENT '订单ID',\n" +
                    "  `user_id` bigint(64) NOT NULL COMMENT '用户编号',\n" +
                    "  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0交易成功 1交易失败）',\n" +
                    "  `order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单流水',\n" +
                    "  PRIMARY KEY (`order_id`) USING BTREE\n" +
                    ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单信息表' ROW_FORMAT = Dynamic;" +
                    "";

            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            res = statement.executeUpdate(sql);
            System.out.println(res);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (res == 0) {
                return AjaxResult.success();
            }
            return AjaxResult.error();
        }
    }

    @GetMapping("/code/add/{code}")
    public AjaxResult add(@PathVariable("code") String codeValue) {
        // 获取当前的用户信息
//        LoginUser currentUser = SecurityUtils.getLoginUser();
        // 获取当前的用户企业
        long deptId =200;
        Code code = new Code();
        code.setCodeType(AccConstants.CODE_TYPE_SINGLE);
        code.setCode(codeValue);
        code.setStatus(AccConstants.STATUS_NORMAL);
        code.setCompanyId(deptId);
        return AjaxResult.success(codeService.insertCode(code));
    }

    @GetMapping("/listCode/{companyId}")
    public AjaxResult listCode(@PathVariable("companyId") long companyId) {
        return AjaxResult.success(codeService.selectCodeListByCompanyId(companyId));
    }

    @GetMapping("/code/get1/{code}")
    public AjaxResult getCode1(@PathVariable("code") String codeValue) {
//        // 获取当前的用户信息
//        LoginUser currentUser = SecurityUtils.getLoginUser();
//        // 获取当前的用户企业
//        long deptId = currentUser.getUser().getDeptId();
        long deptId = 100;
        return AjaxResult.success(codeService.selectCode(deptId, codeValue));
    }

    @GetMapping("/code/get2/{index}")
    public AjaxResult getCode2(@PathVariable("index") Long index) {
        long deptId = 200;
        return AjaxResult.success(codeService.selectCodeById(deptId, deptId));
    }
}
