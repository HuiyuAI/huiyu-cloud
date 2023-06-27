package com.huiyu.service.core.Hconfig.annotation;

import com.huiyu.service.core.Hconfig.HConfigType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wAnG
 * @Date 2023-06-23  21:07
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface HConfig {

    String dataId();

    String group() default "DEFAULT_GROUP";

    HConfigType suffix();
}
