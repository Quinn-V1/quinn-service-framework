package com.quinn.framework.api;

import java.io.Serializable;

/**
 * BPM流程参数值信息
 *
 * @author Qunhua.Liao
 * @since 2020-07-22
 */
public interface BpmParamValue extends Serializable {

    /**
     * 获取BPM节点编码
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
     * 获取BPM任务主键
     *
     * @return BPM任务主键
     */
    String getBpmTaskKey();

    /**
     * 设置BPM任务主键
     *
     * @param bpmTaskKey BPM任务主键
     */
    void setBpmTaskKey(String bpmTaskKey);

    /**
     * 获取BPM主键
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
     * 获取BPM实例主键
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
     * 获取参数值
     *
     * @return 参数值
     */
    String getParamName();

    /**
     * 设置参数名
     *
     * @param paramName 参数名
     */
    void setParamName(String paramName);

    /**
     * 获取参数值
     *
     * @return 参数值
     */
    Object getValue();

    /**
     * 设置参数值
     *
     * @param paramValue 参数值
     */
    void setValue(Object paramValue);

}
