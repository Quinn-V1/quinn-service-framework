package com.quinn.framework.component.strategy;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.StrategyExecutor;
import com.quinn.framework.model.strategy.HttpParam;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.model.BaseResult;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Map;

/**
 * Http Get请求策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("HTTP_GETStrategyExecutor")
public class HttpGetStrategy implements StrategyExecutor<HttpParam, Map<String, Object>> {

    @Resource
    private RestTemplate restTemplate;

    @SneakyThrows
    @Override
    public <T> BaseResult<T> execute(HttpParam httpParam, Map<String, Object> dynamicParam) {
        String url = httpParam.getUrl();
        url = FreeMarkTemplateLoader.invoke(url, dynamicParam);
        RequestEntity requestEntity = httpParam.wrapBuilder(RequestEntity.get(new URI(url)))
                .build();

        Class resultClass = httpParam.getResultClass();
        resultClass = resultClass == null ? JSONObject.class : resultClass;
        ResponseEntity res = this.restTemplate.exchange(requestEntity, resultClass);

        if (res.getStatusCodeValue() < HttpStatus.OK.value()
                || res.getStatusCodeValue() > HttpStatus.MULTIPLE_CHOICES.value()) {
            return BaseResult.fail().ofData(res.getStatusCodeValue())
                    .ofMessage(res.getStatusCode().getReasonPhrase());
        }

        return httpParam.wrapResult((T) res.getBody());
    }

}
