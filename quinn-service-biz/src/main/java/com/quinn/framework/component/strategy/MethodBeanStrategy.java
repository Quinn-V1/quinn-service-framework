package com.quinn.framework.component.strategy;

import com.quinn.framework.api.strategy.*;
import com.quinn.framework.model.strategy.BeanMethodParam;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.model.BaseResult;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean方法策略执行器
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("METHOD_BEAN_StrategyExecutor")
public class MethodBeanStrategy implements StrategyExecutor<BeanMethodParam> {

    /**
     * 作为策略的Bean
     */
    private static final Map<String, StrategyBean> STRATEGY_BEAN_MAP = new HashMap<>();

    /**
     * 作为策略的Bean
     */
    private static final Map<String, BeanMethodInvoker> BEAN_METHOD_INVOKER_MAP = new HashMap<>();

    /**
     * 设置Bean Map
     *
     * @param strategyBeanMap Bean Map
     */
    public static void addStrategyBeanMap(Map<String, StrategyBean> strategyBeanMap) {
        for (Map.Entry<String, StrategyBean> entry : strategyBeanMap.entrySet()) {
            StrategyBean bean = entry.getValue();
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                Strategy declaredAnnotation = method.getDeclaredAnnotation(Strategy.class);
                if (declaredAnnotation == null) {
                    continue;
                }

                Parameter[] parameters = method.getParameters();
                String[] paramNames = new String[parameters.length];
                Class[] paramClass = new Class[parameters.length];

                for (int i = 0; i < parameters.length; i++) {
                    paramNames[i] = parameters[i].getName();
                    paramClass[i] = parameters[i].getType();
                }

                String url = declaredAnnotation.value();
                STRATEGY_BEAN_MAP.put(url, bean);

                BEAN_METHOD_INVOKER_MAP.put(url, (bean1, param) -> {
                    Object[] params = new Object[paramNames.length];
                    for (int i = 0; i < paramNames.length; i++) {
                        Object o = param.get(i + "");
                        if (o == null) {
                            o = param.get(paramNames[i]);
                        }
                        params[i] = BaseConverter.staticConvert(o, paramClass[i]);
                    }

                    try {
                        return method.invoke(bean1, params);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    @Override
    public <T> BaseResult<T> execute(BeanMethodParam beanMethodParam) {
        StrategyBean bean = STRATEGY_BEAN_MAP.get(beanMethodParam.getUrl());
        BeanMethodInvoker beanMethodInvoker = BEAN_METHOD_INVOKER_MAP.get(beanMethodParam.getUrl());
        if (bean == null && beanMethodInvoker == null) {
            return BaseResult.fail();
        }
        Object object = beanMethodInvoker.revoke(bean, beanMethodParam.getJsonParam());
        if (object instanceof BaseResult) {
            return (BaseResult) object;
        }
        return BaseResult.success((T) object);
    }

    @Override
    public BeanMethodParam parseParam(StrategyScript strategyScript, Map<String, Object> dynamicParam) {
        return BeanMethodParam.fromScript(strategyScript, dynamicParam);
    }

}
