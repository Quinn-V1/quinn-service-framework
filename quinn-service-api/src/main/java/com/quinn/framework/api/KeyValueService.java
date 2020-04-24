package com.quinn.framework.api;

import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.model.PageInfo;
import com.quinn.util.base.api.KeyValue;
import com.quinn.util.base.model.BaseResult;

import java.util.List;
import java.util.Map;

/**
 * 通用键值对业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-23
 */
public interface KeyValueService<K extends KeyValue, C extends BaseDTO> {

    /**
     * 数据类型
     *
     * @return 数据类型
     */
    String dataType();

    /**
     * 综合查询
     *
     * @param condition 基础条件
     * @return 符合条件的列表
     */
    BaseResult<List<K>> select(C condition);

    /**
     * 综合查询
     *
     * @param condition 基础条件
     * @return 符合条件的列表
     */
    BaseResult<List<K>> selectByMap(Map condition);

    /**
     * 综合查询
     *
     * @param condition 基础条件
     * @return 符合条件的分页列表
     */
    BaseResult<PageInfo<K>> page(C condition);

    /**
     * 综合查询
     *
     * @param condition 基础条件
     * @return 符合条件的分页列表
     */
    BaseResult<PageInfo<K>> pageByMap(Map condition);

}
