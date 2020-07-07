package com.quinn.framework.api;

import com.quinn.framework.entity.dto.PageDTO;
import com.quinn.util.base.model.BaseResult;
import com.quinn.framework.model.PageInfo;

import java.util.Map;

/**
 * 分页处理器
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
public interface PageAdapter<T> {

    /**
     * 处理分页参数
     *
     * @param pageNum  分页条件
     * @param pageSize 分页条件
     * @return 处理是否成功
     */
    BaseResult handlePageParam(int pageNum, int pageSize);

    /**
     * 处理分页参数
     *
     * @param condition 分页条件
     * @return 处理是否成功
     */
    BaseResult handlePageParam(PageDTO condition);

    /**
     * 处理分页参数
     *
     * @param condition 分页条件
     * @return 处理是否成功
     */
    BaseResult handlePageParam(Map<String, Object> condition);

    /**
     * 将外部分页对象转为内部分页对象
     *
     * @param t   源数据
     * @param <V> 结果泛型
     * @return 分页信息
     */
    <V> PageInfo<V> toPageInf(T t);

}
