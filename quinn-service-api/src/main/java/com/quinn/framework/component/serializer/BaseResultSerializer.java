package com.quinn.framework.component.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.quinn.util.base.model.BaseResult;

import java.io.IOException;

/**
 * 基本结果序列化器
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public class BaseResultSerializer extends JsonSerializer<BaseResult> {

    @Override
    public void serialize(BaseResult value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeBooleanField("success", value.isSuccess());
        gen.writeNumberField("level", value.getLevel());
        gen.writeStringField("message", value.getMessage());

        if (value.getData() != null) {
            gen.writeObjectField("data", value.getData());
        }

        gen.writeEndObject();
    }

}
