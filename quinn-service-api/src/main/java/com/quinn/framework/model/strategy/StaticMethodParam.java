package com.quinn.framework.model.strategy;

import com.quinn.framework.api.strategy.StrategyScript;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 静态方法参数策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Getter
@Setter
public class StaticMethodParam<T> extends BaseStrategyParam<T> {

    private String url;

    public static StaticMethodParam fromScript(StrategyScript strategyScript, Map<String, Object> dynamicParam) {
        StaticMethodParam staticMethodParam = new StaticMethodParam();
        staticMethodParam.initParam(strategyScript, dynamicParam);
        staticMethodParam.setUrl(strategyScript.getScriptUrl());
        return staticMethodParam;
    }
}
