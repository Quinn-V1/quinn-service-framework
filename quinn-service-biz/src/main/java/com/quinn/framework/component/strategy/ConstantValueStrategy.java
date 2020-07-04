package com.quinn.framework.component.strategy;

import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.ConstantValueParam;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 参数值直接取值策略
 *
 * @author Qunhua.Liao
 * @since 2020-05-18
 */
@Component("CONSTANT_VALUE_StrategyExecutor")
public class ConstantValueStrategy implements StrategyExecutor<ConstantValueParam> {

    @Override
    public Object execute(ConstantValueParam constantValueParam) {
        return constantValueParam.getConstantValue();
    }

    @Override
    public ConstantValueParam parseParam(StrategyScript strategyScript, Map<String, Object> param) {
        return ConstantValueParam.fromScript(strategyScript, param);
    }

}
