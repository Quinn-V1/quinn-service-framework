package com.quinn.framework.component.strategy;

import com.alibaba.fastjson.JSONObject;
import com.quinn.framework.api.StrategyExecutor;
import com.quinn.framework.model.strategy.HttpParam;
import com.quinn.util.FreeMarkTemplateLoader;
import com.quinn.util.base.exception.ParameterShouldNotEmpty;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.util.StringUtil;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Map;

/**
 * Http Post请求策略
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Component("HTTP_POSTStrategyExecutor")
public class HttpPostStrategy implements StrategyExecutor<HttpParam, Map<String, Object>> {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public <T> BaseResult<T> execute(HttpParam httpParam, Map<String, Object> dynamicParam) {
        String url = httpParam.getUrl();
        url = FreeMarkTemplateLoader.invoke(url, dynamicParam);

        Class resultClass = httpParam.getResultClass();
        resultClass = resultClass == null ? JSONObject.class : resultClass;

        String entityName = httpParam.getParamEntityName();
        Object entity = StringUtil.isEmpty(entityName) ? dynamicParam : dynamicParam.get(entityName);
        if (entity == null) {
            throw new ParameterShouldNotEmpty();
        }

        ResponseEntity res = restTemplate.postForEntity(url, entity, resultClass);

        return httpParam.wrapResult((T) res.getBody());
    }
}
