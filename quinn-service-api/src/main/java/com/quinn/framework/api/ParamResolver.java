package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

import java.util.Map;

/**
 * 参数解析器
 *
 * @author Qunhua.Liao
 * @since 2020-02-11
 */
public interface ParamResolver {

    /**
     * 消息参数
     *
     * @param messageParam  原参数
     * @return 加工后的参数
     */
    BaseResult<Map<String, Object>> resolve(Map<String, Object> messageParam);

}
