package com.huiyu.service.core.Hconfig.config;

import com.huiyu.service.core.Hconfig.base.HConfigOnChange;
import com.huiyu.service.core.Hconfig.base.HConfigType;
import com.huiyu.service.core.Hconfig.base.annotation.HConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * @author wAnG
 * @Date 2023-06-28  01:02
 */

@Slf4j
@HConfig(dataId = "hotFile", suffix = HConfigType.PROPERTIES)
public class HotFileConfig implements HConfigOnChange<Map<String, String>> {

    private Map<String, String> map;

    @Override
    public void onChange(Map<String, String> map) {
        this.map = map;
    }

    public String getString(String key, String defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    public Integer getInt(String key, Integer defaultValue) {
        String value = map.get(key);
        if (Objects.nonNull(value)) {
            try {
                return Integer.valueOf(value);
            } catch (Exception e) {
                log.error("读取配置错误", e);
            }
        }
        return defaultValue;
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        String value = map.get(key);
        if (Objects.nonNull(value)) {
            try {
                return Boolean.valueOf(value);
            } catch (Exception e) {
                log.error("读取配置错误", e);
            }
        }
        return defaultValue;
    }

    public Integer getExecStrategy() {
        return this.getInt("exec_strategy", 1);
    }

    public Integer getSignInIntegral() {
        return this.getInt("sign_in_integral", 1);
    }

    public Integer getInviteIntegral() {
        return this.getInt("inviteIntegral", 10);
    }
}
