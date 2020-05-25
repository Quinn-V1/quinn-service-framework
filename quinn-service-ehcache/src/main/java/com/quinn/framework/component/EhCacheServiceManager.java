package com.quinn.framework.component;

import com.quinn.framework.api.ApplicationSerializer;
import com.quinn.framework.api.cache.CacheServiceManager;
import com.quinn.framework.service.impl.EhCacheAllServiceImpl;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EhCache 缓存服务管理器
 *
 * @author Qunhua.Liao
 * @since 2020-05-24
 */
public class EhCacheServiceManager implements CacheServiceManager<EhCacheAllServiceImpl> {

    private Map<String, EhCacheAllServiceImpl> ehCacheAllServiceMap = new ConcurrentHashMap<>();

    private CacheManager ehCacheManager;

    public EhCacheServiceManager(CacheManager ehCacheManager) {
        this.ehCacheManager = ehCacheManager;
    }

    @Override
    public EhCacheAllServiceImpl getCacheService(String name, String nameSpace) {
        String key = nameSpace + name;
        EhCacheAllServiceImpl ehCacheAllService = ehCacheAllServiceMap.get(key);
        if (ehCacheAllService != null) {
            return ehCacheAllService;
        }

        synchronized (ehCacheAllServiceMap) {
            Cache cache = ehCacheManager.getCache(name);
            if (cache == null) {
                ehCacheManager.addCache(name);
                cache = ehCacheManager.getCache(name);
            }
            ehCacheAllService = new EhCacheAllServiceImpl(cache, key);
            ehCacheAllServiceMap.put(key, ehCacheAllService);
        }

        return ehCacheAllService;
    }

    @Override
    public EhCacheAllServiceImpl getCacheService(ApplicationSerializer serializer, String name, String nameSpace) {
        return getCacheService(name, nameSpace);
    }

}
