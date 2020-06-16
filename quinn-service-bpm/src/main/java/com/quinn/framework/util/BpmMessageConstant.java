package com.quinn.framework.util;

import com.quinn.util.constant.CommonParamName;

/**
 * BPM消息描述
 *
 * @author Quinn.Liao
 * @since 2020-05-25
 */
public interface BpmMessageConstant {

    /**
     * 任务不是你的
     */
    String DESC_INST_IS_NOT_YOURS = "流程实例【${" + BpmParamName.INSTANCE_ID + "}】非您所有，请核对";

    /**
     * 任务不是你的
     */
    String DESC_TASK_IS_NOT_YOURS = "任务【${" + BpmParamName.INSTANCE_ID + "} - ${"
            + BpmParamName.TASK_ID + "}】非您所有，请核对";

    /**
     * 任务不存在
     */
    String DESC_TASK_NOT_EXITS = "任务【${" + BpmParamName.TASK_ID + "}】不存在";

    /**
     * 流程缺失必要数据
     */
    String DESC_INSTANCE_DATA_LOST = "流程【${" + BpmParamName.INSTANCE_ID + "}】缺失${"
            + CommonParamName.PARAM_DATA_TYPE + "}数据";

    /**
     * 历史节点不存在已办任务
     */
    String DESC_NODE_HISTORY_TASK_NOT_EXISTS = "历史节点【${" + BpmParamName.NODE_CODE + "}】不存在已办任务";

    /**
     * 历史节点不存在已办任务
     */
    String DESC_DEAL_TYPE_NOT_SUPPORT_NOW = "当前节点不支持【${" + BpmParamName.DEAL_TYPE + "}】操作";

    /**
     * 流程模板操作异常
     */
    String DESC_MODEL_INFO_OPERATION_ERROR = "【${" + CommonParamName.PARAM_OPERATE_TYPE + "}】流程失败，【${"
            + CommonParamName.PARAM_DATA_TYPE + "}】数据【${" + CommonParamName.PARAM_DATA_KEY + "}】异常【${"
            + CommonParamName.PARAM_EXPECT_CLASS + "}】";

    /**
     * 任务实例
     */
    String INST_TASK_CANDIDATE = "任务候选人";

    /**
     * 任务实例
     */
    String DESC_INST_TASK = "任务实例";

    /**
     * 流程实例
     */
    String DESC_INST_INFO = "流程实例";

    /**
     * 实流模板节点
     */
    String DESC_MODEL_NODE = "实流模板节点";

}
