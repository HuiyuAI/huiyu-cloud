package com.huiyu.service.core.utils;

import java.util.Locale;
import java.util.UUID;

/**
 * @Author wAnG
 * @Date 2023-06-11  01:39
 */
public class IdUtils {

    private static final String TRACE_PREFIX = "Core_service-";

    public static String getUuId(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase(Locale.ROOT);
    }

    public static String getTraceId(){
        return TRACE_PREFIX + UUID.randomUUID().toString().replace("-","").toUpperCase(Locale.ROOT);
    }

}
