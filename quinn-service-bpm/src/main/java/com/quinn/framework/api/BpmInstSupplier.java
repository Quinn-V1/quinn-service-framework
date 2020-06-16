package com.quinn.framework.api;

import java.io.Serializable;

/**
 * BPM流程实体提供者
 *
 * @author Bpm流程实体提供者
 * @since 2020-06-15
 */
public interface BpmInstSupplier extends Serializable {

    /**
     * 新建流程实例
     *
     * @return 具体流程实例
     */
    BpmInstInfo newBpmInstInfo();

    /**
     * 新建流程任务
     *
     * @return 具体流程任务
     */
    BpmTaskInfo newBpmTaskInfo();

}
