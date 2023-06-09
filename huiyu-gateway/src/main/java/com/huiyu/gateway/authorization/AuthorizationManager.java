package com.huiyu.gateway.authorization;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;
import com.huiyu.common.core.constant.SecurityConstants;
import com.huiyu.common.redis.constant.RedisKeyConstant;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 *
 * @author Naccl
 * @date 2022-03-01
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    private final RedisTemplate redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        // 预检请求放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        PathMatcher pathMatcher = new AntPathMatcher();
        String restfulPath = request.getMethodValue() + ":" + request.getURI().getPath();
        log.info("restfulPath: {}", restfulPath);
        /**
         * 从Redis中获取当前路径可访问角色列表 [URL-角色集合]
         * urlPermRolesRules = [{'key':'GET:/api/v1/users/*','value':['ADMIN','TEST']},...]
         */
        Map<String, Object> urlPermRolesRules = redisTemplate.opsForHash().entries(RedisKeyConstant.RESOURCE_ROLES_MAP);

        // 对应path拥有访问权限的角色
        Set<String> authorizedRoles = new HashSet<>(8);

        // 是否需要鉴权，未设置规则的path不需鉴权
        boolean requireCheck = false;
        // 根据请求路径获取有访问权限的角色列表
        for (Map.Entry<String, Object> permRoles : urlPermRolesRules.entrySet()) {
            String perm = permRoles.getKey();
            if (pathMatcher.match(perm, restfulPath)) {
                List<String> roles = Convert.toList(String.class, permRoles.getValue());
                authorizedRoles.addAll(roles);
                requireCheck = true;
            }
        }

        log.info("requireCheck: {}", requireCheck);
        // 没有设置拦截规则放行
        if (!requireCheck) {
            return Mono.just(new AuthorizationDecision(true));
        }
        log.info("authorizedRoles: {}", authorizedRoles);

        //认证通过且角色匹配的用户可访问当前路径
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authority -> {
                    // 用户的角色
                    String role = authority.substring(SecurityConstants.ROLE_PREFIX.length());
                    log.info("user role: {}", role);
                    if (SecurityConstants.ROLE_ROOT.equals(role)) {
                        // 如果是超级管理员则放行
                        return true;
                    }
                    return CollUtil.isNotEmpty(authorizedRoles) && authorizedRoles.contains(role);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
