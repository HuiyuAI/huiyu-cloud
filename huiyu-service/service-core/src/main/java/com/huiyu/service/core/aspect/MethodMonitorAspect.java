package com.huiyu.service.core.aspect;

import com.google.common.base.Stopwatch;
import com.huiyu.service.core.config.Monitor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author wAnG
 * @Date 2023-06-17  01:18
 */
@Order(3)
@Aspect
@Component
public class MethodMonitorAspect {


    private static final String MONITOR_COUNT_NAME = "methodCount";

    private static final String MONITOR_TIME_NAME = "methodTime";


    @Around("@annotation(com.huiyu.service.core.aspect.annotation.MethodMonitor)")
    public Object methodInfo(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        String methodName = method.getName();
        Stopwatch stopwatch = Stopwatch.createStarted();

        String exceptionClass = "none";
        try {
            return pjp.proceed();
        } catch (Exception e) {
            exceptionClass = e.getClass().getSimpleName();
            throw e;
        } finally {
            Monitor.recordOne(MONITOR_COUNT_NAME, methodName);
            Monitor.recordTime(MONITOR_TIME_NAME, methodName, stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
        }


    }

}
