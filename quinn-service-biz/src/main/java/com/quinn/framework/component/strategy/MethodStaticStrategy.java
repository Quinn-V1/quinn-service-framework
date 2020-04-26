package com.quinn.framework.component.strategy;

import com.quinn.framework.api.StrategyExecutor;
import com.quinn.framework.model.strategy.StaticMethodParam;
import com.quinn.util.base.model.BaseResult;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 静态方法策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("METHOD_STATICStrategyExecutor")
public class MethodStaticStrategy implements StrategyExecutor<StaticMethodParam, Map<String, Object>> {

    @Override
    public <T> BaseResult<T> execute(StaticMethodParam strategyScript, Map<String, Object> dynamicParam) {
        return null;
    }
}
