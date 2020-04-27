package com.quinn.framework.model.strategy;

import com.quinn.framework.api.strategy.StrategyScript;
import lombok.Getter;
import lombok.Setter;

/**
 * 策略参数条目
 *
 * @author Qunhua.Liao
 * @since 2020-04-27
 */
@Getter
@Setter
public class StrategyParamItem {

    /**
     * 参数名
     */
    private String paramName;

    /**
     * 参数类型
     */
    private Class paramClass;

    /**
     * 是否必须
     */
    private boolean mustFlag;

    /**
     * 是否必须
     */
    private Object paramValue;

    /**
     * 值解析策略
     */
    private StrategyScript valueStrategy;

}
