package com.huiyu.service.core.aspect.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequestLimit {
    int limit() default 5;
    long interval() default 1000;
}
