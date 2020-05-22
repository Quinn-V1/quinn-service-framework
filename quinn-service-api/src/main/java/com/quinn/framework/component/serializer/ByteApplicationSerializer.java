package com.quinn.framework.component.serializer;

import com.quinn.framework.api.ApplicationSerializer;
import com.quinn.util.base.StreamUtil;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.constant.StringConstant;
import lombok.SneakyThrows;
import org.springframework.lang.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 字节序列化
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
public class ByteApplicationSerializer implements ApplicationSerializer {

    @Nullable
    @Override
    public byte[] serialize(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new BaseBusinessException().buildParam("", 1, 2).exception();
        } finally {
            StreamUtil.closeQuietly(bos, oos);
        }

    }

    @Nullable
    @Override
    public Object deserialize(@Nullable byte[] data) {
        if (data == null) {
            return null;
        }

        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (Exception e) {
            throw new BaseBusinessException().buildParam("", 1, 2).exception();
        } finally {
            StreamUtil.closeQuietly(bis, ois);
        }
    }

    @Override
    @SneakyThrows
    public String serializeToStr(Object o) {
        byte[] data = serialize(o);
        return new String(data, StringConstant.SYSTEM_DEFAULT_CHARSET);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> tpl) {
        return (T) deserialize(data);
    }

    @Override
    @SneakyThrows
    public <T> T deserializeFromStr(String data, Class<T> tpl) {
        byte[] bytes = data.getBytes(StringConstant.SYSTEM_DEFAULT_CHARSET);
        return deserialize(bytes, tpl);
    }
}
