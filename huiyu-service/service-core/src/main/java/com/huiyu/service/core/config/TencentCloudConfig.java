package com.huiyu.service.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Naccl
 * @date 2023-08-02
 */
@Data
@ConfigurationProperties(prefix = "huiyu.tencentcloud")
@Configuration
public class TencentCloudConfig {
    private List<String> secretId;

    private List<String> secretKey;

    private List<Long> projectId;
}
