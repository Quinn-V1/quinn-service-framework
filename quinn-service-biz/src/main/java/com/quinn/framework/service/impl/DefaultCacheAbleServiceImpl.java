package com.quinn.framework.service.impl;

import com.quinn.framework.api.entityflag.CacheAble;
import com.quinn.framework.service.CacheAbleService;
import com.quinn.util.base.model.BaseResult;

/**
 * 可缓存服务默认实现类
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
public class DefaultCacheAbleServiceImpl implements CacheAbleService {

    @Override
    public <T extends CacheAble> BaseResult<T> set(String key, T data) {
        // TODO 设置缓存
        return BaseResult.fail();
    }

    @Override
    public <T extends CacheAble> BaseResult<T> set(String key, T data, long expire) {
        // TODO 设置缓存
        return BaseResult.fail();
    }

    @Override
    public <T extends CacheAble> BaseResult<T> get(String key) {
        // TODO 获取缓存
        return BaseResult.fail();
    }

    @Override
    public <T extends CacheAble> BaseResult<T> delete(String key) {
        // TODO 删除缓存
        return BaseResult.fail();
    }

}
