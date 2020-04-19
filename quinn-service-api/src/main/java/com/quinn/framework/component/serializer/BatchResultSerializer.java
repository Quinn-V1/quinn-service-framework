package com.quinn.framework.component.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.quinn.util.base.model.BatchResult;

import java.io.IOException;

/**
 * 批处理结果结果序列化器
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public class BatchResultSerializer extends JsonSerializer<BatchResult> {

    @Override
    public void serialize(BatchResult value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeObject(value);
    }

}
