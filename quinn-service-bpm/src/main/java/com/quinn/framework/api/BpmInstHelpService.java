package com.quinn.framework.api;

import java.util.List;

/**
 * BPM实例帮助业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-07-23
 */
public interface BpmInstHelpService {

    /**
     * 选择当前活动的节点
     *
     * @param bpmInstId BPM流程实例ID
     * @return 活动节点编码
     */
    List<String> selectActiveNodeCodes(String bpmInstId);

    /**
     * 选择历史流程的节点
     *
     * @param bpmInstId BPM流程实例ID
     * @return 历史审批节点编码
     */
    List<String> selectHistoryNodeCodes(String bpmInstId);

}
