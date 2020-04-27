package com.quinn.framework.component.strategy;

import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.BaseStrategyParam;
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
@Component("METHOD_STATIC_StrategyExecutor")
public class MethodStaticStrategy implements StrategyExecutor<StaticMethodParam> {

    @Override
    public <T> BaseResult<T> execute(StaticMethodParam strategyScript) {
        return null;
    }

    @Override
    public StaticMethodParam parseParam(StrategyScript strategyParam, Map<String, Object> dynamicParam) {
        return null;
    }
}
