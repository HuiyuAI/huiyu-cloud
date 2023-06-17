package com.huiyu.common.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Naccl
 * @date 2020-11-07
 */
@Slf4j
public class JacksonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJsonStr(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JacksonUtils.toJsonStr() error: ", e);
            return "";
        }
    }

    public static <T> T toBean(String jsonString, Class<T> valueType) {
        try {
            return objectMapper.readValue(jsonString, valueType);
        } catch (IOException e) {
            log.error("JacksonUtils.toBean() error: ", e);
            return null;
        }
    }

    public static <T> T toBean(String jsonString, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (IOException e) {
            log.error("JacksonUtils.toBean() error: ", e);
            return null;
        }
    }

    public static <T> T toBean(InputStream src, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (IOException e) {
            log.error("JacksonUtils.toBean() error: ", e);
            return null;
        }
    }

    public static <T> T toBean(Object fromValue, Class<T> toValueType) {
        return objectMapper.convertValue(fromValue, toValueType);
    }
}
