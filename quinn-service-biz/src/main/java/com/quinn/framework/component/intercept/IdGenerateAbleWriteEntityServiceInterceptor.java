package com.quinn.framework.component.intercept;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.entityflag.IdGenerateAble;
import com.quinn.framework.api.methodflag.InsertFlag;
import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.framework.component.EntityServiceInterceptorChain;
import com.quinn.framework.service.IdGenerateAbleService;
import com.quinn.util.base.NumberUtil;
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
public class IdGenerateAbleWriteEntityServiceInterceptor implements EntityServiceInterceptor {

    @Resource
    private IdGenerateAbleService idGenerateAbleService;

    @Override
    public Class<? extends MethodFlag>[] interceptBean() {
        return new Class[]{IdGenerateAble.class};
    }

    @Override
    public Class<? extends MethodFlag>[] interceptMethod() {
        return new Class[]{InsertFlag.class};
    }

    @Override
    public <T> void before(EntityServiceInterceptorChain chain, T t, BaseResult result) {
        Object object = result.getData();
        if (result.isSuccess() && t instanceof IdGenerateAble) {
            IdGenerateAble idGenerateAble = (IdGenerateAble) object;
            if (NumberUtil.isEmptyInFrame(idGenerateAble.getId())) {
                idGenerateAbleService.generateId(idGenerateAble);
            }
        }
    }

}
