package com.ztl.gym.common.utils;

import com.ztl.gym.common.core.domain.entity.SysDept;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.exception.CustomException;

/**
 * 安全服务工具类
 *
 * @author ruoyi
 */
public class SecurityUtils {
    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new CustomException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户企业经销商信息【对应若依中-- 用户的部门信息】
     **/
    public static SysDept getLoginUserCompany() {
        try {
            LoginUser loginUser = (LoginUser) getAuthentication().getPrincipal();
            return loginUser.getUser().getDept();
        } catch (Exception e) {
            throw new CustomException("获取用户部门信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户所属的企业Id 【对应若依中-- 用户除平台外的顶级部门，ancestors值为0,100的部门】
     **/
    public static Long getLoginUserTopCompanyId() {
        try {
            LoginUser loginUser = (LoginUser) getAuthentication().getPrincipal();
            SysDept sysDept = loginUser.getUser().getDept();
            if (sysDept.getAncestors().contains(",")) {
                String[] ancestors = sysDept.getAncestors().split(",");
                if (ancestors.length <= 2) {
                    return sysDept.getDeptId();
                } else {
                    return Long.parseLong(ancestors[2]);
                }
            } else {
                return sysDept.getDeptId();
//                throw new CustomException("平台是最顶级部门，不属于企业", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            throw new CustomException("获取用户除平台外顶级部门信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
