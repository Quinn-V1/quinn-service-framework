package com.quinn.framework.model.strategy;

import com.quinn.framework.api.strategy.StrategyScript;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 方法参数策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Getter
@Setter
public class BeanMethodParam<T> extends BaseStrategyParam<T> {

    private String url;

    public static BeanMethodParam fromScript(StrategyScript strategyScript, Map<String, Object> dynamicParam) {
        BeanMethodParam beanMethodParam = new BeanMethodParam();
        beanMethodParam.initParam(strategyScript, dynamicParam);
        beanMethodParam.setUrl(strategyScript.getScriptUrl());
        return beanMethodParam;
    }
}
