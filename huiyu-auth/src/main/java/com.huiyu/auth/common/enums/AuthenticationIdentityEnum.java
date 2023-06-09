package com.huiyu.auth.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 认证身份标识枚举
 *
 * @author Naccl
 * @date 2022-03-07
 */
public enum AuthenticationIdentityEnum {
    USERNAME("username", "用户名"),
    OPENID("openid", "开放式认证系统唯一身份标识"),
    ;

    @Getter
    private String value;

    @Getter
    private String label;

    AuthenticationIdentityEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static AuthenticationIdentityEnum getByValue(String value) {
        Optional<AuthenticationIdentityEnum> optional = Arrays.stream(AuthenticationIdentityEnum.values())
                .filter(p -> p.getValue().equals(value))
                .findFirst();
        return optional.isPresent() ? optional.get() : null;
    }

}
