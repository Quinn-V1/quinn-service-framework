package com.quinn.framework.api;

/**
 * BPM流程实体提供者
 *
 * @author Bpm流程实体提供者
 * @since 2020-06-15
 */
public interface BpmModelSupplier {

    /**
     * 新建流程模型
     *
     * @return 具体流程模型
     */
    BpmModelInfo newBpmModelInfo();

    /**
     * 新建流程节点
     *
     * @return 具体流程节点
     */
    BpmNodeInfo newBpmNodeInfo();

    /**
     * 新建流程节点
     *
     * @return 具体流程节点
     */
    BpmNodeRelateInfo newBpmNodeRelateInfo();

}
