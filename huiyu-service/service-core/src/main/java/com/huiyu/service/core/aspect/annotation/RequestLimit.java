package com.huiyu.service.core.aspect.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {
    /**
     * 限制周期(秒)
     */
    int seconds();

    /**
     * 规定周期内限制次数
     */
    int maxCount();

    /**
     * 触发限制时的消息提示
     */
    String msg() default "操作频率过高";
}
