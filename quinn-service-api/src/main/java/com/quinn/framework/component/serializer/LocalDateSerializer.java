package com.quinn.framework.component.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.quinn.util.constant.DateFormatConstant;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 时间序列化器
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public class LocalDateSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value.format(
                DateTimeFormatter.ofPattern(DateFormatConstant.DATE_PATTEN_YYYY_MM_DD_SEPARATOR_1)));
    }
}
