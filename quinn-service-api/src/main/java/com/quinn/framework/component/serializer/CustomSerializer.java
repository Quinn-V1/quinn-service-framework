package com.quinn.framework.component.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.quinn.framework.api.CustomSerializable;

import java.io.IOException;

/**
 * 自定义序列化工具
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
public class CustomSerializer  extends JsonSerializer<CustomSerializable> {

    @Override
    public void serialize(CustomSerializable value, JsonGenerator generator, SerializerProvider serializers)
            throws IOException {

        if (value != null) {
            generator.writeObject(value.customSerialize());
        }
    }
}
