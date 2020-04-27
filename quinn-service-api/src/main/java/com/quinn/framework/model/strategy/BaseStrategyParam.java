package com.quinn.framework.model.strategy;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.base.util.CollectionUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Map;

/**
 * 策略基本参数
 *
 * @author Qunhua.Liao
 * @since 2020-04-26
 */
@Getter
@Setter
public class BaseStrategyParam<T> {

    /**
     * 同步标识
     */
    private boolean async;

    /**
     * 结果类型
     */
    public Class<T> resultClass;

    /**
     * 回调函数
     */
    private MethodInvokerOneParam callback;

    /**
     * 运行时传参数名称
     */
    private JSONObject jsonParam;

    /**
     * 初始化参数
     */
    protected void initParam(StrategyScript strategyScript, Map<String, Object> param) {
        JSONObject jsonObject = new JSONObject();
        LinkedList<String> paramTempChain = strategyScript.getParamTempChain();
        for (String paramTemplate : paramTempChain) {
            paramTemplate = FreeMarkTemplateLoader.invoke(paramTemplate, param);
            CollectionUtil.mergeMap(jsonObject, JSONObject.parseObject(paramTemplate));
        }
        setJsonParam(jsonObject);
    }
}
