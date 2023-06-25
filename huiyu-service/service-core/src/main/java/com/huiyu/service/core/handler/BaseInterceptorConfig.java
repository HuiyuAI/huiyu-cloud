package com.huiyu.service.core.handler;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Auther: wAnG
 * @Date: 2021/11/26 16:49
 * @Description: 注册拦截器
 */

@Configuration
public class BaseInterceptorConfig extends WebMvcConfigurationSupport {

    @Resource
    private RequestInterceptor requestInterceptor;

    @Resource
    private Converter<String, LocalDateTime> localDateTimeConverter;

    @Resource
    private Converter<String, Date> dateConverter;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .maxAge(3600);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverter(localDateTimeConverter);
        registry.addConverter(dateConverter);
        super.addFormatters(registry);
    }
}
