package com.quinn.framework.model.strategy;

import com.quinn.framework.api.StrategyParam;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.enums.HttpMethodEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.RequestEntity;

/**
 * Http请求参数
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
@Getter
@Setter
public class HttpParam<T> implements StrategyParam<T> {

    private String url;

    private HttpMethodEnum method;

    public Class<T> resultClass;

    private String paramEntityName;

    public RequestEntity.HeadersBuilder wrapBuilder(RequestEntity.HeadersBuilder headersBuilder) {
        return headersBuilder;
    }

    public <T> BaseResult<T> wrapResult(T body) {
        return BaseResult.success(body);
    }
}
