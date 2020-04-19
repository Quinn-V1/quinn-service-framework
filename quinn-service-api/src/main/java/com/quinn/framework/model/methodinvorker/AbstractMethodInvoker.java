package com.quinn.framework.model.methodinvorker;

import com.quinn.framework.api.methodflag.MethodFlag;
import com.quinn.util.base.model.BaseResult;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 基础
 */
@Getter
@Setter
public abstract class AbstractMethodInvoker<D> {

    /**
     * 全参构造器
     *
     * @param result    结果
     * @param d         参数
     */
    public AbstractMethodInvoker(BaseResult result, D d) {
        this.result = result;
        this.data = d;
    }

    /**
     * 执行完成
     */
    private boolean done;

    /**
     * 过滤器运行下标
     */
    private int interceptorIndex;

    /**
     * 结果
     */
    public BaseResult result;

    /**
     * 数据
     */
    public D data;

    /**
     * 过滤接口：只有拦截器有这些接口中的一个，才会被拦截到
     *
     * @return
     */
    public abstract Class<? extends MethodFlag> interceptWith();

    /**
     * 执行业务方法
     */
    public abstract void invoke();

    /**
     * 过滤器下标偏移
     */
    public void addInterceptorIndex() {
        interceptorIndex++;
    }

}
