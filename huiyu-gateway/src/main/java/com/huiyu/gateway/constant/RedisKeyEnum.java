package com.huiyu.gateway.constant;

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
    ;

    private String key;
    private long expired;
    private String desc;

    public String getFormatKey(Object... args) {
        return MessageFormat.format(this.key, args);
    }
}
