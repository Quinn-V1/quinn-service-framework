package com.quinn.framework.component;

import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.util.StreamUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 对象序列化器
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public class BaseObjectRedisSerializer implements RedisSerializer {

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
    public Object deserialize(@Nullable byte[] data) throws SerializationException {
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

}
