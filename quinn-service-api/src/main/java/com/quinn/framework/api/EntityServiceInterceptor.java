package com.quinn.framework.api;

import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.framework.component.EntityServiceInterceptorChain;
import com.quinn.framework.model.methodinvorker.AbstractMethodInvoker;
import com.quinn.util.base.model.BaseResult;

/**
 * 实体操作拦截器
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
public interface EntityServiceInterceptor {

    /**
     * 拦截接口列表
     *
     * @return  拦截接口列表
     */
    Class<? extends MethodFlag>[] interceptBean();

    /**
     * 拦截接口列表
     *
     * @return  拦截接口列表
     */
    Class<? extends MethodFlag>[] interceptMethod();

    /**
     * 拦截操作
     *
     * @param chain         拦截链
     * @param methodInvoker 操作对象
     * @param <T>           实体泛型
     */
    default <T> void intercept(EntityServiceInterceptorChain chain, AbstractMethodInvoker<T> methodInvoker) {
        before(chain, methodInvoker.getData(), methodInvoker.getResult());
        if (!methodInvoker.getResult().wantContinue()) {
            return;
        }
        chain.doChain(methodInvoker);

        if (!methodInvoker.isDone()) {
            methodInvoker.invoke();
            methodInvoker.setDone(true);
        }

        after(chain, methodInvoker.getData(), methodInvoker.getResult());
    }

    /**
     * 在新增之前的操作
     *
     * @param chain  拦截链
     * @param t      保存实体
     * @param result 结果
     * @param <D>    实体泛型
     */
    default <D> void before(EntityServiceInterceptorChain chain, D t, BaseResult result) {
    }

    /**
     * 在新增之后的操作
     *
     * @param chain  拦截链
     * @param t      保存实体
     * @param result 结果
     * @param <V>    实体泛型
     */
    default <V> void after(EntityServiceInterceptorChain chain, V t, BaseResult result) {
    }

}
