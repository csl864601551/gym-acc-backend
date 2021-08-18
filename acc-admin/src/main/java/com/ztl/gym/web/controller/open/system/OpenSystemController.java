package com.ztl.gym.web.controller.open.system;

import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysMenu;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.domain.model.LoginBody;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.domain.AndroidVersion;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.IAndroidVersionService;
import com.ztl.gym.common.utils.ServletUtils;
import com.ztl.gym.framework.web.service.SysLoginService;
import com.ztl.gym.framework.web.service.SysPermissionService;
import com.ztl.gym.framework.web.service.TokenService;
import com.ztl.gym.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        //map.put("user", user);
        //map.put("roles", roles);
        //map.put("permissions", permissions);
        if(user.getDept().getParentId()==100){
            map.put("roleName", "企业端");
        }else if(user.getDept().getParentId()!=100&&user.getDept().getDeptType()==1){
            map.put("roleName", "稽查员");
        }else if(user.getDept().getParentId()!=100&&user.getDept().getDeptType()==2){
            map.put("roleName", "经销商");
        }
        map.put("deptName", user.getDept().getDeptName());
        map.put("nickName", user.getNickName());
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
}
