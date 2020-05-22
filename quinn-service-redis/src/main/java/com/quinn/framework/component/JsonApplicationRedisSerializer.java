package com.quinn.framework.component;

import com.quinn.framework.component.serializer.JsonApplicationSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 应用基本Redis序列化器
 *
 * @author Qunhua.Liao
 * @since 2020-04-06
 */
public class JsonApplicationRedisSerializer extends JsonApplicationSerializer implements RedisSerializer {

}
