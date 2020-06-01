package com.quinn.framework.component.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.ApplicationSerializer;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.constant.StringConstant;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 基础Json序列化对象
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public class JsonApplicationSerializer implements ApplicationSerializer {

    /**
     * 序列化对象类名参数名
     */
    private static final String REDIS_SERIALIZER_PROPERTY = "className";

    @Override
    public byte[] serialize(Object o) {
        if (o == null) {
            return null;
        }

        try {
            if (BaseConverter.getInstance(o.getClass()) != null) {
                return BaseConverter.staticToString(o).getBytes(StringConstant.SYSTEM_DEFAULT_CHARSET);
            }

            JSONObject jsonObject = (JSONObject) JSON.toJSON(o);
            jsonObject.put(REDIS_SERIALIZER_PROPERTY, o.getClass().getName());

            return jsonObject.toJSONString().getBytes(StringConstant.SYSTEM_DEFAULT_CHARSET);
        } catch (Exception e) {
            // FIXME
            throw new BaseBusinessException().buildParam("", 1, 1)
                    .exception();
        }
    }

    @Override
    public Object deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        String value;
        try {
            value = new String(data, StringConstant.SYSTEM_DEFAULT_CHARSET);
            if (!value.startsWith(StringConstant.CHAR_OPEN_BRACE) || !value.endsWith(StringConstant.CHAR_CLOSE_BRACE)) {
                return value;
            }
        } catch (Exception e) {
            // FIXME
            throw new BaseBusinessException()
                    .buildParam("deserialize exception", 1, 1)
                    .exception();
        }

        JSONObject object = JSONObject.parseObject(value);
        String className = object.getString(REDIS_SERIALIZER_PROPERTY);
        if (StringUtil.isEmpty(className)) {
            return object;
        }

        try {
            return object.toJavaObject(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return object;
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
            } else if (Map.class.isAssignableFrom(tpl)) {
                return (T) JSONObject.parseObject(s);
            } else if (JSONArray.class == tpl) {
                return (T) JSONArray.parseArray(s);
            }

            return JSONObject.parseObject(s, tpl);
        } catch (Exception e) {
            // FIXME
            throw new BaseBusinessException()
                    .buildParam("deserialize exception", 1, 1)
                    .exception();
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
            // FIXME
            throw new BaseBusinessException().buildParam("deserialize exception", 1, 1).exception();
        }
    }

    @Override
    public String serializeToStr(Object o) {
        try {
            return new String(this.serialize(o), StringConstant.SYSTEM_DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            // FIXME
            throw new BaseBusinessException().buildParam("deserialize exception", 1, 1).exception();
        }
    }

}
