package com.quinn.framework.component.strategy;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.api.strategy.StrategyScript;
import com.quinn.framework.model.strategy.BaseStrategyParam;
import com.quinn.framework.model.strategy.HttpRequestParam;
import com.quinn.util.base.model.BaseResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Http Get请求策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("HTTP_GET_StrategyExecutor")
public class HttpGetStrategy implements StrategyExecutor<HttpRequestParam> {

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("strategyExecutorService")
    private ExecutorService strategyExecutorService;

    @Override
    @SneakyThrows
    public <T> BaseResult<T> execute(HttpRequestParam httpRequestParam) {
        Class clazz = httpRequestParam.getResultClass();
        final Class resultClass = clazz == null ? JSONObject.class : clazz;

        RequestEntity requestEntity = httpRequestParam.wrapBuilder(
                RequestEntity.get(new URI(httpRequestParam.getUrl()))).build();

        if (httpRequestParam.isAsync()) {
            strategyExecutorService.execute(() -> {
                ResponseEntity res = restTemplate.exchange(requestEntity, resultClass);
                httpRequestParam.wrapResult(res);
            });
            return BaseResult.SUCCESS;
        } else {
            ResponseEntity res = restTemplate.exchange(requestEntity, resultClass);
            return httpRequestParam.wrapResult(res);
        }
    }

    @Override
    public HttpRequestParam parseParam(StrategyScript strategyScript, Map<String, Object> param) {
        return HttpRequestParam.fromScript(strategyScript, param);
    }

}
