package com.huiyu.service.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.text.MessageFormat;

/**
 * RedisKey
 *
 * @author Naccl
 * @date 2022-03-01
 */
@Getter
@ToString
@AllArgsConstructor
public enum RedisKeyEnum {
    RESOURCE_ROLES_MAP("auth:resource_roles_map", -1, "角色资源映射"),
    REQUEST_LIMITER("service:request_limiter:{0}:{1}:{2}", -1, "请求限制 userFlag + method + requestURI"),
    SIGN_IN_MAP("service:sign_in_map:{0}", 60 * 60 * 24 * 2, "每日签到记录 yyyy-MM-dd"),
    ;

    private String key;
    private long expired;
    private String desc;

    public String getFormatKey(Object... args) {
        return MessageFormat.format(this.key, args);
    }
}
