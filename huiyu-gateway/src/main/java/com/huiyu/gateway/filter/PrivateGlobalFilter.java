package com.huiyu.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.huiyu.common.core.result.R;
import com.huiyu.common.core.result.ResultCode;

import java.nio.charset.StandardCharsets;

/**
 * 内部接口禁止外部访问
 *
 * @author Naccl
 * @date 2022-04-14
 */
@Slf4j
@Component
public class PrivateGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (isPrivate(path)) {
            log.warn("禁止访问内部接口：{}", path);
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.getHeaders().set(HttpHeaders.CACHE_CONTROL, "no-cache");
            String body = JSONUtil.toJsonStr(R.create(ResultCode.FORBIDDEN));
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
        }
        return chain.filter(exchange);
    }

    /**
     * 判断是否是内部接口
     *
     * @param path 请求路径
     * @return 是否是内部接口
     */
    private boolean isPrivate(String path) {
        return StrUtil.isNotBlank(path) && path.indexOf("/private") != -1;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
