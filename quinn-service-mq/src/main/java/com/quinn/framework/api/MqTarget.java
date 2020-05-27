package com.quinn.framework.api;

/**
 * 消息发送目标
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public interface MqTarget {

    /**
     * 交换器类型
     *
     * @return 交换器类型
     */
    String getExchangeType();

    /**
     * 交换器名称
     *
     * @return 交换器名称
     */
    String getExchangeName();

    /**
     * 路由规则
     *
     * @return 路由规则
     */
    String getRoutingKey();

    /**
     * 队列名称
     *
     * @return 队列名称
     */
    String[] getQueueNames();
}
