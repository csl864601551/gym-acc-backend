package com.ztl.gym.web.controller.open.system;

import com.alibaba.fastjson.JSONObject;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysMenu;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.domain.model.*;
import com.ztl.gym.common.domain.AndroidVersion;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.IAndroidVersionService;
import com.ztl.gym.common.utils.ServletUtils;
import com.ztl.gym.framework.web.service.SysLoginService;
import com.ztl.gym.framework.web.service.SysPermissionService;
import com.ztl.gym.framework.web.service.TokenService;
import com.ztl.gym.system.service.ISysDeptService;
import com.ztl.gym.system.service.ISysDictDataService;
import com.ztl.gym.system.service.ISysMenuService;
import com.ztl.gym.system.service.ISysUserService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/open/system")
public class OpenSystemController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private IAndroidVersionService androidVersionService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Value("${ruoyi.website}")
    private String website;

    @Value("${ruoyi.websiteLogin}")
    private String websiteLogin;
    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        try {

            AjaxResult ajax = AjaxResult.success();
            // 生成令牌
            String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                    loginBody.getUuid(),loginBody.getThirdPartyFlag());
            ajax.put("data", token);
            return ajax;
        }catch (Exception e){
            throw new CustomException("用户名密码错误！", HttpStatus.ERROR);
        }
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        Map<String,Object> map=new HashMap<>();
//        map.put("user", user);
        map.put("roles", roles);
        //map.put("permissions", permissions);
        if(user.getDept().getParentId()==100){
            map.put("roleName", "企业端");
        }else if(user.getDept().getParentId()!=100&&user.getDept().getDeptType()==1){
            map.put("roleName", "稽查员");
        }else if(user.getDept().getParentId()!=100&&user.getDept().getDeptType()==2){
            map.put("roleName", "经销商");
        }
        ajax.put("data", map);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());

        List<SysMenu> list=new ArrayList<>();
        for (int i=0 ;i<menus.size();i++){
            if(menus.get(i).getMenuName().equals("物流管理")||menus.get(i).getMenuName().equals("窜货管理")){
                list.addAll(menus.get(i).getChildren());
            }
        }
        return AjaxResult.success(list);
    }
    /**
     * PDA更新方法
     *
     * @return 结果
     */
    @PostMapping("/pdaUpdate")
    public AjaxResult pdaUpdate(@RequestBody Map<String,Object> temp)
    {
        try {
            Map<String,Object> map=new HashMap<>();
            Integer currentVersion=Integer.valueOf(temp.get("currentVersion").toString());//当前版本号
            String clientType=temp.get("clientType").toString();//终端类型 dayi

            AndroidVersion androidVersion=new AndroidVersion();
            List<AndroidVersion> list = androidVersionService.selectAndroidVersionList(androidVersion);
            if(list.size()>0){
                if(list.get(0).getVersion()>currentVersion){
                    return AjaxResult.success(list.get(0));
                }
            }
            map.put("versionName","1.0.5");
            map.put("updateDescription",1);// 1.bug修复； 2.新功能
            map.put("apkUrl","http://10.70.151.108:8080/examples/1.apk");
            map.put("fileSize",20480);
            map.put("forceUpdate",false);
            map.put("isUpdate",false);
            return AjaxResult.success(map);



        }catch (Exception e){
            throw new CustomException("登录超时，请检查网络连接！", HttpStatus.ERROR);
        }
    }

    /**
     * 第一步
     * 回调工业码登录
     */
    @GetMapping("/backAuth")
    public void backLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            String redirectUrl = website + "/sy/openApi/v1/oauth/authorize?client_id=" + sysDictDataService.selectDictValue("gym_dic", "clientId");
            System.out.println(redirectUrl);
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            throw new CustomException("用户名密码错误！", HttpStatus.ERROR);
        }
    }

    /**
     * 第二步
     * 工业码回调处理
     *
     * @param code 登录信息
     * @return 结果
     */
    @GetMapping("/authInfoLogin")
    public void codeBackDeal(String code, HttpServletRequest request, HttpServletResponse response) {
        UserInfo userInfo = new UserInfo();
        try {
            String urlAccessToken = website + "/sy/openApi/v1/oauth/access_token";

            AccessTokenRequest accessTokenRequest = new AccessTokenRequest();
            ;
            accessTokenRequest.setClientId(sysDictDataService.selectDictValue("gym_dic", "clientId"));
            accessTokenRequest.setClientSecret(sysDictDataService.selectDictValue("gym_dic", "clientSecret"));
            accessTokenRequest.setCode(code);

            if (!(StringUtils.isNotEmpty(urlAccessToken) && urlAccessToken.startsWith("http"))) {
                throw new CustomException("不存在或链接格式错误");
            }

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            Request.Builder builderAccessToken = new Request.Builder();
            builderAccessToken.url(urlAccessToken);

            System.out.println(urlAccessToken);
            System.out.println(accessTokenRequest);
            Request requestAccessToken = builderAccessToken.post(okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(accessTokenRequest))).build();
            System.out.println(requestAccessToken);
            Response responseAccessToken = okHttpClient.newCall(requestAccessToken).execute();
            System.out.println(responseAccessToken);
            String resAccessTokenBody = responseAccessToken.body().string();
            System.out.println(resAccessTokenBody);
            Map<String, Object> map = (Map) JSONObject.parseObject(resAccessTokenBody);
            System.out.println(map);
            if (map.get("code").toString() == "500") {
                throw new CustomException(map.get("message").toString());
            }
            AccessTokenResponse accessTokenResponse = JSONObject.parseObject(map.get("data").toString(), AccessTokenResponse.class);

            System.out.println(map);

            //token 换userinfo获取用户信息
            String urlUser = website + "/sy/openApi/v1/oauth/user?access_token=" + accessTokenResponse.getAccessToken();
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
            response.sendRedirect(websiteLogin+"/fc/login?token=" + token);
        } catch (Exception e) {
            throw new CustomException("登录用户失败！", HttpStatus.ERROR);
        }
    }

    /**
     * 第二步
     * 工业码回调处理
     *
     * @return 结果
     */
    @GetMapping("/authAdminLogin")
    public void codeBackDeal(HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setTenantId("100");
            userInfo.setUserName("");
            userInfo.setAccount("admin");
            // 生成token
            String token = loginService.dealLoginToken(userInfo, request);
            response.sendRedirect(websiteLogin+"/fc/login?token=" + token);
        } catch (Exception e) {
            throw new CustomException("登录用户失败！", HttpStatus.ERROR);
        }
    }
}
