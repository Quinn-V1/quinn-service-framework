package com.quinn.framework.model.strategy;

import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.constant.StringConstant;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 参数值策略参数
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Getter
@Setter
public class ParamValueParam<T> extends BaseStrategyParam<T> {

    private String[] paramPaths;

    public static ParamValueParam fromScript(StrategyScript strategyScript, Map<String, Object> dynamicParam) {
        ParamValueParam paramValueParam = new ParamValueParam();
        paramValueParam.initParam(strategyScript, dynamicParam);
        String url = strategyScript.getScriptUrl();
        url = FreeMarkTemplateLoader.invoke(url, dynamicParam);

        if (StringUtil.isEmptyInFrame(url)) {
            paramValueParam.setParamPaths(new String[]{ConfigConstant.PARAM_OF_ITSELF});
        } else {
            paramValueParam.setParamPaths(url.split(StringConstant.CHAR_DOT_));
        }

        return paramValueParam;
    }
}
