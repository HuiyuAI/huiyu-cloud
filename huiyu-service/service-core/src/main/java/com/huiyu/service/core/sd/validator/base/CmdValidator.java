package com.huiyu.service.core.sd.validator.base;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wAnG
 * @Date 2023-06-12  00:32
 */

@Component
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface CmdValidator {

    String[] dealType() default "";

    String name() default "";

}
