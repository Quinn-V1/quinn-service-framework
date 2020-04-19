package com.quinn.framework.model.methodinvorker;

import com.quinn.framework.api.methodflag.InsertFlag;
import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.framework.api.methodflag.WriteFlag;
import com.quinn.framework.entity.data.BaseDO;
import com.quinn.util.base.model.BaseResult;

/**
 * 写操作过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-03-29
 */
public abstract class BaseInsertMethodInvoker<T  extends BaseDO> extends AbstractMethodInvoker<T> implements WriteFlag {

    /**
     * 全参构造器
     *
     * @param result     结果
     * @param baseEntity 参数
     */
    public BaseInsertMethodInvoker(BaseResult result, T baseEntity) {
        super(result, baseEntity);
    }

    @Override
    public Class<? extends MethodFlag> interceptWith() {
        return InsertFlag.class;
    }

}
