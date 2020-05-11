package com.quinn.framework.component.intercept;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.entityflag.AuditAble;
import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.framework.api.methodflag.WriteFlag;
import com.quinn.framework.component.EntityServiceInterceptorChain;
import com.quinn.framework.service.AuditAbleService;
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
public class AuditAbleWriteEntityServiceInterceptor implements EntityServiceInterceptor {

    @Resource
    private AuditAbleService auditAbleService;

    @Override
    public Class<? extends MethodFlag>[] interceptBean() {
        return new Class[]{AuditAble.class};
    }

    @Override
    public Class<? extends MethodFlag>[] interceptMethod() {
        return new Class[]{WriteFlag.class};
    }

    @Override
    public <T> void before(EntityServiceInterceptorChain chain, T t, BaseResult result) {
        Object object = result.getData();
        if (result.isSuccess() && t instanceof AuditAble) {
            AuditAble auditAble = (AuditAble) object;
            auditAbleService.auditLog(auditAble, (AuditAble) t);
        }
    }
}
