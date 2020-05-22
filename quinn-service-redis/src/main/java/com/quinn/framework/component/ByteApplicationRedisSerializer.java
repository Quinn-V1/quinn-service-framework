package com.quinn.framework.component;

import com.quinn.framework.component.serializer.ByteApplicationSerializer;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.StreamUtil;
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
public class ByteApplicationRedisSerializer extends ByteApplicationSerializer implements RedisSerializer {

}
