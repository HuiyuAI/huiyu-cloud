package com.huiyu.service.core.aspect;

import com.huiyu.common.core.result.R;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.core.aspect.annotation.RequestLogger;
import com.huiyu.service.core.entity.RequestLog;
import com.huiyu.service.core.service.RequestLogService;
import com.huiyu.service.core.utils.RequestUtils;
import com.huiyu.service.core.utils.IdUtils;
import com.huiyu.service.core.utils.IpAddressUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 记录请求日志
 *
 * @author Naccl
 * @date 2020-12-04
 */
@Order(2)
@Aspect
@Component
public class RequestLogAspect {
    @Resource
    private RequestLogService requestLogService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(requestLogger)")
    public void logPointcut(RequestLogger requestLogger) {
    }

    /**
     * 配置环绕通知
     */
    @Around("logPointcut(requestLogger)")
    public Object logAround(ProceedingJoinPoint joinPoint, RequestLogger requestLogger) throws Throwable {
        currentTime.set(System.currentTimeMillis());
        R result = (R) joinPoint.proceed();
        int elapsed = (int) (System.currentTimeMillis() - currentTime.get());
        currentTime.remove();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, Object> requestParams = RequestUtils.getRequestParams(joinPoint);
        String param = StringUtils.substring(JacksonUtils.toJsonStr(requestParams), 0, 2000);

        RequestLog requestLog = RequestLog.builder()
                .id(IdUtils.nextSnowflakeId())
                .userId(JwtUtils.getUserId())
                .ip(IpAddressUtils.getIpAddress(request))
                .method(request.getMethod())
                .uri(request.getRequestURI())
                .param(param)
                .elapsedTime(elapsed)
                .createTime(LocalDateTime.now())
                .build();
        requestLogService.insert(requestLog);
        return result;
    }

}
