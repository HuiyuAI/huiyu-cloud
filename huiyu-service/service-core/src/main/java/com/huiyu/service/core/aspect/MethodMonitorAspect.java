package com.huiyu.service.core.aspect;

import com.google.common.base.Stopwatch;
import com.huiyu.service.core.config.Monitor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author wAnG
 * @Date 2023-06-17  01:18
 */
@Aspect
@Component
public class MethodMonitorAspect {

    private static final String TIME_SUFFIX = "_Time";

    private static final String COUNT_SUFFIX = "_Count";


    @Around("@annotation(com.huiyu.service.core.annotation.MethodMonitor)")
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
            Monitor.recordOne(methodName + COUNT_SUFFIX);
            Monitor.recordTime(methodName + TIME_SUFFIX, stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
        }


    }

}
