package com.quinn.framework.api;

import com.quinn.util.base.api.MethodInvokerTwoParam;
import com.quinn.util.base.exception.BaseBusinessException;

/**
 * 有序任务监听器接口
 *
 * @author Qunhua.Liao
 * @since 2020-02-15
 */
public interface SortJobListener {

    /**
     * 生效策略
     *
     * @return 生效策略
     */
    MethodInvokerTwoParam<String, String, Boolean> effectStrategy();

    /**
     * 获取执行顺序
     *
     * @return 执行顺序
     */
    int getOrder();

    /**
     * 监听器名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 任务执行之前
     *
     * @param executionInfo 执行信息
     */
    void jobToBeExecuted(ExecutionInfo executionInfo);

    /**
     * 任务执行取消
     *
     * @param executionInfo 执行信息
     */
    void jobExecutionVetoed(ExecutionInfo executionInfo);

    /**
     * 任务执行结束
     *
     * @param executionInfo 执行信息
     * @param exception     异常
     */
    void jobWasExecuted(ExecutionInfo executionInfo, BaseBusinessException exception);

}
