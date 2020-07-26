package com.quinn.framework.component.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.handler.MultiMessageResolver;
import com.quinn.util.base.model.BatchResult;
import com.quinn.util.constant.CharConstant;
import com.quinn.util.constant.enums.NotifyEnum;

import java.io.IOException;
import java.util.List;

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

        gen.writeStartObject();
        gen.writeBooleanField("success", value.isSuccess());
        gen.writeNumberField("level", value.getLevel());

        BatchResult.BatchItems data = value.getData();
        if (data != null) {
            if (data.allSuccess()) {
                gen.writeStringField("message",
                        MultiMessageResolver.resolve(SessionUtil.getLocale(), NotifyEnum.ALL_DATA_PASSED.name()));
            } else {
                StringBuilder query = new StringBuilder();
                List<BatchResult.BatchItem> failItems = data.getFailItems();
                for (BatchResult.BatchItem item : failItems) {
                    query.append(MultiMessageResolver.resolveMessageProp(
                            SessionUtil.getLocale(), item.getMessageProp()))
                            .append(CharConstant.LINE_BREAK);
                }
            }
        } else {
            gen.writeStringField("message", value.getMessage());
        }

        gen.writeEndObject();
    }

}
