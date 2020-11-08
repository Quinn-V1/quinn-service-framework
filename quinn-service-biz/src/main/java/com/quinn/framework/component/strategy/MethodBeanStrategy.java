package com.quinn.framework.component.strategy;

import com.quinn.framework.api.strategy.StrategyBean;
import com.quinn.util.base.exception.MethodNotFoundException;

import java.util.Map;

/**
 * Bean方法策略
 *
 * @author Qunhua.Liao
 * @since 2020-11-08
 */
public class MethodBeanStrategy {

    public static void addStrategyBeanMap(Map<String, StrategyBean> strategyBeans) {
        throw new MethodNotFoundException();
    }

}
