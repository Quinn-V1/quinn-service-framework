package com.quinn.framework.api.cache;

import com.quinn.framework.api.ApplicationSerializer;

/**
 * 缓存管理器
 *
 * @author Qunhua.Liao
 * @since 2020-05-24
 */
public interface CacheServiceManager<T extends CacheAllService> {

    /**
     * 根据名称他命名空间生成缓存服务
     *
     * @param name      名称
     * @param nameSpace 命名空间
     * @return 缓存服务
     */
    T getCacheService(String name, String nameSpace);

    /**
     * 根据名称他命名空间生成缓存服务
     *
     * @param serializer 序列化器
     * @param name       名称
     * @param nameSpace  命名空间
     * @return 缓存服务
     */
    T getCacheService(ApplicationSerializer serializer, String name, String nameSpace);

    /**
     * 刷新机制
     *
     * @param name      缓存名
     * @param nameSpace 命名空间
     * @param key       缓存键
     * @param value     韩村值
     */
    default void refresh(String name, String nameSpace, String key, Object value) {
        CacheAllService cacheService = getCacheService(name, nameSpace);
        if (cacheService == null) {
            return;
        }

        if (value == null) {
            cacheService.delete(key);
        } else {
            cacheService.set(key, value);
        }
    }

}
