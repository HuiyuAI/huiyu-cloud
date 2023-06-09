package com.huiyu.service.api.feign;

import com.huiyu.common.core.result.R;
import com.huiyu.service.api.entity.SysOauthClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * oauth2客户端 FeignClient
 *
 * @author Naccl
 * @date 2022-03-07
 */
@FeignClient(value = "huiyu-service", contextId = "sysOauthClientFeignClient")
public interface SysOauthClientFeignClient {
    /**
     * 通过主键查询单条数据
     *
     * @param clientId 主键
     * @return 单条数据
     */
    @GetMapping("/sysOauthClient/{clientId}")
    R<SysOauthClient> queryByClientId(@PathVariable("clientId") String clientId);
}
