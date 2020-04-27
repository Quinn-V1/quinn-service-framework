package com.quinn.framework.component.strategy;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.BaseStrategyParam;
import com.quinn.framework.model.strategy.HttpRequestParam;
import com.quinn.util.base.exception.ParameterShouldNotEmpty;
import com.quinn.util.base.model.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Http Post请求策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("HTTP_POST_StrategyExecutor")
public class HttpPostStrategy implements StrategyExecutor<HttpRequestParam> {

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("strategyExecutorService")
    private ExecutorService strategyExecutorService;

    @Override
    public <T> BaseResult<T> execute(HttpRequestParam httpRequestParam) {

        Class clazz = httpRequestParam.getResultClass();
        Class resultClass = clazz == null ? JSONObject.class : clazz;

        JSONObject realParam = httpRequestParam.getJsonParam();
        HttpEntity entity = httpRequestParam.wrapParamToEntity(realParam);

        if (entity == null) {
            throw new ParameterShouldNotEmpty();
        }

        if (httpRequestParam.isAsync()) {
            strategyExecutorService.execute(() -> {
                ResponseEntity res = restTemplate.postForEntity(httpRequestParam.getUrl(), entity, resultClass);
                httpRequestParam.wrapResult(res);
            });
            return BaseResult.SUCCESS;
        } else {
            ResponseEntity res = restTemplate.postForEntity(httpRequestParam.getUrl(), entity, resultClass);
            return httpRequestParam.wrapResult(res);
        }
    }

    @Override
    public HttpRequestParam parseParam(StrategyScript strategyScript, Map<String, Object> param) {
        return HttpRequestParam.fromScript(strategyScript, param);
    }

}
