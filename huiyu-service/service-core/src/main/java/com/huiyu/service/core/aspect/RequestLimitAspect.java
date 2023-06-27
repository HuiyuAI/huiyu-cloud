package com.huiyu.service.core.aspect;

import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.core.aspect.annotation.RequestLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class RequestLimitAspect {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(requestLimit)")
    public Object limitRequestFrequency(ProceedingJoinPoint joinPoint, RequestLimit requestLimit) throws Throwable {
        // 获取请求的 IP 和请求方法名
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        int seconds = requestLimit.seconds();
        int maxCount = requestLimit.maxCount();
        String ip = request.getRemoteAddr();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String redisKey = ip + ":" + method + ":" + requestURI;
        Object redisResult = redisTemplate.opsForValue().get(redisKey);
        Integer count = JacksonUtils.toBean(redisResult, Integer.class);
        if (count == null) {
            // 在规定周期内第一次访问，存入redis
            redisTemplate.opsForValue().increment(redisKey, 1);
            redisTemplate.expire(redisKey, seconds, TimeUnit.SECONDS);
        } else {
            if (count >= maxCount) {
                log.error(" error count: {}", count);
//                throw new RuntimeException("请求频率超限，请稍后重试。");
            } else {
                //没超出访问限制次数
                redisTemplate.opsForValue().increment(redisKey, 1);
            }
        }
        log.info("count: {}", count);
        return joinPoint.proceed();
    }
}
