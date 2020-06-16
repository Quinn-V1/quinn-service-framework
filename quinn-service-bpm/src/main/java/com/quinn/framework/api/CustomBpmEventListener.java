package com.quinn.framework.api;

import com.quinn.util.base.model.BatchResult;

/**
 * 自定义Activiti监听器
 *
 * @author Qunhua.Liao
 * @since 2020-05-06
 */
public interface CustomBpmEventListener {

    /**
     * 监听事件
     *
     * @return 监听事件
     */
    String eventType();

    /**
     * 监听工作
     *
     * @param bpmTaskInfo         监听目标
     * @param executionId         执行ID
     * @param processInstanceId   流程实例ID
     * @param processDefinitionId 流程定义ID
     * @return 监听结果
     */
    BatchResult listen(BpmTaskInfo bpmTaskInfo, String executionId, String processInstanceId, String processDefinitionId);

}
