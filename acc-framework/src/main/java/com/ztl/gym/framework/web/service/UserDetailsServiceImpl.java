package com.ztl.gym.framework.web.service;

import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.entity.SysUser;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.system.service.ISysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ztl.gym.common.enums.UserStatus;
import com.ztl.gym.common.exception.BaseException;
import com.ztl.gym.system.service.ISysUserService;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }

        //判断所属企业或上级企业【每个上级都要判断】是否被停用
        if (!user.getDeptId().equals(AccConstants.ADMIN_DEPT_ID)) {
            SysDept sysDept = deptService.selectDeptById(user.getDeptId());
            if (sysDept == null) {
                log.info("登录用户：{} 部门不存在.", username);
                throw new BaseException("对不起，您的企业不存在");
            } else if (sysDept != null && sysDept.getStatus().equals(AccConstants.DEPT_STATUS_STOP)) {
                log.info("登录用户：{} 部门被停用.", username);
                throw new BaseException("对不起，您的企业已被停用");
            }

            //判断每层上级是否被停用【目前若依逻辑是停用上级，必须停用下级】
//            String[] ancestors = sysDept.getAncestors().split(",");
//            for (int i = ancestors.length - 1; i > 1; i--) {
//                long pDeptId = Long.parseLong(ancestors[i]);
//                SysDept pDept = deptService.selectDeptById(pDeptId);
//                if (pDept == null) {
//                    log.info("登录用户：{} 部门或上级部门不存在.", username);
//                    throw new BaseException("对不起，您的企业或上级企业不存在");
//                } else if (pDept != null && pDept.getStatus().equals(AccConstants.DEPT_STATUS_STOP)) {
//                    log.info("登录用户：{} 部门或上级部门被停用.", username);
//                    throw new BaseException("对不起，您的企业或上级企业已被停用");
//                }
//            }
        }
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }
}
