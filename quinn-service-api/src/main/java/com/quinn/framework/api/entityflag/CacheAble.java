package com.quinn.framework.api.entityflag;

/**
 * 可缓存标识
 *
 * @author Qunhua.Liao
 * @since 2020-03-28
 */
public interface CacheAble {

    /**
     * 获取缓存键
     *
     * @return
     */
    String cacheKey();

}
