package com.quinn.framework.api;

/**
 * BPM节点关系综合类
 *
 * @author Qunhua.Liao
 * @since 2020-06-15
 */
public interface BpmNodeRelateBO {

    /**
     * 添加BPM节点关系
     *
     * @param relate 节点关系
     */
    void addRelate(BpmNodeRelateInfo relate);

    /**
     * 添加节点信息
     *
     * @param nodeInfo 节点信息
     */
    void addNode(BpmNodeInfo nodeInfo);

    /**
     * 设置起始节点
     *
     * @param nodeInfo 起始节点
     */
    void setStartNode(BpmNodeInfo nodeInfo);

}
