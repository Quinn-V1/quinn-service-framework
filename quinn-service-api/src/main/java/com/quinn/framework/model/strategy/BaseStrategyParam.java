package com.quinn.framework.model.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.CollectionUtil;
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

    private static final String DIRECT_PARAM_PREFIX = "_DIRECT:";

    private static final String OUT_PARAM_NAME = "_outParam";

    private static final String OUT_PARAM_NAME_MAPPING = "_paramNameMapping";

    /**
     * 结果类型
     */
    private Class<T> resultClass;

    /**
     * 运行时传参数名称
     */
    private JSONObject jsonParam;

    /**
     * 初始化参数
     */
    public void initParam(StrategyScript strategyScript, Map<String, Object> param) {
        JSONObject jsonObject = new JSONObject();
        LinkedList<String> paramTempChain = strategyScript.getParamTempChain();
        if (paramTempChain == null) {
            jsonObject.putAll(param);
            setJsonParam(jsonObject);
            return;
        }

        for (String paramTemplate : paramTempChain) {
            paramTemplate = FreeMarkTemplateLoader.invoke(paramTemplate, param);
            CollectionUtil.mergeMap(jsonObject, JSONObject.parseObject(paramTemplate));
        }

        initDirectParam(jsonObject, param);
        Object o = param.remove(OUT_PARAM_NAME_MAPPING);
        if (o instanceof Map) {
            Map<String, String> mapping = (Map<String, String>) o;
            for (Map.Entry<String, String> entry : mapping.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        }

        setJsonParam(jsonObject);
    }

    /**
     * 初始化直接参数
     *
     * @param jsonObject 参数
     * @param param      直接参数
     */
    private void initDirectParam(Object jsonObject, Map<String, Object> param) {
        if (jsonObject instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) jsonObject;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof String) {
                    String str = (String) value;
                    if (str.startsWith(DIRECT_PARAM_PREFIX)) {
                        String key = str.substring(DIRECT_PARAM_PREFIX.length());
                        map.put(entry.getKey(), param.get(key));
                    } else if (OUT_PARAM_NAME.equals(str)) {
                        map.put(entry.getKey(), param);
                    }
                } else if (value instanceof Map || value instanceof JSONArray) {
                    initDirectParam(value, param);
                }
            }
        } else if (jsonObject instanceof JSONArray) {
            JSONArray array = (JSONArray) jsonObject;
            int size = array.size();
            for (int i = 0; i < size; i++) {
                initDirectParam(array.get(i), param);
            }
        }
    }

}
