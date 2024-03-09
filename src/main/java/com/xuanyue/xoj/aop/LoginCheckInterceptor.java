package com.xuanyue.xoj.aop;

import com.xuanyue.xoj.common.ErrorCode;
import com.xuanyue.xoj.exception.BusinessException;
import com.xuanyue.xoj.utils.JwtUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Aspect
@Component
public class LoginCheckInterceptor {

    @Value("#{'${gateway.excludedUrls}'.split(',')}")
    private List<String> excludedUrls;

    @Around("execution(* com.xuanyue.xoj.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前请求路径
        String path = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServletPath();
        // 如果当前链接不需要校验则直接放行
        if (excludedUrls.contains(path)) {
            return joinPoint.proceed();
        }

        // 获取token并校验
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        // 不为空时把 ("Bearer"去掉) 有时候前端传来的 token是带这个的
        if (token != null) {
            token = token.replace("Bearer ", "");
        }

        // 使用工具类，判断token是否有效
        boolean verifyToken = JwtUtils.verifyToken(token);
        // 如果token失效，返回状态码401，拦截
        if (!verifyToken) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "Token失效，请重新登录");
        }

        return joinPoint.proceed();
    }
}