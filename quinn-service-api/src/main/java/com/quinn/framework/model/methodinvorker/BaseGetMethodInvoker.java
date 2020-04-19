package com.quinn.framework.model.methodinvorker;

import com.quinn.framework.api.methodflag.GetFlag;
import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.util.base.model.BaseResult;

/**
 * 单个取操作过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-03-29
 */
public abstract class BaseGetMethodInvoker<T> extends BaseReadMethodInvoker<T> implements GetFlag {

    /**
     * 全参构造器
     *
     * @param result     结果
     * @param baseEntity 参数
     */
    public BaseGetMethodInvoker(BaseResult result, T baseEntity) {
        super(result, baseEntity);
    }

    @Override
    public Class<? extends MethodFlag> interceptWith() {
        return GetFlag.class;
    }
}
