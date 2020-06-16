package com.quinn.framework.api;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * BPM流程任务信息
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public interface BpmTaskInfo extends Serializable {

    /**
     * 获取节点ID
     *
     * @return 节点ID
     */
    Long getNodeId();

    /**
     * 获取模型ID
     *
     * @return 模型ID
     */
    Long getModelId();

    /**
     * 获取任务主键
     *
     * @return 任务主键
     */
    Long getId();

    /**
     * 获取BPM任务主键
     *
     * @return BPM任务主键
     */
    String getBpmKey();

    /**
     * 设置BPM主键
     *
     * @param bpmKey BPM主键
     */
    void setBpmKey(String bpmKey);

    /**
     * 设置BPM主键
     *
     * @return BPM执行主键
     */
    String getBpmExecKey();

    /**
     * 设置BPM主键
     *
     * @param bpmExecKey BPM执行主键
     */
    void setBpmExecKey(String bpmExecKey);

    /**
     * 设置BPM实例主键
     *
     * @return BPM执行实例主键
     */
    String getBpmInstKey();

    /**
     * 设置BPM实例主键
     *
     * @param bpmExecKey BPM执行实例主键
     */
    void setBpmInstKey(String bpmExecKey);

    /**
     * 设置BPM模板主键
     *
     * @return BPM执行模板主键
     */
    String getBpmModelKey();

    /**
     * 设置BPM模板主键
     *
     * @param bpmExecKey BPM执行模板主键
     */
    void setBpmModelKey(String bpmExecKey);

    /**
     * 设置BPM节点编码
     *
     * @return BPM节点编码
     */
    String getNodeCode();

    /**
     * 设置BPM节点编码
     *
     * @param nodeCode BPM节点编码
     */
    void setNodeCode(String nodeCode);

    /**
     * 设置BPM节点名称
     *
     * @return BPM节点名称
     */
    String getNodeName();

    /**
     * 设置BPM节点名称
     *
     * @param nodeName BPM节点名称
     */
    void setNodeName(String nodeName);

    /**
     * 获取审批人
     *
     * @return 审批人
     */
    String getAssignee();

    /**
     * 设置审批人
     *
     * @param assignee 审批人
     */
    void setAssignee(String assignee);

    /**
     * 获取节点类型
     *
     * @return 节点类型
     */
    String getNodeType();

    /**
     * 设置节点类型
     *
     * @param nodeType 节点类型
     */
    void setNodeType(String nodeType);

    /**
     * 获取参数
     *
     * @return 参数
     */
    Map<String, Object> getParams();

    /**
     * 设置参数
     *
     * @param params 参数
     */
    void setParams(Map<String, Object> params);

    /**
     * 获取流程实例ID
     *
     * @return 流程实例ID
     */
    Long getInstanceId();

    /**
     * 流程实例ID
     *
     * @param instanceId 流程实例ID
     */
    void setInstanceId(Long instanceId);

    /**
     * 设置待办类型
     *
     * @param todoType 待办类型
     */
    void setTodoType(String todoType);

    /**
     * 获取待办类型
     *
     * @return 待办类型
     */
    String getTodoType();

    /**
     * 任务创建事件
     *
     * @return 任务创建事件
     */
    LocalDateTime getCreateDateTime();

}
