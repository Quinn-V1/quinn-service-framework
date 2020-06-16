package com.quinn.framework.api;

/**
 * BPM流程任务信息
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public interface BpmNodeInfo {

    /**
     * 为排序初始化
     */
    void initForSeq();

    /**
     * 设置模板ID
     *
     * @param modelId 设置
     */
    void setModelId(Long modelId);

    /**
     * 设置模型编码
     *
     * @param modelKey 模型编码
     */
    void setModelKey(String modelKey);

    /**
     * 设置模型版本
     *
     * @param modelVersion 模型版本
     */
    void setModelVersion(Integer modelVersion);

    /**
     * 设置节点名称
     *
     * @param nodeName 节点名称
     */
    void setNodeName(String nodeName);

    /**
     * 设置节点类型
     *
     * @param nodeType 节点类型
     */
    void setNodeType(String nodeType);

    /**
     * 节点编码
     *
     * @param nodeCode 节点编码
     */
    void setNodeCode(String nodeCode);


    /**
     * 设置模板ID
     *
     * @return modelId 设置
     */
    Long getModelId();

    /**
     * 设置模型编码
     *
     * @return modelKey 模型编码
     */
    String getModelKey();

    /**
     * 设置模型版本
     *
     * @return modelVersion 模型版本
     */
    Integer getModelVersion();

    /**
     * 设置节点名称
     *
     * @return nodeName 节点名称
     */
    String getNodeName();

    /**
     * 设置节点类型
     *
     * @return nodeType 节点类型
     */
    String getNodeType();

    /**
     * 节点编码
     *
     * @return nodeCode 节点编码
     */
    String getNodeCode();

}
