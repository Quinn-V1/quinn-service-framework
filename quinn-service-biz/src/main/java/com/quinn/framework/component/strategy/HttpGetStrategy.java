package com.quinn.framework.component.strategy;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.HttpRequestParam;
import com.quinn.util.base.exception.BaseBusinessException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Http Get请求策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("HTTP_GET_StrategyExecutor")
public class HttpGetStrategy implements StrategyExecutor<HttpRequestParam> {

    @Resource
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Override
    public Object execute(HttpRequestParam httpRequestParam) {
        Class clazz = httpRequestParam.getResultClass();
        final Class resultClass = clazz == null ? JSONObject.class : clazz;

        RequestEntity requestEntity;
        try {
            requestEntity = httpRequestParam.wrapBuilder(
                    RequestEntity.get(new URI(httpRequestParam.getUrl()))
            ).build();
        } catch (URISyntaxException e) {
            throw new BaseBusinessException("Url not correct", false);
        }

        try {
            return httpRequestParam.wrapResult(restTemplate.exchange(requestEntity, resultClass));
        } catch (RestClientResponseException e) {
            throw new BaseBusinessException(e.getResponseBodyAsString(StandardCharsets.UTF_8), false);
        }
    }

    @Override
    public HttpRequestParam parseParam(StrategyScript strategyScript, Map<String, Object> param) {
        return HttpRequestParam.fromScript(strategyScript, param);
    }

}
