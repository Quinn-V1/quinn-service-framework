package com.quinn.framework.api.strategy;

import com.quinn.framework.model.strategy.StrategyParamItem;

import java.util.LinkedList;
import java.util.Map;

/**
 * 策略脚本（对于策略得进一步具体化）
 *
 * @author Qunhua.Liao
 * @since 2020-04-26
 */
public interface StrategyScript {

    /**
     * 脚本类型
     *
     * @return 脚本类型
     */
    String getScriptType();

    /**
     * 脚本资源唯一标识符
     *
     * @return 唯一标识符
     */
    String getScriptUrl();

    /**
     * 获取策略静态参数类型
     *
     * @return 静态参数
     */
    LinkedList<String> getParamTempChain();

    /**
     * 获取参数解析策略
     *
     * @return 参数解析策略
     */
    Map<String, StrategyParamItem> getParamItems();

}
