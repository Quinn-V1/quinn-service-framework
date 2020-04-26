package com.quinn.framework.component.strategy;

import com.quinn.framework.api.StrategyExecutor;
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
@Component("METHOD_BEANStrategyExecutor")
public class MethodBeanStrategy implements StrategyExecutor<BeanMethodParam, Map<String, Object>> {

    @Override
    public <T> BaseResult<T> execute(BeanMethodParam strategyScript, Map<String, Object> dynamicParam) {
        return null;
    }

}
