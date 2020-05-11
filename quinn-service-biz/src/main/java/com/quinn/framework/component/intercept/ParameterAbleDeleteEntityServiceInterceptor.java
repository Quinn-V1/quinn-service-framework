package com.quinn.framework.component.intercept;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.entityflag.ParameterAble;
import com.quinn.framework.api.methodflag.DeleteFlag;
import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.framework.api.methodflag.SaveFlag;
import com.quinn.framework.component.EntityServiceInterceptorChain;
import com.quinn.framework.service.ParameterAbleService;
import com.quinn.util.base.model.BaseResult;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 缓存读过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-03-20
 */
@Component
public class ParameterAbleDeleteEntityServiceInterceptor implements EntityServiceInterceptor {

    @Resource
    private ParameterAbleService parameterAbleService;

    @Override
    public Class<? extends MethodFlag>[] interceptBean() {
        return new Class[]{ParameterAble.class};
    }

    @Override
    public Class<? extends MethodFlag>[] interceptMethod() {
        return new Class[]{DeleteFlag.class};
    }

    @Override
    public <T> void after(EntityServiceInterceptorChain chain, T t, BaseResult result) {
        Object object = result.getData();
        if (result.isSuccess() && object instanceof ParameterAble) {
            ParameterAble parameterAble = (ParameterAble) object;
            BaseResult<ParameterAble> res = parameterAbleService.selectByKey(parameterAble.cacheKey());
            if (res.isSuccess()) {
                parameterAbleService.delete(parameterAble);
            }
        }
    }

}
