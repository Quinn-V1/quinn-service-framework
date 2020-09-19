package com.quinn.framework.component.strategy;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.HttpRequestParam;
import com.quinn.util.base.exception.BaseBusinessException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Http Post请求策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("HTTP_POST_StrategyExecutor")
public class HttpPostStrategy implements StrategyExecutor<HttpRequestParam> {

    @Resource
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Override
    public Object execute(HttpRequestParam httpRequestParam) {
        Class clazz = httpRequestParam.getResultClass();
        Class resultClass = clazz == null ? JSONObject.class : clazz;

        Object realParam = httpRequestParam.getRealParams();
        HttpEntity entity = httpRequestParam.wrapParamToEntity(realParam);

        try {
            ResponseEntity res = restTemplate.postForEntity(httpRequestParam.getUrl(), entity, resultClass);
            return httpRequestParam.wrapResult(res);
        } catch (RestClientResponseException e) {
            throw new BaseBusinessException(e.getResponseBodyAsString(), false);
        }
    }

    @Override
    public HttpRequestParam parseParam(StrategyScript strategyScript, Map<String, Object> param) {
        return HttpRequestParam.fromScript(strategyScript, param);
    }

}
