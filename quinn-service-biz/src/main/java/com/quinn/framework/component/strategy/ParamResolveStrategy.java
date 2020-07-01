package com.quinn.framework.component.strategy;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.ParamValueParam;
import com.quinn.util.base.BaseUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 参数值直接解析策略
 *
 * @author Qunhua.Liao
 * @since 2020-05-18
 */
@Component("PARAM_RESOLVE_StrategyExecutor")
public class ParamResolveStrategy implements StrategyExecutor<ParamValueParam> {

    @Override
    public Object execute(ParamValueParam paramValueParam) {
        String[] paths = paramValueParam.getParamPaths();
        JSONObject jsonParam = paramValueParam.getJsonParam();
        return BaseUtil.valueOfJson(jsonParam, paths);
    }

    @Override
    public ParamValueParam parseParam(StrategyScript strategyScript, Map<String, Object> param) {
        return ParamValueParam.fromScript(strategyScript, param);
    }

}
