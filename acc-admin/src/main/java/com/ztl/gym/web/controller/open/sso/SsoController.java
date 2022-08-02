package com.ztl.gym.web.controller.open.sso;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ztl.gym.code.service.CodeTestService;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.constant.Constants;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.domain.model.AccessTokenRequest;
import com.ztl.gym.common.core.domain.model.AccessTokenResponse;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.core.domain.model.UserInfo;
import com.ztl.gym.common.core.redis.RedisCache;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.utils.MessageUtils;
import com.ztl.gym.common.utils.OkHttpCli;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.framework.manager.AsyncManager;
import com.ztl.gym.framework.manager.factory.AsyncFactory;
import com.ztl.gym.framework.web.service.SysLoginService;
import com.ztl.gym.framework.web.service.SysPermissionService;
import com.ztl.gym.framework.web.service.TokenService;
import com.ztl.gym.product.service.IProductStockService;
import com.ztl.gym.system.service.ISysDeptService;
import com.ztl.gym.system.service.ISysUserService;
import com.ztl.gym.web.controller.open.sso.model.TokenObj;
import com.ztl.gym.web.controller.open.sso.model.UserObj;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@Slf4j
@RequestMapping("/oauth2")
public class SsoController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private OkHttpCli okHttpCli;
    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private DruidDataSource dataSource;

    @Autowired
    private CodeTestService codeService;

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
    private final static String redirectUri = "https://fc.asun.cloud/prod-api/oauth2/callback";

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
        //获取token
        String token = null;
        try {
            token = getToken(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotBlank(token)) {
            Cookie cookie = new Cookie("sso_token", token);
            cookie.setPath("/");
            response.addCookie(cookie);

            try {
                //获取登录用户信息/组织信息
                UserObj userInfo = getUser(token);
                //登录 【判断当前用户是否有账号，没有则注册】
                try {
//                    UserInfo userInfo = new UserInfo();
                    // 生成token
//                    String fcToken = loginService.dealLoginToken(userInfo, request);
                    String fcToken = loginByUser(userInfo,request);
                    response.sendRedirect("https://fc.asun.cloud/login?token=" + fcToken);
                } catch (Exception e) {
                    throw new CustomException("登录用户失败！", HttpStatus.ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 登录   【没有账号则注册】
     *
     * @param userInfo
     */
    private String loginByUser(UserObj userInfo, HttpServletRequest request) {

        //本地处理用户企业
        Long tenantId = Long.valueOf(userInfo.getOrg_id());
        String userName=userInfo.getNick_name();
        SysDept sysDept = sysDeptService.selectDeptById(tenantId);
        if (sysDept == null) {
            sysDept=new SysDept();
            sysDept.setDeptId(Long.valueOf(userInfo.getOrg_id()));
            sysDept.setDeptType(2);
            sysDept.setParentId(AccConstants.ADMIN_DEPT_ID);
            sysDept.setDeptName(userInfo.getOrg_name());
            sysDept.setOrderNum("2");
            sysDept.setCreateBy("admin");
            sysDept.setCreateTime(new Date());
            sysDeptService.insertDept(sysDept);
                //判断新增dept是不是企业级别
                if (sysDept.getParentId() == AccConstants.ADMIN_DEPT_ID) {
                    long companyId = Long.valueOf(userInfo.getOrg_id());

                    //判断该企业对应的分表是否创建
                    int res = -1;
                    //1.t_code
                    if (!codeService.checkCompanyTableExist(companyId, "t_code_")) {
                        String sql = "CREATE TABLE t_code_" + companyId + "(\n" +
                                "    code_index BIGINT NOT NULL AUTO_INCREMENT  COMMENT '流水号' ,\n" +
                                "    company_id BIGINT    COMMENT '企业ID' ,\n" +
                                "    status INT    COMMENT '状态' ,\n" +
                                "    code VARCHAR(64)    COMMENT '码' ,\n" +
                                "    code_acc VARCHAR(64)    COMMENT '防窜码' ,\n" +
                                "    code_type VARCHAR(64)    COMMENT '码类型（箱码or单码）' ,\n" +
                                "    p_code VARCHAR(64)    COMMENT '所属箱码' ,\n" +
                                "    code_attr_id BIGINT    COMMENT '码属性id' ,\n" +
                                "    PRIMARY KEY (code_index)\n" +
                                ") COMMENT = '码表 ';";
                        res = createTableByCompany(sql);
                    }

                    //2.码流转明细表
                    if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_in_code_flow_")) {
                        String sql = "CREATE TABLE t_in_code_flow_" + companyId + "(\n" +
                                "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键ID' ,\n" +
                                "    company_id BIGINT    COMMENT '企业ID' ,\n" +
                                "    code VARCHAR(64)    COMMENT '单码' ,\n" +
                                "    storage_record_id BIGINT    COMMENT '流转记录id' ,\n" +
                                "    create_user BIGINT    COMMENT '创建人' ,\n" +
                                "    create_time DATETIME    COMMENT '创建时间' ,\n" +
                                "    PRIMARY KEY (id)\n" +
                                ") COMMENT = '单码流转记录表 ';";
                        res = createTableByCompany(sql);
                    }
                    if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_out_code_flow_")) {
                        String sql = "CREATE TABLE t_out_code_flow_" + companyId + "(\n" +
                                "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键ID' ,\n" +
                                "    company_id BIGINT    COMMENT '企业ID' ,\n" +
                                "    code VARCHAR(64)    COMMENT '单码' ,\n" +
                                "    storage_record_id BIGINT    COMMENT '流转记录id' ,\n" +
                                "    create_user BIGINT    COMMENT '创建人' ,\n" +
                                "    create_time DATETIME    COMMENT '创建时间' ,\n" +
                                "    PRIMARY KEY (id)\n" +
                                ") COMMENT = '单码流转记录表 ';";
                        res = createTableByCompany(sql);
                    }
                    if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_transfer_code_flow_")) {
                        String sql = "CREATE TABLE t_transfer_code_flow_" + companyId + "(\n" +
                                "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键ID' ,\n" +
                                "    company_id BIGINT    COMMENT '企业ID' ,\n" +
                                "    code VARCHAR(64)    COMMENT '单码' ,\n" +
                                "    storage_record_id BIGINT    COMMENT '流转记录id' ,\n" +
                                "    create_user BIGINT    COMMENT '创建人' ,\n" +
                                "    create_time DATETIME    COMMENT '创建时间' ,\n" +
                                "    PRIMARY KEY (id)\n" +
                                ") COMMENT = '单码流转记录表 ';";
                        res = createTableByCompany(sql);
                    }
                    if (res == 0 && !codeService.checkCompanyTableExist(companyId, "t_back_code_flow_")) {
                        String sql = "CREATE TABLE t_back_code_flow_" + companyId + "(\n" +
                                "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键ID' ,\n" +
                                "    company_id BIGINT    COMMENT '企业ID' ,\n" +
                                "    code VARCHAR(64)    COMMENT '单码' ,\n" +
                                "    storage_record_id BIGINT    COMMENT '流转记录id' ,\n" +
                                "    create_user BIGINT    COMMENT '创建人' ,\n" +
                                "    create_time DATETIME    COMMENT '创建时间' ,\n" +
                                "    PRIMARY KEY (id)\n" +
                                ") COMMENT = '单码流转记录表 ';";
                        res = createTableByCompany(sql);
                    }
                }

        }

        SysUser sysUser = sysUserService.selectUserByUserName(userInfo.getSub());
        if (sysUser == null) {
            sysUser=new SysUser();
            sysUser.setDeptId(Long.valueOf(userInfo.getOrg_id()));
            sysUser.setUserName(userInfo.getSub());
            sysUser.setNickName(userInfo.getNick_name());
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

        if (com.ztl.gym.common.utils.StringUtils.isNotNull(loginUser) )
        {
            tokenService.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        return token;

    }

    String getToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", grantType);
        params.put("redirect_uri", redirectUri);
        String retStr = okHttpCli.doPost(URL_GET_TOKEN, params);
        if (StringUtils.isNotBlank(retStr)) {
            TokenObj tokenObj = JSON.parseObject(retStr, TokenObj.class);
            if (tokenObj != null) {
                return tokenObj.getAccess_token();
            }
        }
        return null;
    }

    UserObj getUser(String token) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        String retStr = okHttpCli.doGet(URL_GET_USER, params);
        UserObj userObj = JSON.parseObject(retStr, UserObj.class);
        return userObj;
    }
    /**
     * 按企业创建分表
     *
     * @param sql 建表sql
     * @return
     */
    private int createTableByCompany(String sql) {
        int res = -1;
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            res = statement.executeUpdate(sql);
            System.out.println(res);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            res = -2;
        } finally {
            return res;
        }
    }
}
