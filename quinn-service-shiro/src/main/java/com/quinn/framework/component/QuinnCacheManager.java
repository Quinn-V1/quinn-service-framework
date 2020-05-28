package com.quinn.framework.component;

import com.quinn.framework.api.cache.CacheAllService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 简易缓存管理器
 *
 * @author Quinn.Liao
 * @since 2020-05-22
 */
public class QuinnCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    private CacheAllService cacheAllService;

    private static final int DEFAULT_EXPIRE = 1800;

    private int expire = DEFAULT_EXPIRE;

    public static final String DEFAULT_CACHE_KEY_PREFIX = "shiro:cache:";

    private String keyPrefix = DEFAULT_CACHE_KEY_PREFIX;

    public static final String DEFAULT_PRINCIPAL_ID_FIELD_NAME = "principal";

    private String principalIdFieldName = DEFAULT_PRINCIPAL_ID_FIELD_NAME;

    public QuinnCacheManager(CacheAllService cacheAllService) {
        this.cacheAllService = cacheAllService;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        Cache cache = caches.get(name);

        if (cache == null) {
            cache = new QuinnCache(cacheAllService, keyPrefix + name + ":", expire, principalIdFieldName);
            caches.put(name, cache);
        }
        return cache;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getPrincipalIdFieldName() {
        return principalIdFieldName;
    }

    public void setPrincipalIdFieldName(String principalIdFieldName) {
        this.principalIdFieldName = principalIdFieldName;
    }
}