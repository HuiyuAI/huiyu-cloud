package com.huiyu.auth.security.core.clientdetails;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.huiyu.auth.domain.SysOauthClient;
import com.huiyu.common.core.result.R;

/**
 * oauth2客户端 FeignClient
 *
 * @author Naccl
 * @date 2022-03-07
 */
@FeignClient(value = "clh-service-sys", contextId = "sysOauthClientFeignClient")
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
