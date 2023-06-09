package com.huiyu.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 网关白名单配置
 *
 * @author Naccl
 * @date 2022-03-01
 */
@Data
@ConfigurationProperties(prefix = "secure.ignore")
@Configuration
public class IgnoreUrlsProperties {
    /**
     * 白名单path
     */
    private List<String> urls;
}
