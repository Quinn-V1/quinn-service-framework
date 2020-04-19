package com.quinn.framework.component.intercept;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.entityflag.CacheAble;
import com.quinn.framework.api.methodflag.GetFlag;
import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.framework.api.methodflag.ReadFlag;
import com.quinn.framework.component.EntityServiceInterceptorChain;
import com.quinn.framework.service.CacheAbleService;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.util.StringUtil;
import com.quinn.util.constant.enums.MessageLevelEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 缓存读过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
@Component
public class CacheAbleGetEntityServiceInterceptor implements EntityServiceInterceptor {

    @Autowired
    private CacheAbleService cacheAbleService;

    @Override
    public Class<? extends MethodFlag>[] interceptBean() {
        return new Class[]{CacheAble.class};
    }

    @Override
    public Class<? extends MethodFlag>[] interceptMethod() {
        return new Class[]{GetFlag.class};
    }

    @Override
    public <T> void before(EntityServiceInterceptorChain chain, T t, BaseResult result) {
        if (t instanceof CacheAble) {
            CacheAble cacheAble = (CacheAble) t;
            String cacheKey = cacheAble.cacheKey();

            BaseResult cacheRes = cacheAbleService.get(cacheKey);
            if (cacheRes.isSuccess()) {
                result.setData(cacheRes.getData());
                result.ofLevel(MessageLevelEnum.WARN);
            }
        }
    }

    @Override
    public <T> void after(EntityServiceInterceptorChain chain, T t, BaseResult result) {
        Object object = result.getData();
        if (object instanceof CacheAble) {
            CacheAble cacheAble = (CacheAble) object;
            String cacheKey = cacheAble.cacheKey();

            if (StringUtil.isNotEmpty(cacheKey)) {
                cacheAbleService.set(cacheKey, cacheAble);
            }
        }
    }
}
