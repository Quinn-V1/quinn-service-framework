package com.quinn.framework.api;

/**
 * BPM节点关系信息
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public interface BpmNodeRelateInfo {

    /**
     * 设置模型ID
     *
     * @param modelId 模型ID
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
     * 设置优先级
     *
     * @param intOne 节点优先级
     */
    void setPriority(Integer intOne);

    /**
     * 生效策略
     *
     * @param effectStrategy 生效策略
     */
    void setEffectStrategy(String effectStrategy);

    /**
     * 来源节点编码
     *
     * @param leadNodeCode 来源节点编码
     */
    void setLeadNodeCode(String leadNodeCode);

    /**
     * 目标节点编码
     *
     * @param followNodeCode 目标节点编码
     */
    void setFollowNodeCode(String followNodeCode);

    /**
     * 设置关系类型
     *
     * @param relateType 关系类型
     */
    void setRelateType(String relateType);


    /**
     * 设置模型ID
     *
     * @return modelId 模型ID
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
     * 设置优先级
     *
     * @return intOne 节点优先级
     */
    Integer getPriority();

    /**
     * 生效策略
     *
     * @return effectStrategy 生效策略
     */
    String getEffectStrategy();

    /**
     * 来源节点编码
     *
     * @return leadNodeCode 来源节点编码
     */
    String getLeadNodeCode();

    /**
     * 目标节点编码
     *
     * @return followNodeCode 目标节点编码
     */
    String getFollowNodeCode();

    /**
     * 设置关系类型
     *
     * @return relateType 关系类型
     */
    String getRelateType();

}
