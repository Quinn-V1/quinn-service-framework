package com.quinn.framework.api.strategy;

import com.quinn.framework.model.strategy.BaseStrategyParam;
import com.quinn.util.base.model.BaseResult;

import java.util.Map;

/**
 * 策略执行器
 *
 * <S> 脚本传入的静态参数
 * <D> 运行时传入的动态参数
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
public interface StrategyExecutor<S extends BaseStrategyParam> {

    /**
     * 策略执行
     *
     * @param strategyParam 脚本
     * @return 执行结果
     */
    Object execute(S strategyParam);

    /**
     * 解析参数
     *
     * @param strategyScript 非结构化参数
     * @param param          运行时参数
     * @return 结构化参数
     */
    S parseParam(StrategyScript strategyScript, Map<String, Object> param);

}
