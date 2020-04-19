package com.quinn.framework.service;

import com.quinn.framework.api.entityflag.ParameterAble;
import com.quinn.util.base.model.BaseResult;

/**
 * 参数业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-03-28
 */
public interface ParameterAbleService {

    /**
     * 保存参数
     *
     * @param data 值
     * @param <T>  值泛型
     * @return 旧值
     */
    <T extends ParameterAble> BaseResult<T> save(T data);

    /**
     * 更新参数
     *
     * @param data 值
     * @param <T>  值泛型
     * @return 旧值
     */
    <T extends ParameterAble> BaseResult<T> update(T data);

    /**
     * 新增或者更新参数
     *
     * @param data 值
     * @param <T>  值泛型
     * @return 旧值
     */
    <T extends ParameterAble> BaseResult<T> saveOrUpdate(T data);

    /**
     * 删参数
     *
     * @param data 值
     * @param <T>  值泛型
     * @return 旧值
     */
    <T extends ParameterAble> BaseResult<T> delete(T data);

    /**
     * 删参数
     *
     * @param key 键
     * @param <T> 值泛型
     * @return 旧值
     */
    <T extends ParameterAble> BaseResult<T> deleteByKey(String key);

    /**
     * 删参数
     *
     * @param key 键
     * @param <T> 值泛型
     * @return 旧值
     */
    <T extends ParameterAble> BaseResult<T> selectByKey(String key);

}
