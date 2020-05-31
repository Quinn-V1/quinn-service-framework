package com.quinn.framework.api;

/**
 * 触发器中的执行消息
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public interface TriggerExecuteInfo extends ExecutionInfo {

    /**
     * 获取任务编码
     *
     * @return 任务编码
     */
    String getJobKey();

    /**
     * 获取任务分组
     *
     * @return 任务分组
     */
    String getJobGroup();

}
