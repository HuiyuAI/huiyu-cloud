package com.huiyu.auth.security.config;

import com.huiyu.service.api.feign.SysPermissionFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.huiyu.common.core.result.R;

/**
 * 容器启动完成时加载角色权限规则至Redis缓存
 *
 * @author Naccl
 * @date 2022-03-08
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class InitPermissionRolesCache implements CommandLineRunner {
    private final SysPermissionFeignClient sysPermissionFeignClient;

    @Override
    public void run(String... args) {
        R<Boolean> result = sysPermissionFeignClient.refreshPermRolesRules();
        log.info("初始化权限角色规则缓存：{}", result.getData());
    }
}
