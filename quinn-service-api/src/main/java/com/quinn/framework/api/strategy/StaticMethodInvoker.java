package com.quinn.framework.api.strategy;

import com.alibaba.fastjson.JSONObject;

/**
 * 静态方法调用其
 *
 * @author Qunhua.Liao
 * @since 2020-04-27
 */
public interface StaticMethodInvoker {

    /**
     * 调用方法
     *
     * @param param 参数
     * @return  调用结果
     */
    Object revoke(JSONObject param);

}
