package com.huiyu.common.core.config;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * jackson全局配置LocalDateTime的序列化时间响应格式
 *
 * @author Naccl
 * @date 2021-12-09
 */
@Configuration
public class JacksonLocalDateTimeSerializerConfig {
    /**
     * 适配自定义序列化和反序列化策略，返回前端指定数据类型的数据
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
        };
    }

    /**
     * 序列化
     * LocalDateTime序列化为毫秒级时间戳
     */
    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                long timestamp = LocalDateTimeUtil.toEpochMilli(value);
                gen.writeNumber(timestamp);
            }
        }
    }

    /**
     * 反序列化
     * 毫秒级时间戳序列化为LocalDateTime
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
            long timestamp = p.getValueAsLong();
            System.out.println("timestamp: " + timestamp);
            if (timestamp > 0) {
                return LocalDateTimeUtil.of(timestamp, ZoneOffset.of("+8"));
            }
            return null;
        }
    }

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     * 接收毫秒级时间戳字符串->LocalDateTime
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return LocalDateTimeUtil.of(Long.parseLong(source), ZoneOffset.of("+8"));
            }
        };
    }

    /**
     * java.util.Date转换器
     * 接收毫秒级时间戳字符串->Date
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                return new Date(new Long(source));
            }
        };
    }
}
