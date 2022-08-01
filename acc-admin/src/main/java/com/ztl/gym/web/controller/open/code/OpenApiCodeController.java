package com.ztl.gym.web.controller.open.code;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeAttr;
import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.code.domain.vo.FuzhiVo;
import com.ztl.gym.code.mapper.CodeAttrMapper;
import com.ztl.gym.code.mapper.CodeMapper;
import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.code.service.impl.CodeRecordServiceImpl;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.domain.model.UserInfo;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.framework.web.service.SysLoginService;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.domain.ProductBatch;
import com.ztl.gym.product.service.IProductBatchService;
import com.ztl.gym.product.service.IProductService;
import com.ztl.gym.storage.domain.Storage;
import com.ztl.gym.storage.domain.StorageOut;
import com.ztl.gym.storage.service.IStorageInService;
import com.ztl.gym.storage.service.IStorageOutService;
import com.ztl.gym.storage.service.IStorageService;
import com.ztl.gym.system.service.ISysDeptService;
import com.ztl.gym.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/openApi/v1/code")
@Slf4j
public class OpenApiCodeController {

    @Autowired
    private ICodeService codeService;
    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private CodeAttrMapper codeAttrMapper;
    @Autowired
    private ICodeRecordService codeRecordService;
    @Autowired
    private IProductService productService;

    @Autowired
    private IProductBatchService productBatchService;

    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IStorageService storageService;
    @Autowired
    private IStorageInService storageInService;
    @Autowired
    private IStorageOutService storageOutService;
    @Autowired
    private SysLoginService loginService;


    @PostMapping("/accListAll")
    @Transactional
    @Log(title = "MOM同步数据", businessType = BusinessType.INSERT)
    public AjaxResult getCodes(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        UserInfo userInfo = new UserInfo();
        try {
            userInfo = JSONObject.parseObject(JSONObject.toJSONString(map.get("userInfo")), UserInfo.class);
        } catch (Exception e) {
            throw new CustomException("获取用户参数异常！", HttpStatus.ERROR);
        }
        //处理用户认证登录
        String token = "";
        try {
            token = loginService.dealLoginToken(userInfo, request);
        } catch (Exception e) {
            throw new CustomException("同步用户认证登录失败！", HttpStatus.ERROR);
        }
        //token 换userinfo获取用户信息
        Long tenantId = 0L;
        try {
            //登录用户初始数据
            tenantId = Long.valueOf(SecurityUtils.getLoginUserTopCompanyId());
        } catch (Exception e) {
            throw new CustomException("获取用户登录信息失败！", HttpStatus.ERROR);
        }


        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        Long userId = sysUser.getUserId();
        SysDept sysDept = SecurityUtils.getLoginUserCompany();



        return AjaxResult.success("请求成功", "同步数据成功");
    }


}
