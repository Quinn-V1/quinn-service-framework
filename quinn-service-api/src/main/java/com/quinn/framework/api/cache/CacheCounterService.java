package com.quinn.framework.api.cache;

/**
 * 缓存计接口
 *
 * @author Qunhua.Liao
 * @since   2020-04-02
 */
public interface CacheCounterService extends CacheBaseService {

    /**
     * 缓存计数器，获取当前值
     *
     * @param key 计数器
     * @return 当前值
     */
    long getCurrent(String key);

    /**
     * 缓存计数器，值增加1
     *
     * @param key 计数器
     * @return 增加后的值
     */
    long increase(String key);

    /**
     * 分布式缓存计数器，值减少1
     *
     * @param key 计数器
     * @return 减少后的值
     */
    long decrease(String key);

    /**
     * 缓存计数器，值增加
     *
     * @param key  计数器
     * @param step 增加值
     * @return 增加后的值
     */
    long increaseBy(String key, int step);

    /**
     * 缓存计数器，值增加
     *
     * @param key  计数器
     * @param step 增加值
     * @param expire 过期时间
     * @return 增加后的值
     */
    long increaseByEx(String key, int step, long expire);

    /**
     * 分布式缓存计数器，值减少
     *
     * @param key   计数器
     * @param step  减少值
     * @return 减少后的值
     */
    long decreaseBy(String key, int step);

    /**
     * 分布式缓存计数器，值重置
     *
     * @param key 计数器
     * @param val 重置值
     */
    void reset(String key, long val);

    /**
     * 分布式缓存计数器，删除
     *
     * @param key 计数器
     */
    void delete(String key);
}
