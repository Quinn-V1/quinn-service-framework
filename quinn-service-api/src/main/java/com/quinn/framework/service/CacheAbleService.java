package com.quinn.framework.service;

import com.quinn.framework.api.entityflag.CacheAble;
import com.quinn.util.base.model.BaseResult;

/**
 * 缓存业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-03-28
 */
public interface CacheAbleService {

    /**
     * 设置缓存
     *
     * @param key   键
     * @param data  值
     * @param <T>   值泛型
     * @return      旧值
     */
    <T extends CacheAble> BaseResult<T> set(String key, T data);

    /**
     * 设置缓存
     *
     * @param key       键
     * @param data      值
     * @param expire    过期时间
     * @param <T>       值泛型
     * @return          旧值
     */
    <T extends CacheAble> BaseResult<T> set(String key, T data, long expire);

    /**
     * 取缓存
     *
     * @param key   键
     * @param <T>   值泛型
     * @return      旧值
     */
    <T extends CacheAble> BaseResult<T> get(String key);

    /**
     * 删除存
     *
     * @param key   键
     * @param <T>   值泛型
     * @return      旧值
     */
    <T extends CacheAble> BaseResult<T> delete(String key);

}
