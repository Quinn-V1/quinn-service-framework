package com.quinn.framework.service;

import com.quinn.framework.api.ApplicationSerializer;
import com.quinn.framework.api.cache.CacheCommonService;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.ConfigConstant;
import com.quinn.util.constant.StringConstant;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * Redis缓存操作基础类
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
public abstract class AbstractRedisService implements CacheCommonService {

    public AbstractRedisService(
            RedisTemplate redisTemplate, ApplicationSerializer redisSerializer,
            String name, String keyNamespace
    ) {
        this.redisTemplate = redisTemplate;
        this.redisSerializer = redisSerializer;
        this.name = name;
        this.keyNamespace = StringUtil.isEmpty(keyNamespace) ? ConfigConstant.PACKAGE_PATH_BASE : keyNamespace;

    }

    /**
     * redis模版
     */
    @Resource
    protected RedisTemplate redisTemplate;

    /**
     * 序列化工具类
     */
    protected ApplicationSerializer redisSerializer;

    /**
     * 名称：主要用于监控
     */
    protected String name;

    /**
     * 命名空间
     */
    protected String keyNamespace;


    @Override
    public void expire(String key, long expire) {
        doExpire(wrapperKey(key), expire);
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 放入操作
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间
     */
    public void doSet(String key, Object value, long expire) {
        if (value == null || expire == 0) {
            delete(key);
            return;
        }

        String wKey = wrapperKey(key);
        redisTemplate.execute((RedisCallback) redisConnection -> {
            byte[] val = redisSerializer.serialize(value);
            byte[] keyBytes = StringUtil.getBytes(wKey);

            if (val != null) {
                redisConnection.set(keyBytes, val);
                if (expire > 0) {
                    redisConnection.expire(keyBytes, expire);
                }
            }
            return 1L;
        });
    }

    /**
     * 取值，实现方法
     *
     * @param key 键
     * @param <T> 返回泛型
     * @return 值
     */
    protected <T> T doGet(String key) {
        return (T) redisTemplate.execute((RedisCallback) redisConnection -> {
            byte[] value = redisConnection.get(StringUtil.getBytes(wrapperKey(key)));
            if (value != null) {
                T realValue = (T) redisSerializer.deserialize(value);
                return realValue;
            }
            return null;
        });
    }

    /**
     * 取值，实现方法
     *
     * @param key 键
     * @param tpl 指定类对象
     * @param <T> 返回泛型
     * @return 值
     */
    protected <T> T doGet(String key, Class<T> tpl) {
        return (T) redisTemplate.execute((RedisCallback) redisConnection -> {
            byte[] value = redisConnection.get(StringUtil.getBytes(wrapperKey(key)));
            if (value != null) {
                T realValue = redisSerializer.deserialize(value, tpl);
                return realValue;
            }
            return null;
        });
    }

    /**
     * 删除动作的具体实现
     *
     * @param keys 键
     */
    protected void doDelete(String... keys) {
        redisTemplate.execute((RedisCallback) connection -> {
            for (int i = 0; i < keys.length; i++) {
                connection.del(StringUtil.getBytes(wrapperKey(keys[i])));
            }
            return 1L;
        });
    }

    /**
     * 设置过期时间的具体实现
     *
     * @param key
     * @param expire
     */
    protected void doExpire(String key, long expire) {
        if (expire <= 0) {
            doDelete(key);
            return;
        }

        redisTemplate.execute((RedisCallback) connection -> {
            connection.expire(StringUtil.getBytes(wrapperKey(key)), expire);
            return 1L;
        });
    }

    /**
     * 键加工
     *
     * @param key 原始键
     * @return 加工键
     */
    protected String wrapperKey(String key) {
        return name + StringConstant.CHAR_COLON + keyNamespace + key;
    }

    /**
     * 键清理
     *
     * @param key 加工键
     * @return 原始键
     */
    protected String cleanKey(String key) {
        return key.substring(name.length() + keyNamespace.length() + 1);
    }

}
