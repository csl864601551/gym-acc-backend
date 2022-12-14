package com.ztl.gym.web.controller.open.system;

import cn.hutool.core.util.StrUtil;
import com.ztl.gym.area.domain.CompanyArea;
import com.ztl.gym.area.service.ICompanyAreaService;
import com.ztl.gym.code.service.CodeTestService;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.entity.SysMenu;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.domain.model.LoginBody;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.domain.AndroidVersion;
import com.ztl.gym.common.enums.BusinessType;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.IAndroidVersionService;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.ServletUtils;
import com.ztl.gym.framework.web.service.SysLoginService;
import com.ztl.gym.framework.web.service.SysPermissionService;
import com.ztl.gym.framework.web.service.TokenService;
import com.ztl.gym.product.service.IProductStockService;
import com.ztl.gym.system.domain.SysDeptERP;
import com.ztl.gym.system.service.ISysDeptService;
import com.ztl.gym.system.service.ISysMenuService;
import com.ztl.gym.system.service.ISysPostService;
import com.ztl.gym.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ICompanyAreaService companyAreaService;
    /**
     * ????????????
     *
     * @param loginBody ????????????
     * @return ??????
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        try {

            AjaxResult ajax = AjaxResult.success();
            // ????????????
            String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                    loginBody.getUuid(),loginBody.getThirdPartyFlag());
            ajax.put("data", token);
            return ajax;
        }catch (Exception e){
            throw new CustomException("????????????????????????", HttpStatus.ERROR);
        }
    }

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // ????????????
        Set<String> roles = permissionService.getRolePermission(user);
        // ????????????
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        Map<String,Object> map=new HashMap<>();
//        map.put("user", user);
        map.put("roles", roles);
        map.put("postIds", userService.selectUserPostGroup(user.getUserName()));
        //map.put("permissions", permissions);
        if(user.getDept().getParentId()==100){
            map.put("roleName", "?????????");
        }else if(user.getDept().getParentId()!=100&&user.getDept().getDeptType()==1){
            map.put("roleName", "?????????");
        }else if(user.getDept().getParentId()!=100&&user.getDept().getDeptType()==2){
            map.put("roleName", "?????????");
        }
        ajax.put("data", map);
        return ajax;
    }

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // ????????????
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());

        List<SysMenu> list=new ArrayList<>();
        for (int i=0 ;i<menus.size();i++){
            if(menus.get(i).getMenuName().equals("????????????")||menus.get(i).getMenuName().equals("????????????")){
                list.addAll(menus.get(i).getChildren());
            }
        }
        return AjaxResult.success(list);
    }
    /**
     * PDA????????????
     *
     * @return ??????
     */
    @PostMapping("/pdaUpdate")
    public AjaxResult pdaUpdate(@RequestBody Map<String,Object> temp)
    {
        try {
            Map<String,Object> map=new HashMap<>();
            Integer currentVersion=Integer.valueOf(temp.get("currentVersion").toString());//???????????????
            String clientType=temp.get("clientType").toString();//???????????? dayi

            AndroidVersion androidVersion=new AndroidVersion();
            List<AndroidVersion> list = androidVersionService.selectAndroidVersionList(androidVersion);
            if(list.size()>0){
                if(list.get(0).getVersion()>currentVersion){
                    return AjaxResult.success(list.get(0));
                }
            }
            map.put("versionName","1.0.5");
            map.put("updateDescription",1);// 1.bug????????? 2.?????????
            map.put("apkUrl","http://10.70.151.108:8080/examples/1.apk");
            map.put("fileSize",20480);
            map.put("forceUpdate",false);
            map.put("isUpdate",false);
            return AjaxResult.success(map);



        }catch (Exception e){
            throw new CustomException("???????????????????????????????????????", HttpStatus.ERROR);
        }
    }


    /**
     * ????????????
     */
    @Log(title = "ERP???????????????", businessType = BusinessType.INSERT)
    @PostMapping("getERPDept")
    public AjaxResult getERPDept(@Validated @RequestBody SysDeptERP dept) {
        //???????????????
        if (StrUtil.isEmpty(dept.getDeptNo())) {
            throw new CustomException("??????????????????????????????", HttpStatus.ERROR);
        }
        if (StrUtil.isEmpty(dept.getDeptName())) {
            throw new CustomException("??????????????????????????????", HttpStatus.ERROR);
        }


        SysDept deptQuery=new SysDept();
        deptQuery.setDeptNo(dept.getDeptNo());
        List<SysDept> deptList=deptService.selectDeptList(deptQuery);

        deptQuery.setDeptName(dept.getDeptName());
        deptQuery.setParentId(SecurityUtils.getLoginUserTopCompanyId());
        deptQuery.setDeptType(AccConstants.DEPT_TYPE_ZY);

        if(deptList.size()==0){
            deptQuery.setCreateBy(SecurityUtils.getUsername());
            deptService.insertDept(deptQuery);

        }else{
            deptQuery.setDeptId(deptList.get(0).getDeptId());
            deptQuery.setUpdateBy(SecurityUtils.getUsername());
            deptService.updateDept(deptQuery);
        }


        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        Long companyId = SecurityUtils.getLoginUserCompany().getDeptId();
        //???????????????????????????
        if(dept.getCompanyArea()!=null){
            companyAreaService.deleteCompanyAreaByTenantId(deptQuery.getDeptId());
            CompanyArea companyArea = new CompanyArea();
            List<CompanyArea> areaList = dept.getCompanyArea();
            for (int i = 0; i < areaList.size(); i++) {
                companyArea = areaList.get(i);
                companyArea.setCompanyId(companyId);
                companyArea.setTenantId(deptQuery.getDeptId());
                companyArea.setCreateUser(userId);
                companyArea.setCreateTime(DateUtils.getNowDate());
                companyArea.setUpdateUser(userId);
                companyArea.setUpdateTime(DateUtils.getNowDate());
                companyAreaService.insertCompanyArea(companyArea);
            }
        }





        return AjaxResult.success("???????????????");
    }
}
