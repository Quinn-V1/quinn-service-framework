package com.quinn.framework.component.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.quinn.util.constant.DateFormatConstant;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间日期反序列化
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        return LocalDateTime.parse(p.getValueAsString(),
                DateTimeFormatter.ofPattern(DateFormatConstant.DATE_PATTEN_YYYY_MM_DD_HH_MM_SS_SEPARATOR_1));
    }
}
