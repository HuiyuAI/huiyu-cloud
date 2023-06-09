package com.huiyu.auth.security.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import com.huiyu.common.core.result.R;

/**
 * URL权限映射 FeignClient
 *
 * @author Naccl
 * @date 2022-03-12
 */
@FeignClient(value = "clh-service-sys", contextId = "sysPermissionFeignClient")
public interface SysPermissionFeignClient {
    /**
     * 加载角色权限规则至Redis缓存
     *
     * @return 是否成功
     */
    @PutMapping("/sysPermission/refreshPermRolesRules")
    R<Boolean> refreshPermRolesRules();
}
