package com.quinn.framework.model.strategy;

import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;
import com.quinn.util.constant.enums.HttpMethodEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.*;

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
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity(realParam, headers);
    }

}
