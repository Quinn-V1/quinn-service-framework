package com.quinn.framework.model.methodinvorker;

import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.framework.api.methodflag.ReadFlag;
import com.quinn.util.base.model.BaseResult;

/**
 * 通用去操作过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-03-29
 */
public abstract class BaseReadMethodInvoker<T> extends AbstractMethodInvoker<T> implements ReadFlag {

    /**
     * 全参构造器
     *
     * @param result     结果
     * @param baseEntity 参数
     */
    public BaseReadMethodInvoker(BaseResult result, T baseEntity) {
        super(result, baseEntity);
    }

    @Override
    public Class<? extends MethodFlag> interceptWith() {
        return ReadFlag.class;
    }
}
