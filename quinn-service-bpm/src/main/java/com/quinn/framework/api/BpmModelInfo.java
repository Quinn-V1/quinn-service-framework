package com.quinn.framework.api;

/**
 * BPM流程模板信息
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public interface BpmModelInfo {

    /**
     * 模型名称
     *
     * @return 模型名称
     */
    String getModelName();

    /**
     * 设置模板名称
     *
     * @param modelName 模板名称
     */
    void setModelName(String modelName);

    /**
     * 模型编码
     *
     * @return 模型编码
     */
    String getModelKey();

    /**
     * 设置BPMKey
     *
     * @param modelKey BPM编码
     */
    void setModelKey(String modelKey);

    /**
     * 设计内容
     *
     * @return 设计内容
     */
    String getDesignContent();

    /**
     * 设置设计内容
     *
     * @param designContent 设计内容
     */
    void setDesignContent(String designContent);

    /**
     * 设置BPM部署主键
     *
     * @param deploymentId 部署主键
     */
    void setBpmDeployKey(String deploymentId);

    /**
     * 设置BPMKey
     *
     * @param bpmKey BPM编码
     */
    void setBpmKey(String bpmKey);

    /**
     * 获取BPM编码
     *
     * @return BPM编码
     */
    String getBpmKey();

    /**
     * 设置BPM版本
     *
     * @param version 版本
     */
    void setBpmVersion(Integer version);

    /**
     * 获取BPM版本
     *
     * @return BPM版本
     */
    Integer getModelVersion();

    /**
     * 生成文件名
     *
     * @return 文件名
     */
    String fileName();

    /**
     * ID
     *
     * @return ID
     */
    Long getId();

}
