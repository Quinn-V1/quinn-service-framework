package com.quinn.framework.component.strategy;

import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.BaseStrategyParam;
import com.quinn.framework.model.strategy.BeanMethodParam;
import com.quinn.util.base.model.BaseResult;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Bean方法策略执行器
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("METHOD_BEAN_StrategyExecutor")
public class MethodBeanStrategy implements StrategyExecutor<BeanMethodParam> {

    @Override
    public <T> BaseResult<T> execute(BeanMethodParam strategyScript) {
        return null;
    }

    @Override
    public BeanMethodParam parseParam(StrategyScript strategyParam, Map<String, Object> dynamicParam) {
        return null;
    }

}
