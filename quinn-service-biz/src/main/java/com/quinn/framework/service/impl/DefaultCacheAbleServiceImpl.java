package com.quinn.framework.service.impl;

import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.framework.api.entityflag.CacheAble;
import com.quinn.framework.service.CacheAbleService;
import com.quinn.util.base.model.BaseResult;

import javax.annotation.Resource;

/**
 * 可缓存服务默认实现类
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
public class DefaultCacheAbleServiceImpl implements CacheAbleService {

    @Resource
    private CacheAllService cacheAllService;

    @Override
    public BaseResult set(String key, Object data) {
        cacheAllService.set(key, data);
        return BaseResult.SUCCESS;
    }

    @Override
    public BaseResult set(String key, Object data, long expire) {
        cacheAllService.set(key, data, expire);
        return BaseResult.SUCCESS;
    }

    @Override
    public BaseResult get(String key, Class clazz) {
        Object t = cacheAllService.get(key, clazz);
        if (t == null) {
            return BaseResult.fail();
        }
        return BaseResult.success(t);
    }

    @Override
    public BaseResult delete(String key) {
        cacheAllService.delete(key);
        return BaseResult.SUCCESS;
    }

}
