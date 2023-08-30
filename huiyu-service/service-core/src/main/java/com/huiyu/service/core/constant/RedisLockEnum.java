package com.huiyu.service.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.text.MessageFormat;

/**
 * RedisLock
 *
 * @author Naccl
 * @date 2023-08-29
 */
@Getter
@ToString
@AllArgsConstructor
public enum RedisLockEnum {
    UPDATE_POINT("service:lock:update_point:{0}", "用户积分修改"),
    ;

    private String key;
    private String desc;

    public String getFormatKey(Object... args) {
        return MessageFormat.format(this.key, args);
    }
}
