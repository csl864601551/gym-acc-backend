package com.ztl.gym.web.controller.open.system;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.constant.Constants;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysMenu;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.domain.model.LoginBody;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.ServletUtils;
import com.ztl.gym.framework.web.service.SysLoginService;
import com.ztl.gym.framework.web.service.SysPermissionService;
import com.ztl.gym.framework.web.service.TokenService;
import com.ztl.gym.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

@RestController
@RequestMapping("/open/system")
public class OpenSystemController {
    @Autowired
    private SysLoginService loginService;
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
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid(),loginBody.getThirdPartyFlag());
        ajax.put("data", token);
        return ajax;
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
}
