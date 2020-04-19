package com.quinn.framework.component.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quinn.framework.api.ApplicationSerializer;
import com.quinn.framework.model.ObjectMapperFactory;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.util.StreamUtil;
import com.quinn.util.base.util.StringUtil;
import com.quinn.util.constant.StringConstant;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 基础Json序列化对象
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public class JsonApplicationSerializer implements ApplicationSerializer {

    private static final String REDIS_SERIALIZER_PROPERTY = "className";

    private ObjectMapper objectMapper;

    public JsonApplicationSerializer() {
        objectMapper = ObjectMapperFactory.defaultObjectMapper();
    }

    public JsonApplicationSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(Object o) {
        if (o == null) {
            return null;
        }

        Writer writer = null;
        ByteArrayOutputStream bos = null;
        try {
            if (BaseConverter.getInstance(o.getClass()) != null) {
                return BaseConverter.staticToString(o).getBytes(StringConstant.SYSTEM_DEFAULT_CHARSET);
            }

            bos = new ByteArrayOutputStream();
            writer = new OutputStreamWriter(bos, StringConstant.SYSTEM_DEFAULT_CHARSET);
            JSONObject jsonObject = (JSONObject) JSON.toJSON(o);
            jsonObject.put(REDIS_SERIALIZER_PROPERTY, o.getClass().getName());
            objectMapper.writeValue(writer, jsonObject);
            byte[] data = bos.toByteArray();

            return data;
        } catch (Exception e) {
            throw new BaseBusinessException().buildParam("", 1, 1).exception();
        } finally {
            StreamUtil.closeQuietly(bos);
            StreamUtil.closeQuietly(writer);
        }
    }

    @Override
    public Object deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        try {
            String s = new String(data, StringConstant.SYSTEM_DEFAULT_CHARSET);
            if (!s.startsWith(StringConstant.CHAR_OPEN_BRACE) || !s.endsWith(StringConstant.CHAR_CLOSE_BRACE)) {
                return s;
            }

            JSONObject object = objectMapper.readValue(s, JSONObject.class);
            String className = object.getString(REDIS_SERIALIZER_PROPERTY);
            if (StringUtil.isEmpty(className)) {
                return object;
            }

            return object.toJavaObject(Class.forName(className));
        } catch (Exception e) {
            throw new BaseBusinessException().buildParam("", 1, 1).exception();
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> tpl) {
        if (data == null || data.length == 0) {
            return null;
        }


        try {
            String s = new String(data, StringConstant.SYSTEM_DEFAULT_CHARSET);
            if (BaseConverter.isPrimitive(tpl)) {
                return BaseConverter.staticConvert(s, tpl);
            }

            return (T) objectMapper.readValue(s, TypeFactory.rawClass(tpl));
        } catch (Exception e) {
            throw new BaseBusinessException().buildParam("", 1, 1).exception();
        }
    }

    @Override
    public <T> T deserializeFromStr(String json, Class<T> tpl) {
        if (json == null || "".equals(json)) {
            return null;
        }

        if (BaseConverter.isPrimitive(tpl)) {
            return BaseConverter.staticConvert(json, tpl);
        }

        try {
            return deserialize(json.getBytes(StringConstant.SYSTEM_DEFAULT_CHARSET), tpl);
        } catch (UnsupportedEncodingException e) {
            throw new BaseBusinessException().buildParam("", 1, 1).exception();
        }
    }

    @Override
    public String serializeToStr(Object o) {
        try {
            return new String(this.serialize(o), StringConstant.SYSTEM_DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new BaseBusinessException().buildParam("", 1, 1).exception();
        }
    }

}
