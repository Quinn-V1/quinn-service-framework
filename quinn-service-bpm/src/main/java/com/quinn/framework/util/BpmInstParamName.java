package com.quinn.framework.util;

/**
 * BPM相关参数名
 *
 * @author Qunhua.Liao
 * @since 2020-05-13
 */
public interface BpmInstParamName {

    /**
     * 流程实例ID
     */
    String INSTANCE_ID = "instanceId";

    /**
     * 流程实例ID
     */
    String TASK_ID = "taskId";

    /**
     * 流程实例ID
     */
    String MODEL_ID = "modelId";

    /**
     * 流程实例ID
     */
    String NODE_ID = "nodeId";

    /**
     * 流程实例ID
     */
    String MODEL_KEY = "modelKey";

    /**
     * 流程实例ID
     */
    String MODEL_VERSION = "modelVersion";

    /**
     * 流程实例ID
     */
    String NODE_CODE = "nodeCode";

    /**
     * 流程实例ID
     */
    String NODE_TYPE = "nodeType";

    /**
     * 处理类型
     */
    String DEAL_TYPE = "dealType";

    /**
     * 流程实例ID
     */
    String START_USER = "startUser";

    /**
     * 转办用户
     */
    String TO_USER_KEY = "toUserKey";

    /**
     * 沟通用户
     */
    String TO_USER_KEYS = "toUserKeys";

    /**
     * 沟通意见
     */
    String SUGGESTION = "suggestion";

    /**
     * 流程实例ID
     */
    String ACT_KEY = "bpmKey";

    /**
     * 流程实例ID
     */
    String ACT_KEY_MODEL = "bpmModelKey";

    /**
     * 流程实例ID
     */
    String ACT_KEY_INST = "bpmInstKey";

    /**
     * 流程实例ID
     */
    String ACT_KEY_TASK = "bpmTaskKey";

    /**
     * 流程实例ID
     */
    String ACT_KEY_EXEC = "bpmExecKey";

    /**
     * 流程实例ID
     */
    String BPM_CANDIDATE = "bpmCandidate";

    /**
     * 实例流程图显示路径
     */
    String PROCESS_PIC_URL = "/bpm7/instance/instance-info/download-png-basic?bpmKey=";

    /**
     * 来源节点
     */
    String SOURCE_NODE_CODE = "sourceNodeCode";

    /**
     * 来源审批用户
     */
    String SOURCE_DEAL_USER = "sourceUserKey";
}
