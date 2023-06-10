package com.huiyu.service.core.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Auther: wAnG
 * @Date: 2021/11/26 16:49
 * @Description: 注册拦截器
 */

@Configuration
public class BaseInterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    private RequestInterceptor requestInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
