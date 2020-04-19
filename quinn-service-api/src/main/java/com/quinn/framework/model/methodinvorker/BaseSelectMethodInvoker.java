package com.quinn.framework.model.methodinvorker;

import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.framework.api.methodflag.SelectFlag;
import com.quinn.util.base.model.BaseResult;

/**
 * 列表取操作过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-03-29
 */
public abstract class BaseSelectMethodInvoker<T> extends BaseReadMethodInvoker<T> implements SelectFlag {

    /**
     * 全参构造器
     *
     * @param result     结果
     * @param baseEntity 参数
     */
    public BaseSelectMethodInvoker(BaseResult result, T baseEntity) {
        super(result, baseEntity);
    }

    @Override
    public Class<? extends MethodFlag> interceptWith() {
        return SelectFlag.class;
    }
}
