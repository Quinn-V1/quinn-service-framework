package com.quinn.framework.configuration;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.quinn.framework.api.CustomSerializable;
import com.quinn.framework.component.serializer.BaseResultSerializer;
import com.quinn.framework.component.serializer.BatchResultSerializer;
import com.quinn.framework.component.serializer.CustomSerializer;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.DateFormatConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JSON序列化配置类
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
@Configuration
public class JsonSerializerConfiguration {

    @Value("${com.quinn-service.date-pattern:" + DateFormatConstant.DEFAULT_DATE_FORMAT + "}")
    private String datePattern;

    @Value("${com.quinn-service.datetime-pattern:" + DateFormatConstant.DEFAULT_DATE_TIME_FORMAT + "}")
    private String dateTimePattern;

    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimePattern));
    }

    @Bean
    public LocalDateSerializer localDateSerializer() {
        return new LocalDateSerializer(DateTimeFormatter.ofPattern(datePattern));
    }

    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimePattern));
    }

    @Bean
    public LocalDateDeserializer localDateDeserializer() {
        return new LocalDateDeserializer(DateTimeFormatter.ofPattern(datePattern));
    }

    @Bean
    public CustomSerializer customSerializer() {
        return new CustomSerializer();
    }

    @Bean
    public BaseResultSerializer baseResultSerializer() {
        return new BaseResultSerializer();
    }

    @Bean
    public BatchResultSerializer batchResultSerializer() {
        return new BatchResultSerializer();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, localDateTimeSerializer())
                .serializerByType(LocalDate.class, localDateSerializer())
                .deserializerByType(LocalDateTime.class, localDateTimeDeserializer())
                .deserializerByType(LocalDate.class, localDateDeserializer())
                .serializerByType(CustomSerializable.class, customSerializer())
                .serializerByType(BaseResult.class, baseResultSerializer())
                .serializerByType(BatchResultSerializer.class, batchResultSerializer())
                ;
    }

}