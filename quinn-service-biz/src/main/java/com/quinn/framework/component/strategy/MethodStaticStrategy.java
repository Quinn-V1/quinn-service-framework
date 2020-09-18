package com.quinn.framework.component.strategy;

import com.quinn.framework.api.strategy.StaticMethodInvoker;
import com.quinn.framework.api.strategy.StaticMethodRegister;
import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.StaticMethodParam;
import com.quinn.util.base.api.Strategy;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.exception.ParameterShouldNotEmpty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 静态方法策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("METHOD_STATIC_StrategyExecutor")
public class MethodStaticStrategy implements StrategyExecutor<StaticMethodParam> {

    private static List<String> scriptUrls;

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
        scriptUrls = new ArrayList<>(STATIC_METHOD_INVOKER_MAP.keySet());
        Collections.sort(scriptUrls);
    }

    @Override
    public Object execute(StaticMethodParam staticMethodParam) {
        StaticMethodInvoker staticMethodInvoker = STATIC_METHOD_INVOKER_MAP.get(staticMethodParam.getUrl());
        if (staticMethodInvoker == null) {
            throw new ParameterShouldNotEmpty();
        }

        return staticMethodInvoker.revoke(staticMethodParam.getJsonParam());
    }

    @Override
    public StaticMethodParam parseParam(StrategyScript strategyScript, Map<String, Object> dynamicParam) {
        return StaticMethodParam.fromScript(strategyScript, dynamicParam);
    }

    @Override
    public List<String> scriptUrls() {
        return scriptUrls;
    }
}
