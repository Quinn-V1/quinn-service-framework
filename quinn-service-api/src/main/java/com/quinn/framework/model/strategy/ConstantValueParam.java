package com.quinn.framework.model.strategy;

import com.quinn.framework.api.strategy.StrategyScript;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 策略基本参数
 *
 * @author Qunhua.Liao
 * @since 2020-04-26
 */
@Getter
@Setter
public class ConstantValueParam<T> extends BaseStrategyParam<T> {

    /**
     * 常量值
     */
    private String constantValue;

    public static ConstantValueParam fromScript(StrategyScript strategyScript, Map<String, Object> param) {
        ConstantValueParam result = new ConstantValueParam();
        result.setConstantValue(strategyScript.getScriptUrl());
        return result;
    }

    @Override
    public void initParam(StrategyScript strategyScript, Map<String, Object> param) {
        super.initParam(strategyScript, param);
    }
}
