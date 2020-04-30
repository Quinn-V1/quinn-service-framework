package com.quinn.framework.model.strategy;

import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.exception.ParameterShouldNotEmpty;
import com.quinn.util.base.exception.UnSupportedStrategyException;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.util.StringUtil;
import com.quinn.util.constant.enums.ExceptionEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 策略工厂（解析策略参数：执行策略）
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
public class StrategyFactory {

    private static final String BEAN_NAME_SUFFIX = "_StrategyExecutor";

    private static final Map<String, StrategyExecutor> STRATEGY_EXECUTOR_MAP = new HashMap<>();

    private static ExecutorService strategyExecutorService;

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
     * 策略执行线程池
     *
     * @param strategyExecutorService 线程池
     */
    public static void setExecutorService(ExecutorService strategyExecutorService) {
        StrategyFactory.strategyExecutorService = strategyExecutorService;
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
         * 异步执行标识
         */
        private boolean async;

        /**
         * 回调函数
         */
        private MethodInvokerOneParam callback;

        /**
         * 参数构造器
         *
         * @param strategyScript 策略脚本
         * @param async          是否同步
         * @param params         参数
         */
        private StrategyBuilder(StrategyScript strategyScript, boolean async, Map<String, Object> params) {
            String scriptType = strategyScript.getScriptType() + BEAN_NAME_SUFFIX;
            this.strategyExecutor = STRATEGY_EXECUTOR_MAP.get(scriptType);
            if (this.strategyExecutor == null) {
                throw new UnSupportedStrategyException().getMessageProp()
                        .addParam(ExceptionEnum.STRATEGY_NOT_SUPPORTED.paramNames[0], scriptType)
                        .exception();
            }

            this.async = async;
            Map<String, StrategyParamItem> paramItems = strategyScript.getParamItems();
            if (paramItems != null) {
                for (Map.Entry<String, StrategyParamItem> entry : paramItems.entrySet()) {
                    StrategyParamItem paramItem = entry.getValue();
                    Object paramValue = paramItem.getParamValue();

                    if (paramItem.getValueStrategy() != null) {
                        BaseResult<Object> execute = StrategyFactory.build(paramItem.getValueStrategy(),
                                false, params).execute();
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
        }

        /**
         * 设置结果类型
         *
         * @param clazz 结果类型
         * @return 本身
         */
        public StrategyBuilder ofResultClass(Class clazz) {
            if (clazz != null) {
                strategyParam.setResultClass(clazz);
            }
            return this;
        }

        /**
         * 设置结果类型
         *
         * @param clazz 结果类型
         * @return 本身
         */
        public StrategyBuilder ofResultClass(String clazz) {
            if (StringUtil.isNotEmpty(clazz)) {
                strategyParam.setResultClass(BaseConverter.classOf(clazz));
            }
            return this;
        }

        /**
         * 设置结果类型
         *
         * @param callback 结果类型
         * @return 本身
         */
        public StrategyBuilder ofCallback(MethodInvokerOneParam callback) {
            this.callback = callback;
            return this;
        }

        /**
         * 执行脚本
         *
         * @param <T> 结果泛型
         * @return 执行结果
         */
        public <T> BaseResult<T> execute() {
            if (async) {
                strategyExecutorService.execute(() -> {
                    exec();
                });
                return BaseResult.SUCCESS;
            }

            return exec();
        }

        /**
         * 内部执行
         *
         * @return 执行结果
         */
        private BaseResult exec() {
            BaseResult result = BaseResult.fail();
            try {
                result = strategyExecutor.execute(strategyParam);
                return result;
            } finally {
                if (callback != null) {
                    callback.invoke(result);
                }
            }
        }
    }

}
