package com.quinn.framework.component;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.model.methodinvorker.AbstractMethodInvoker;
import com.quinn.util.base.ClassUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体业务处理拦截器
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
public class EntityServiceInterceptorChain {

    /**
     * 过滤器列表
     */
    private List<EntityServiceInterceptor> entityServiceInterceptors = new ArrayList<>();

    /**
     * 添加拦截器
     *
     * @param entityServiceInterceptor 拦截器
     */
    public void addInterceptor(EntityServiceInterceptor entityServiceInterceptor) {
        entityServiceInterceptors.add(entityServiceInterceptor);
    }

    /**
     * 链式拦截处理
     *
     * @param methodInvoker
     * @param <T>
     */
    public <T> void doChain(AbstractMethodInvoker<T> methodInvoker) {
        while (methodInvoker.getInterceptorIndex() < entityServiceInterceptors.size()) {
            EntityServiceInterceptor entityServiceInterceptor = entityServiceInterceptors
                    .get(methodInvoker.getInterceptorIndex());
            methodInvoker.addInterceptorIndex();

            if (ClassUtil.classIn(methodInvoker.interceptWith(), entityServiceInterceptor.interceptMethod())) {
                entityServiceInterceptor.intercept(this, methodInvoker);
            }
        }

        if (!methodInvoker.isDone() && methodInvoker.getResult().wantContinue()) {
            methodInvoker.invoke();
            methodInvoker.setDone(true);
        }
    }

}
