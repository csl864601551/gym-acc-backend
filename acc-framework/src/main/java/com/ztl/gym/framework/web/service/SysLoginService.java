package com.ztl.gym.framework.web.service;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.Constants;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.core.domain.model.UserInfo;
import com.ztl.gym.common.core.redis.RedisCache;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.exception.user.CaptchaException;
import com.ztl.gym.common.exception.user.CaptchaExpireException;
import com.ztl.gym.common.exception.user.UserPasswordNotMatchException;
import com.ztl.gym.common.utils.MessageUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.framework.manager.AsyncManager;
import com.ztl.gym.framework.manager.factory.AsyncFactory;
import com.ztl.gym.system.service.ISysDeptService;
import com.ztl.gym.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Set;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private ISysUserService sysUserService;
    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid,String flag)
    {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if(StringUtils.isEmpty(flag)){
            if (captcha == null)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha))
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }

        // 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, StringUtils.isEmpty(flag) ?"WEB"+"|"+ MessageUtils.message("user.login.success"):flag+"|"+ MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }

    public String dealLoginToken(UserInfo userInfo, HttpServletRequest request) {
        //本地处理用户企业
        Long tenantId = Long.valueOf(userInfo.getTenantId());
        String userName=userInfo.getUserName();
        SysDept sysDept = sysDeptService.selectDeptById(tenantId);
        if (sysDept == null) {
            sysDept=new SysDept();
            sysDept.setDeptId(Long.valueOf(userInfo.getTenantId()));
            sysDept.setDeptType(2);
            sysDept.setParentId(AccConstants.ADMIN_DEPT_ID);
            sysDept.setDeptName(userInfo.getCompanyName());
            sysDept.setOrderNum("2");
            sysDept.setCreateBy("admin");
            sysDept.setCreateTime(new Date());
            sysDeptService.insertDept(sysDept);
        }

        SysUser sysUser = sysUserService.selectUserByUserName(userInfo.getAccount());
        if (sysUser == null) {
            sysUser=new SysUser();
            sysUser.setDeptId(Long.valueOf(userInfo.getTenantId()));
            sysUser.setUserName(userInfo.getAccount());
            sysUser.setNickName(userInfo.getUserName());
            sysUser.setPassword(SecurityUtils.encryptPassword("tbdg54321"));
            sysUser.setCreateBy("admin");
            sysUser.setCreateTime(new Date());
            Long[] roleIds={101L};
            sysUser.setRoleIds(roleIds);//添加角色权限
            sysUserService.insertUser(sysUser);

        }

        //本地登录

        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_SUCCESS, "gymAuth|"+ MessageUtils.message("user.login.success")));
        LoginUser loginUser = new LoginUser();
        sysUser.setDept(sysDept);
        loginUser.setUser(sysUser);
        loginUser.setPermissions(permissions);
        System.out.println("dealLoginToken:loginUser:------"+loginUser);
        // 生成token
        String token= tokenService.createToken(loginUser);

        if (StringUtils.isNotNull(loginUser) )
        {
            tokenService.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        return token;
    }
}
