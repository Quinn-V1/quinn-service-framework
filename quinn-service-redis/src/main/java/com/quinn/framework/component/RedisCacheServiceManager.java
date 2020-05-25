package com.quinn.framework.component;

import com.quinn.framework.api.ApplicationSerializer;
import com.quinn.framework.api.cache.CacheServiceManager;
import com.quinn.framework.service.RedisAllServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis缓存管理器
 *
 * @author Qunhua.Liao
 * @since 2020-05-24
 */
public class RedisCacheServiceManager implements CacheServiceManager<RedisAllServiceImpl> {

    private Map<String, RedisAllServiceImpl> redisAllServiceMap = new ConcurrentHashMap<>();

    /**
     * Redis缓存管理器
     *
     * @param redisTemplate Redis模板
     * @param serializer    序列化对象
     */
    public RedisCacheServiceManager(RedisTemplate redisTemplate, ApplicationSerializer serializer) {
        this.redisTemplate = redisTemplate;
        this.serializer = serializer;
    }

    /**
     * Redis模板
     */
    private RedisTemplate redisTemplate;

    /**
     * 序列化对象
     */
    private ApplicationSerializer serializer;

    @Override
    public RedisAllServiceImpl getCacheService(String name, String nameSpace) {
        RedisAllServiceImpl redisAllService = redisAllServiceMap.get(name);
        if (redisAllService != null) {
            return redisAllService;
        }

        synchronized (redisAllServiceMap) {
            redisAllService = new RedisAllServiceImpl(redisTemplate, serializer, name, nameSpace);
            redisAllServiceMap.put(name, redisAllService);
        }

        return redisAllService;
    }

    @Override
    public RedisAllServiceImpl getCacheService(ApplicationSerializer serializer, String name, String nameSpace) {
        RedisAllServiceImpl redisAllService = redisAllServiceMap.get(name);
        if (redisAllService != null) {
            return redisAllService;
        }

        synchronized (redisAllServiceMap) {
            redisAllService = new RedisAllServiceImpl(redisTemplate, serializer, name, nameSpace);
            redisAllServiceMap.put(name, redisAllService);
        }
        return redisAllService;
    }

}
