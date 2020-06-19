package com.quinn.framework.model.strategy;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.enums.HttpMethodEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Http请求参数
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Getter
@Setter
public class HttpRequestParam<T> extends BaseStrategyParam<T> {

    private static final String HEADER_PARAM_KEY = "Headers";

    /**
     * URL模板：可能有占位参数
     */
    private String url;

    /**
     * 方法POST\GET
     */
    private HttpMethodEnum method;

    /**
     * 从脚本解析参数
     *
     * @param strategyScript 策略脚本
     * @param param          参数
     * @return 请求参数
     */
    public static HttpRequestParam fromScript(StrategyScript strategyScript, Map<String, Object> param) {
        HttpRequestParam requestParam = new HttpRequestParam();
        requestParam.initParam(strategyScript, param);
        requestParam.setUrl(FreeMarkTemplateLoader.invoke(strategyScript.getScriptUrl(), param));
        return requestParam;
    }

    /**
     * 包装头信息构建（一般加一些Header）
     *
     * @param headersBuilder 头信息构建器
     * @return 包装后的构建器
     */
    public RequestEntity.HeadersBuilder wrapBuilder(RequestEntity.HeadersBuilder headersBuilder) {
        JSONObject headers = getJsonParam().getJSONObject(HEADER_PARAM_KEY);
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                headersBuilder.header(entry.getKey(), BaseConverter.staticToString(entry.getValue()));
            }
            getJsonParam().remove(HEADER_PARAM_KEY);
        }
        return headersBuilder;
    }

    /**
     * 包装结果
     *
     * @param res 结果体
     * @return 包装后的结果 BaseResult
     */
    public Object wrapResult(ResponseEntity<T> res) {
        return res.getBody();
    }

    /**
     * 个性化包装（留给子类实现）
     *
     * @param body 结果体
     * @return 包装体
     */
    private BaseResult customWrapResult(T body) {
        return BaseResult.success(body);
    }

    /**
     * 包装请求体
     *
     * @param realParam 真实参数
     * @return 请求体
     */
    public HttpEntity wrapParamToEntity(Object realParam) {
        JSONObject headers = getJsonParam().getJSONObject(HEADER_PARAM_KEY);
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpHeaders.add(entry.getKey(), BaseConverter.staticToString(entry.getValue()));
            }
            getJsonParam().remove(HEADER_PARAM_KEY);
        }
        return new HttpEntity(realParam, httpHeaders);
    }

}
