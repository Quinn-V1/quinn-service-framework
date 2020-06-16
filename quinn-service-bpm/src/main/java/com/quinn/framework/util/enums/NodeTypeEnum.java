package com.quinn.framework.util.enums;

/**
 * 流程节点枚举类
 *
 * @author Qunhua.Liao
 * @since 2020-05-03
 */
public enum NodeTypeEnum {

    // 起始节点
    startEvent,

    // 结束节点
    endEvent,

    // 用户任务
    userTask,

    // 服务任务
    serviceTask,

    // 分支网关
    exclusiveGateway,

    // 同步网关
    parallelGateway,

    // 连接线
    sequenceFlow,

    // 子流程
    subProcess,

    // 包含网关
    inclusiveGateway,

    ;

}
