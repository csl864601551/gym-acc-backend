package com.ztl.gym.weixin.controller;

import com.ztl.gym.weixin.common.AjaxJson;
import com.ztl.gym.weixin.service.WxService;
import com.ztl.gym.weixin.utils.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@RestController
@RequestMapping("/weixin")
public class WxInitController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WxService wxService;

    /**
     *@Author: shenz
     *@param:  shareUrl 分享url地址
     *@Description: 初始化微信JSSDK Config信息
    1.先通过appId和appSecret参数请求指定微信地址 获取AccessToken
    2.在通过第一步中的AccessToken作为参数请求微信地址 获取jsapi_ticket临时票据(此处不考虑调用频率，使用者根据情况放入缓存或定时任务)
    3.通过第二步的JssdkGetticket和timestamp,nonceStr,url作为参数请求微信地址，获取签名signature
    4.将第三步获得的signature和jsapi_ticket,nonceStr,timestamp,url返回给前端，作为Config初始化验证的信息
     */
    @RequestMapping("/initWXJSSDKConfigInfo")
    public AjaxJson initWXJSConfig (@RequestParam(required = false) String url) throws Exception{
        logger.info("url=" + url);
        String json = "";
        try {
            Map map = wxService.initJSSDKConfig(url);
            json = WxUtil.mapToJson(map);
        }catch (Exception e){
            AjaxJson.fail(e.getMessage());
        }
        return AjaxJson.ok(json);
    }

    /**
     * 用于微信验证
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "getwx", method = {RequestMethod.GET,RequestMethod.POST})
    public void getwx(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。 timestamp
         * 时间戳 nonce 随机数 echostr 随机字符串
         */
        System.out.println("this is getwx");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        PrintWriter out = null;
        //接入验证
        if (wxService.check(timestamp, nonce, signature)) {
            System.out.println("接入成功");
            out = response.getWriter();
            // 原样返回echostr参数
            out.print(echostr);
            out.flush();
            out.close();
            return;
        } else {
            System.out.println("接入失败");
        }
    }
}
