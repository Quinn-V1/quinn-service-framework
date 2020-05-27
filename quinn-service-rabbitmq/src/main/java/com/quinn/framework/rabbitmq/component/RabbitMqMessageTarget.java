package com.quinn.framework.rabbitmq.component;

import com.quinn.framework.api.MqTarget;

/**
 * Rabbit MQ 消息发送目标
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public class RabbitMqMessageTarget implements MqTarget {

    protected RabbitMqMessageTarget() {
    }

    protected RabbitMqMessageTarget(String exchangeType, String exchangeName, String routingKey,
                                    String... queueNames) {
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.exchangeType = exchangeType;
        this.queueNames = queueNames;
    }

    /**
     * <p>返回一个向指定queue发送消息的信息对象，其中exchange默认与queue名称一致，
     * exchangeType为direct类型。</p>
     *
     * @param exchangeType 交换器类型
     * @param exchangeName 交换器名称
     * @param routingKey   路由
     * @param queueNames   队列
     * @return 向指定queue发送数据的信息类型对象
     */
    public static final RabbitMqMessageTarget createTarget(String exchangeType, String exchangeName,
                                                           String routingKey, String[] queueNames) {
        return new RabbitMqMessageTarget(exchangeType, exchangeName, routingKey, queueNames);
    }

    /**
     * 交换器类型
     */
    private String exchangeType;

    /**
     * 交换器名称
     */
    private String exchangeName;

    /**
     * 路由规则
     */
    private String routingKey;

    /**
     * 队列名称
     */
    private String[] queueNames;

    @Override
    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    @Override
    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    @Override
    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    @Override
    public String[] getQueueNames() {
        return queueNames;
    }

    public void setQueueNames(String[] queueNames) {
        this.queueNames = queueNames;
    }

}
