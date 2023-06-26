package com.huiyu.service.core.aspect.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequestLimit {
    int maxCount() default 5;
    int seconds() default 1;
}
