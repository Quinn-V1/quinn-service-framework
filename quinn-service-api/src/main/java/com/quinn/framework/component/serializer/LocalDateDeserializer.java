package com.quinn.framework.component.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.quinn.util.constant.DateConstant;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 时间反序列化器
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        return LocalDate.parse(p.getValueAsString(),
                DateTimeFormatter.ofPattern(DateConstant.DATE_PATTEN_YYYY_MM_DD_SEPARATOR_1));
    }
}