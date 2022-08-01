package com.ztl.gym.web.controller.open.sso;

import com.alibaba.fastjson.JSONObject;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.model.AccessTokenRequest;
import com.ztl.gym.common.core.domain.model.AccessTokenResponse;
import com.ztl.gym.common.core.domain.model.UserInfo;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.framework.web.service.SysLoginService;
import com.ztl.gym.framework.web.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/oauth2")
public class SsoController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private OkHttpClient okHttpClient;
    @Autowired
    private SysLoginService loginService;
    /**
     * 授权地址
     */
    private final static String URL_AUTH = "https://sso.asun.cloud/oauth2/auth";
    /**
     * 获取token
     */
    private final static String URL_GET_TOKEN = "https://sso.asun.cloud/oauth2/token";
    /**
     * 获取用户信息
     */
    private final static String URL_GET_USER = "https://sso.asun.cloud/userinfo";

    /**
     * 应用clientId
     */
    private final static String clientId = "0e8745a3-56b3-4903-ad66-a15cc302c04d";
    /**
     * 应用secret
     */
    private final static String clientSecret = "6695f0dbde4c5eb865f645d841c54a10";
    /**
     * 授权模式
     */
    private final static String grantType = "authorization_code";
    /**
     * 回调地址
     */
    private final static String redirectUri = "https://fc.asun.cloud/index";

    /**
     * 跳转到自动登录页面
     *
     * @param session
     * @return
     */
    @RequestMapping("/auto")
    public ModelAndView auto(HttpSession session, String account) {
        ModelAndView modelAndView = new ModelAndView("/open/auto.html");
        modelAndView.addObject("username", account);
        modelAndView.addObject("password", "ztt12345");
        return modelAndView;
    }

    /**
     * 跳转到异常页面
     *
     * @return
     */
    @RequestMapping("/error")
    public ModelAndView error(String msg) {
        ModelAndView modelAndView = new ModelAndView("/open/error.html");
        if (StringUtils.isBlank(msg)) {
            msg = "啊哦，你访问的应用授权出现异常，请联系管理员处理";
        }
        modelAndView.addObject("msg", msg);
        return modelAndView;
    }

    /**
     * 服务平台授权登录
     *
     * @param response
     */
    @GetMapping("/render")
    public void render(HttpServletResponse response) {
        try {
            String url = URL_AUTH + "?response_type=code&scope=openid&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&state=" + System.currentTimeMillis();
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务平台授权登录回调
     *
     * @param code
     * @return
     */
    @RequestMapping("/callback")
    public String oauth(HttpSession session, HttpServletRequest request, HttpServletResponse response, String code) {
        UserInfo userInfo = new UserInfo();
        try {

            AccessTokenRequest accessTokenRequest = new AccessTokenRequest();

            accessTokenRequest.setClientId(clientId);
            accessTokenRequest.setClientSecret(clientSecret);
            accessTokenRequest.setCode(code);

            if (!(StringUtils.isNotEmpty(URL_GET_TOKEN) && URL_GET_TOKEN.startsWith("http"))) {
                throw new CustomException("不存在或链接格式错误");
            }

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            Request.Builder builderAccessToken = new Request.Builder();
            builderAccessToken.url(URL_GET_TOKEN);

            Request requestAccessToken = builderAccessToken.post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(accessTokenRequest))).build();

            Response responseAccessToken = okHttpClient.newCall(requestAccessToken).execute();
            String resAccessTokenBody = responseAccessToken.body().string();
            Map<String, Object> map = (Map) JSONObject.parseObject(resAccessTokenBody);
            if (map.get("code").toString() == "500") {
                throw new CustomException(map.get("message").toString());
            }
            AccessTokenResponse accessTokenResponse = JSONObject.parseObject(map.get("data").toString(), AccessTokenResponse.class);


            //token 换userinfo获取用户信息
            String urlUser = URL_GET_USER + "?access_token=" + accessTokenResponse.getAccessToken();
            System.out.println(urlUser);
            Request.Builder builderAccessUser = new Request.Builder();
            builderAccessUser.url(urlUser);
            Request requestAccessUser = builderAccessUser.get().build();
            Response responseAccessUser = okHttpClient.newCall(requestAccessUser).execute();
            String resAccessUserBody = responseAccessUser.body().string();
            Map<String, Object> mapUser = (Map) JSONObject.parseObject(resAccessUserBody);

            userInfo = JSONObject.parseObject(mapUser.get("data").toString(), UserInfo.class);


        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("无法获取链接: " + e.getMessage());
        }

        try {
            // 生成token
            String token = loginService.dealLoginToken(userInfo, request);
            response.sendRedirect("https://fc.asun.cloud/fc/login?token=" + token);
        } catch (Exception e) {
            throw new CustomException("登录用户失败！", HttpStatus.ERROR);
        }
        return null;
    }



}
