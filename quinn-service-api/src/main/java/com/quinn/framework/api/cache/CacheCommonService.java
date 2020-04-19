package com.quinn.framework.api.cache;

import java.util.Collection;
import java.util.Set;

/**
 * 缓存通用接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
public interface CacheCommonService extends CacheBaseService {

    /**
     * 设置值：不限时间
     *
     * @param key 键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 设置值：限制过期事件
     *
     * @param key 键
     * @param value 值
     * @param expire 有效时间（秒）
     */
    void set(String key, Object value, long expire);

    /**
     * 获取值
     *
     * @param key 键
     * @return 值
     */
    <T> T get(String key);

    /**
     * 获取值，并反序列化为特定对象实例
     *
     * @param key 键
     * @param tpl 指定类
     * @param <T> 返回泛型
     * @return 值
     */
    <T> T get(String key, Class<T> tpl);

    /**
     * 获取缓存大小
     * @param pattern   键样式
     *
     * @return      符合样式的键的数量
     */
    int size(String pattern);

    /**
     * 获取缓存大小
     *
     * @return  缓存键总数
     */
    int size();

    /**
     * 返回满足特定正则表达式的所有键
     *
     * @param pattern 正则表达式
     * @return 键
     */
    Set<String> keys(String pattern);

    /**
     * 获取所有的缓存键
     *
     * @return
     */
    Set<String> keys();

    /**
     * 获取所有值
     * @param pattern   键样式
     *
     * @return  符合键样式对应的所有的值
     */
    <T> Collection<T> values(String pattern);

    /**
     * 获取所有值
     *
     * @return  所有人值
     */
    <T> Collection<T> values();

    /**
     * 判断某个键是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 删除一个键
     *
     * @param key
     * @return
     */
    void delete(String key);

    /**
     * 删除一个或者多个键对应的值
     *
     * @param keys 键
     */
    void delete(String... keys);

    /**
     * 清除所有键
     *
     * @param pattern   键样式
     */
    void clear(String pattern);

    /**
     * 清除所有键
     */
    void clear();

}
