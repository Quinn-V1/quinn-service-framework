package com.quinn.framework.api;

import com.quinn.framework.api.entityflag.CacheAble;
import com.quinn.util.base.model.BaseResult;

import java.util.List;

/**
 * 启动即加载服务
 *
 * @author Qunhua.Liao
 * @since 2020-05-29
 */
public interface LoadOnStartDataService {

    /**
     * 查找热数据
     *
     * @return 热数据
     */
    <T extends CacheAble> BaseResult<List<T>> selectHotData();

}
