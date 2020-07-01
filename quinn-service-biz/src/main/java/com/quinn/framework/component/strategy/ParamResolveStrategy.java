package com.quinn.framework.component.strategy;

import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.BaseStrategyParam;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 参数值直接解析策略
 *
 * @author Qunhua.Liao
 * @since 2020-05-18
 */
@Component("PARAM_RESOLVE_StrategyExecutor")
public class ParamResolveStrategy implements StrategyExecutor<BaseStrategyParam> {

    @Override
    public Object execute(BaseStrategyParam paramValueParam) {
        return paramValueParam.getJsonParam();
    }

    @Override
    public BaseStrategyParam parseParam(StrategyScript strategyScript, Map<String, Object> param) {
        BaseStrategyParam baseStrategyParam = new BaseStrategyParam();
        baseStrategyParam.initParam(strategyScript, param);
        return baseStrategyParam;
    }

}
