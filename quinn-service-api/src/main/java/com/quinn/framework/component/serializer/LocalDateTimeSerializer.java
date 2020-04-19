package com.quinn.framework.component.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.quinn.util.constant.DateFormatConstant;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间日期序列化
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value.format(
                DateTimeFormatter.ofPattern(DateFormatConstant.DATE_PATTEN_YYYY_MM_DD_HH_MM_SS_SEPARATOR_1)));
    }
}
