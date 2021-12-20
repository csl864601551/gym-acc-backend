package com.ztl.gym.web.controller.open.code;

import com.ztl.gym.code.domain.SecurityCodeRecord;
import com.ztl.gym.code.domain.vo.ScanSecurityCodeOutBean;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.code.service.ISecurityCodeRecordService;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.framework.web.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/openApi/v1")
public class AntiValidateController {

    @Autowired
    private ICodeService codeService;
    @Autowired
    private ISecurityCodeRecordService securityCodeRecordService;
    @Autowired
    private SysLoginService loginService;

    /**
     * 通过accCodecheck防伪码
     *
     * @return 通过accCodecheck防伪码
     */
    @PostMapping("/antiValidate")
    public AjaxResult antiValidate(@RequestBody Map<String, Object> map) {
        if (map.get("loginName") == "" || map.get("loginName") == null || map.get("loginPwd") == "" || map.get("loginPwd") == null) {
            throw new CustomException("缺少账号密码参数！", HttpStatus.ERROR);
        }
        if (map.get("idisCode") == "" || map.get("idisCode") == null) {
            throw new CustomException("缺少标识码参数！", HttpStatus.ERROR);
        }
        if (map.get("checkCode") == "" || map.get("checkCode") == null) {
            throw new CustomException("缺少防伪码参数！", HttpStatus.ERROR);
        }
        //校验用户名密码
        loginService.login(map.get("loginName").toString(), map.get("loginPwd").toString(), "","","1");

        SecurityCodeRecord securityCodeRecord = new SecurityCodeRecord();
        securityCodeRecord.setCodeAcc(map.get("checkCode").toString());
        securityCodeRecord.setCode(map.get("idisCode").toString());
        securityCodeRecord.setLatitude(map.get("latitude")==null?new BigDecimal(0):new BigDecimal(map.get("latitude").toString()));
        securityCodeRecord.setLongitude(map.get("longitude")==null?new BigDecimal(0):new BigDecimal(map.get("longitude").toString()));
        securityCodeRecord.setAddress(map.get("accessAddress")==null?"":map.get("accessAddress").toString());
        securityCodeRecord.setIp(map.get("ip")==null?"":map.get("ip").toString());
        securityCodeRecord.setProvince(map.get("provinceName")==null?"":map.get("provinceName").toString());
        securityCodeRecord.setCity(map.get("cityName")==null?"":map.get("cityName").toString());
        securityCodeRecord.setDistrict(map.get("districtName")==null?"":map.get("districtName").toString());
        //不从登录信息获取企业id，根据防伪码解析
        securityCodeRecord.setCompanyId(CodeRuleUtils.getCompanyIdBySecurityCode(securityCodeRecord.getCodeAcc()));
        ScanSecurityCodeOutBean bean = securityCodeRecordService.getSecurityCodeInfo(securityCodeRecord);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String firstTime = "";
        String lastTime = "";
        String msg ="您好，你所查询的标识(88.118.8/" + map.get("idisCode").toString() + ")对应防伪码(" + map.get("checkCode").toString() + ")";
        if (bean.getCount() != 0) {
            firstTime = formatter.format(bean.getFirstQueryTime());
            lastTime = formatter.format(bean.getLastQueryTime());
            msg = msg+"已被查询过" + bean.getCount() + "次，首次查询时间为：" + firstTime + "，如果您不是第一次查询，请谨防假冒！";
        } else {
            msg = msg+"是正品，谢谢您的查询。";
        }


        Map<String, Object> res = new HashMap<>();
        res.put("firstQueryTime", firstTime);
        res.put("lastQueryTime", lastTime);
        res.put("success", true);
        res.put("times", bean.getCount());
        res.put("tipMsg", msg);
        return AjaxResult.success(res);

    }

}
