package com.huiyu.service.core.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

import java.util.Locale;

/**
 * @Author wAnG
 * @Date 2023-06-11  01:39
 */
public class IdUtils {

    private static final String TRACE_PREFIX = "Core_service-";

    private static final Snowflake snowflake = IdUtil.getSnowflake(IdUtil.getWorkerId(1, 16), IdUtil.getDataCenterId(16));

    public static String getUuId() {
        return IdUtil.fastSimpleUUID().toUpperCase(Locale.ROOT);
    }

    public static String getTraceId() {
        return TRACE_PREFIX + IdUtils.getUuId();
    }

    public static Long nextSnowflakeId() {
        return snowflake.nextId();
    }
}
