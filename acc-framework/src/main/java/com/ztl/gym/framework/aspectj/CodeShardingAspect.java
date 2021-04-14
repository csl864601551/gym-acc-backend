package com.ztl.gym.framework.aspectj;

import com.alibaba.fastjson.JSON;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.annotation.Log;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.enums.BusinessStatus;
import com.ztl.gym.common.enums.HttpMethod;
import com.ztl.gym.common.utils.ServletUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.common.utils.ip.IpUtils;
import com.ztl.gym.common.utils.spring.SpringUtils;
import com.ztl.gym.framework.datasource.DynamicDataSourceContextHolder;
import com.ztl.gym.framework.manager.AsyncManager;
import com.ztl.gym.framework.manager.factory.AsyncFactory;
import com.ztl.gym.framework.web.service.TokenService;
import com.ztl.gym.system.domain.SysOperLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * 操作日志记录处理
 *
 * @author ruoyi
 */
//@Aspect
//@Component
public class CodeShardingAspect
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.ztl.gym.common.annotation.DataSource)"
            + "|| @within(com.ztl.gym.common.annotation.DataSource)")
    public void codeShardingPointCut()
    {

    }

    @Around("codeShardingPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        DataSource dataSource = getDataSource(point);

        if (StringUtils.isNotNull(dataSource))
        {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }

        try
        {
            return point.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }


    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point)
    {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource))
        {
            return dataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }


    @Before("codeShardingPointCut()")
    public void doBefore(JoinPoint point) throws Throwable
    {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint)
    {

    }

}
