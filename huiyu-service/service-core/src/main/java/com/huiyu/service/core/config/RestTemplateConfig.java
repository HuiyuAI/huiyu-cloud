package com.huiyu.service.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置
 *
 * @author Naccl
 * @date 2023-06-11
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 默认的RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60 * 1000);
        return new RestTemplate(requestFactory);
    }
}
