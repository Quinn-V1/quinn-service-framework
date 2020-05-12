package com.quinn.framework.model;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quinn.framework.component.serializer.*;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ObjectMapper工厂
 *
 * @author Qunhua.Liao
 * @since 2020-04-05
 */
public class ObjectMapperFactory {

    /**
     * 默认Json解析器
     *
     * @return Json解析器
     */
    public static ObjectMapper defaultObjectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());

        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(javaTimeModule)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToEnable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)
                .build();

        return objectMapper;
    }

}
