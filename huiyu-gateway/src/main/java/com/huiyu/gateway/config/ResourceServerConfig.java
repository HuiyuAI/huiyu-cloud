package com.huiyu.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;
import com.huiyu.common.core.result.R;
import com.huiyu.common.core.result.ResultCode;
import com.huiyu.common.core.constant.SecurityConstants;
import com.huiyu.gateway.authorization.AuthorizationManager;
import com.huiyu.gateway.config.properties.IgnoreUrlsProperties;
import com.huiyu.gateway.filter.IgnoreUrlsRemoveJwtFilter;

import java.nio.charset.StandardCharsets;

/**
 * 资源服务器配置
 *
 * @author Naccl
 * @date 2022-03-01
 */
@Slf4j
@RequiredArgsConstructor
@EnableWebFluxSecurity
@Configuration
public class ResourceServerConfig {
    private final AuthorizationManager authorizationManager;
    private final IgnoreUrlsProperties ignoreUrlsProperties;
    private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
                //自定义处理JWT请求头过期或签名错误的结果
                .and().authenticationEntryPoint(authenticationEntryPoint())
                //对白名单路径，直接移除JWT请求头
                .and().addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                //白名单配置
                .pathMatchers(ArrayUtil.toArray(ignoreUrlsProperties.getUrls(), String.class)).permitAll()
                //鉴权管理器配置
                .anyExchange().access(authorizationManager)
                .and().exceptionHandling()
                //处理未授权
                .accessDeniedHandler(accessDeniedHandler())
                //处理未认证
                .authenticationEntryPoint(authenticationEntryPoint())
                .and().csrf().disable()
                .build();
    }

    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(SecurityConstants.ROLE_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(SecurityConstants.AUTHORITY_CLAIM_NAME);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    /**
     * 自定义返回结果：没有权限访问时
     */
    @Bean
    ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                    response.getHeaders().set(HttpHeaders.CACHE_CONTROL, "no-cache");
                    String body = JSONUtil.toJsonStr(R.create(ResultCode.FORBIDDEN, denied.getMessage()));
                    log.info("没有权限访问：{}", body);
                    DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
                });
    }

    /**
     * 自定义返回结果：没有登录或token过期时
     */
    @Bean
    ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, ex) -> Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                    response.getHeaders().set(HttpHeaders.CACHE_CONTROL, "no-cache");
                    String body = JSONUtil.toJsonStr(R.create(ResultCode.UNAUTHORIZED, ex.getMessage()));
                    log.info("没有登录或token过期：{}", body);
                    DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
                });
    }
}
