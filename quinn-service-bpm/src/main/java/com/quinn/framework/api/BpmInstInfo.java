package com.quinn.framework.api;

/**
 * BPM流程实例信息
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public interface BpmInstInfo {

    /**
     * 获取发起用户
     *
     * @return 发起用户
     */
    String getStartUser();

    /**
     * 流程模型编码（BPM的）
     *
     * @return 流程模型编码
     */
    String getBpmModelKey();

    /**
     * 流程实例ID
     *
     * @return 实例ID
     */
    Long getId();

    /**
     * 流程模型编码（内部的）
     *
     * @return 流程模型编码
     */
    String getModelKey();

    /**
     * 流程主题
     *
     * @return 主题
     */
    String getSubject();

    /**
     * 流程状态
     *
     * @param instStatus 流程状态
     */
    void setInstStatus(String instStatus);

    /**
     * 流程实例BPM Key
     *
     * @param bpmKey 流程实例编码
     */
    void setBpmKey(String bpmKey);
}
