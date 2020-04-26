package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

/**
 * 策略执行器
 *
 * <S> 脚本传入的静态参数
 * <D> 运行时传入的动态参数
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
public interface StrategyExecutor<S extends StrategyParam, D> {

    /**
     * 策略执行
     *
     * @param strategyScript 脚本
     * @param dynamicParam   动态参数
     * @return 执行结果
     */
    <T> BaseResult<T> execute(S strategyScript, D dynamicParam);

}
