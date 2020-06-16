package com.quinn.framework.util.enums;

/**
 * 脚本位置枚举类
 *
 * @author Qunhua.Liap
 * @since 2020-05-02
 */
public enum BpmEventTypeEnum {

    // 流程启动之后
    POST_INST_START,

    // 流程结束之后
    POST_INST_END,

    // 任务创建之后
    POST_TASK_CREATE,

    // 任务处理之后
    POST_TASK_DEAL,

    // 服务节点（主逻辑）
    SERVICE_DELEGATED,

    // 历史任务创建
    HISTORIC_ACTIVITY_INSTANCE_CREATED,

    // 历史任务完成
    HISTORIC_ACTIVITY_INSTANCE_ENDED,

    // 任务创建
    ACTIVITY_STARTED,

    // 流程完成
    PROCESS_COMPLETED,

    // 流程启动
    PROCESS_STARTED,

    // 流程启动
    TASK_ASSIGNED,

    // 任务完成
    TASK_COMPLETED,

    // 任务完成
    TASK_CREATED,

    ;
}
