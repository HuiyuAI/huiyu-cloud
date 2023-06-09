package com.huiyu.auth.common.enums;

import lombok.Getter;

/**
 * 密文编码器类型枚举
 *
 * @author Naccl
 * @date 2022-03-07
 */
public enum PasswordEncoderTypeEnum {
    BCRYPT("{bcrypt}", "BCRYPT加密"),
    NOOP("{noop}", "无加密明文"),
    ;

    @Getter
    private String prefix;

    PasswordEncoderTypeEnum(String prefix, String desc) {
        this.prefix = prefix;
    }

}
