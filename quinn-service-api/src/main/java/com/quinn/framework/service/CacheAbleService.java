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
     * @return      旧值
     */
    BaseResult set(String key, Object data);

    /**
     * 设置缓存
     *
     * @param key       键
     * @param data      值
     * @param expire    过期时间
     * @return          旧值
     */
    BaseResult set(String key, Object data, long expire);

    /**
     * 取缓存
     *
     * @param key   键
     * @param clazz   返回值类型
     * @return      旧值
     */
    BaseResult get(String key, Class clazz);

    /**
     * 删除存
     *
     * @param key   键
     * @return      旧值
     */
    BaseResult delete(String key);

}
