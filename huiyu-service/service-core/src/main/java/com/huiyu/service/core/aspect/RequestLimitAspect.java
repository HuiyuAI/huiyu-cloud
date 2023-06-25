package com.huiyu.service.core.aspect;

import com.huiyu.service.core.aspect.annotation.RequestLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class RequestLimitAspect {


    private final ConcurrentHashMap<String, AtomicInteger> requestCountMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Long> requestDateMap = new ConcurrentHashMap<>();

    @Around("@annotation(requestLimit)")
    public Object limitRequestFrequency(ProceedingJoinPoint joinPoint, RequestLimit requestLimit) throws Throwable {
        // 获取请求的 IP 和请求方法名
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = request.getRemoteAddr();
        String methodName = joinPoint.getSignature().toLongString();

        // 判断该 IP 的请求次数是否超过限制
        if (isRequestLimitExceeded(ip, methodName, requestLimit)) {
                throw new RuntimeException("请求频率超限，请稍后重试。");
        }

        return joinPoint.proceed();
    }

    private boolean isRequestLimitExceeded(String ip, String methodName, RequestLimit requestLimit) {
        String key = ip + "-" + methodName;
        AtomicInteger count = requestCountMap.computeIfAbsent(key, k -> new AtomicInteger(0));
        Long requestDate = requestDateMap.computeIfAbsent(key, k -> System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();
        long interval = requestLimit.interval();
        int limit = requestLimit.limit();
        boolean exceeded = false;
        if (currentTime - requestDate > interval) {
            count.set(0);
            requestDateMap.put(key, currentTime);
        }
        if (count.incrementAndGet() > limit) {
            exceeded = true;
        }
        return exceeded;
    }

}
