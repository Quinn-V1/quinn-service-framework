package com.quinn.framework.model.strategy;

import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.BaseStrategyParam;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.exception.ParameterShouldNotEmpty;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.enums.ExceptionEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略工厂（解析策略参数：执行策略）
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
public class StrategyFactory {

    private static final String BEAN_NAME_SUFFIX = "_StrategyExecutor";

    private static final Map<String, StrategyExecutor> STRATEGY_EXECUTOR_MAP = new HashMap<>();

    /**
     * 策略执行构造器
     *
     * @param strategyScript 策略脚本
     * @param async          异步标识
     * @param param          运行时参数
     * @return 构建器
     */
    public static StrategyBuilder build(StrategyScript strategyScript, boolean async, Map<String, Object> param) {
        StrategyBuilder builder = new StrategyBuilder(strategyScript, async, param);
        return builder;
    }

    /**
     * 添加策略
     *
     * @param map 策略执行器
     */
    public static void addStrategies(Map<String, StrategyExecutor> map) {
        STRATEGY_EXECUTOR_MAP.putAll(map);
    }

    /**
     * 策略参数构建器
     *
     * @author Qunhua.Liao
     * @since 2020-04-26
     */
    public static class StrategyBuilder {

        /**
         * 参数
         */
        private BaseStrategyParam strategyParam;

        /**
         * 执行器
         */
        private StrategyExecutor strategyExecutor;

        /**
         * 参数构造器
         *
         * @param strategyScript 策略脚本
         * @param async          是否同步
         * @param params         参数
         */
        private StrategyBuilder(StrategyScript strategyScript, boolean async, Map<String, Object> params) {
            String scriptType = strategyScript.getScriptType() + BEAN_NAME_SUFFIX;
            strategyExecutor = STRATEGY_EXECUTOR_MAP.get(scriptType);

            Map<String, StrategyParamItem> paramItems = strategyScript.getParamItems();
            if (paramItems != null) {
                for (Map.Entry<String, StrategyParamItem> entry : paramItems.entrySet()) {
                    StrategyParamItem paramItem = entry.getValue();
                    Object paramValue = paramItem.getParamValue();

                    if (paramItem.getValueStrategy() != null) {
                        BaseResult<Object> execute = StrategyFactory.build(paramItem.getValueStrategy(),
                                true, params).execute();
                        if (execute.isSuccess()) {
                            paramValue = execute.getData();
                        }
                    }

                    String paramName = entry.getKey();
                    if (BaseConverter.staticIsEmpty(paramValue)) {
                        if (paramItem.isMustFlag()) {
                            throw new ParameterShouldNotEmpty().getMessageProp()
                                    .addParam(ExceptionEnum.PARAM_SHOULD_NOT_NULL.paramNames, paramName)
                                    .exception();
                        }
                    } else {
                        params.put(paramName, paramValue);
                    }
                }
            }

            strategyParam = strategyExecutor.parseParam(strategyScript, params);
            strategyParam.setAsync(async);
        }

        /**
         * 设置结果类型
         *
         * @param clazz 结果类型
         * @return 本身
         */
        public StrategyBuilder ofResultClass(Class clazz) {
            strategyParam.setResultClass(clazz);
            return this;
        }

        /**
         * 设置结果类型
         *
         * @param callback 结果类型
         * @return 本身
         */
        public StrategyBuilder ofCallback(MethodInvokerOneParam callback) {
            strategyParam.setCallback(callback);
            return this;
        }

        /**
         * 执行脚本
         *
         * @param <T> 结果泛型
         * @return 执行结果
         */
        public <T> BaseResult<T> execute() {
            return strategyExecutor.execute(strategyParam);
        }
    }

}
