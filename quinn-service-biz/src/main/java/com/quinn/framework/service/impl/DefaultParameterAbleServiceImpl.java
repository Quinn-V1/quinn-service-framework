package com.quinn.framework.service.impl;

import com.quinn.framework.api.entityflag.ParameterAble;
import com.quinn.framework.service.ParameterAbleService;
import com.quinn.util.base.model.BaseResult;

/**
 * 可缓存服务默认实现类
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
public class DefaultParameterAbleServiceImpl implements ParameterAbleService {

    @Override
    public <T extends ParameterAble> BaseResult<T> save(T data) {
        // TODO 保存扩展参数
        return BaseResult.fail();
    }

    @Override
    public <T extends ParameterAble> BaseResult<T> update(T data) {
        // TODO 更新扩展参数
        return BaseResult.fail();
    }

    @Override
    public <T extends ParameterAble> BaseResult<T> saveOrUpdate(T data) {
        // TODO 保存或者更新扩展参数
        return BaseResult.fail();
    }

    @Override
    public <T extends ParameterAble> BaseResult<T> delete(T data) {
        // TODO 删除指定扩展参数
        return BaseResult.fail();
    }

    @Override
    public <T extends ParameterAble> BaseResult<T> deleteByKey(String key) {
        // TODO 根据参数业务主键删除扩展参数
        return BaseResult.fail();
    }

    @Override
    public <T extends ParameterAble> BaseResult<T> selectByKey(String key) {
        // TODO 查询指定扩展参数
        return BaseResult.fail();
    }
}
