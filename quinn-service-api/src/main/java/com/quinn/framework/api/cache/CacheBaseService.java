package com.quinn.framework.api.cache;

/**
 * 缓存基本接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
public interface CacheBaseService {

    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 设置键的过期事件
     *
     * @param key 键
     * @param expire 过期时间（秒）
     */
    void expire(String key, long expire);

}
