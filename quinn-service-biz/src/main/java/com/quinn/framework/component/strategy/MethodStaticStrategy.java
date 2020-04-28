package com.quinn.framework.component.strategy;

import com.quinn.framework.api.strategy.*;
import com.quinn.framework.model.strategy.StaticMethodParam;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.enums.ExceptionEnum;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 静态方法策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("METHOD_STATIC_StrategyExecutor")
public class MethodStaticStrategy implements StrategyExecutor<StaticMethodParam> {

    private static final Map<String, StaticMethodInvoker> STATIC_METHOD_INVOKER_MAP = new HashMap<>();

    static {
        ServiceLoader<StaticMethodRegister> staticMethodRegisters = ServiceLoader.load(StaticMethodRegister.class);
        Iterator<StaticMethodRegister> staticMethodRegisterIterator = staticMethodRegisters.iterator();
        while (staticMethodRegisterIterator.hasNext()) {
            StaticMethodRegister next = staticMethodRegisterIterator.next();
            Class[] register = next.register();
            for (Class clazz : register) {
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (Method method : declaredMethods) {
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

                    STATIC_METHOD_INVOKER_MAP.put(declaredAnnotation.value(), paramMap -> {
                        Object[] params = new Object[paramNames.length];
                        for (int i = 0; i < paramNames.length; i++) {
                            Object o = paramMap.get(paramNames[i]);
                            if (o == null) {
                                o = paramMap.get(i + "");
                            }
                            params[i] = BaseConverter.staticConvert(o, paramClass[i]);
                        }

                        try {
                            return method.invoke(null, params);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }
    }

    @Override
    public <T> BaseResult<T> execute(StaticMethodParam staticMethodParam) {
        StaticMethodInvoker staticMethodInvoker = STATIC_METHOD_INVOKER_MAP.get(staticMethodParam.getUrl());
        if (staticMethodInvoker == null) {
            return BaseResult.fail()
                    .buildMessage(ExceptionEnum.STRATEGY_NOT_SUPPORTED.name(), 1, 0)
                    .addParamI8n(ExceptionEnum.STRATEGY_NOT_SUPPORTED.paramNames[0], staticMethodParam.getUrl())
                    .result();
        }

        Object obj = staticMethodInvoker.revoke(staticMethodParam.getJsonParam());
        if (obj instanceof BaseResult) {
            return (BaseResult) obj;
        }

        return BaseResult.success((T) obj);
    }

    @Override
    public StaticMethodParam parseParam(StrategyScript strategyScript, Map<String, Object> dynamicParam) {
        return StaticMethodParam.fromScript(strategyScript, dynamicParam);
    }
}
