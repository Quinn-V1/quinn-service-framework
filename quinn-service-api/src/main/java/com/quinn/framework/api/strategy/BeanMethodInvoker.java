package com.quinn.framework.api.strategy;

import com.alibaba.fastjson.JSONObject;

/**
 * Bean 方法 回调器
 *
 * @author Qunhua.Liao
 * @since 2020-04-28
 */
public interface BeanMethodInvoker {

    /**
     * 调用方法
     *
     * @param bean  对象
     * @param param 参数
     * @return 调用结果
     */
    Object revoke(StrategyBean bean, JSONObject param);

}
