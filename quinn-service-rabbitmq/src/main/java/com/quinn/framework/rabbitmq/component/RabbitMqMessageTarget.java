package com.quinn.framework.rabbitmq.component;

/**
 * Rabbit MQ 消息发送目标
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public class RabbitMqMessageTarget {

    /**
     * <p>返回一个向指定queue发送消息的信息对象，其中exchange默认与queue名称一致，
     * exchangeType为direct类型。</p>
     *
     * @param queueName queue名称
     * @return 向指定queue发送数据的信息类型对象
     */
    public static final RabbitMqMessageTarget createDirectTarget(String queueName) {
        return new RabbitMqMessageTarget(queueName, queueName, ExchangeTypeEnum.DIRECT, queueName);
    }

    /**
     * <p>返回一个向指定queue发送消息的信息对象，exchangeType为fanout类型，
     * 所有指定的queue都将接收到发送的消息。</p>
     *
     * @param exchangeName exchange名称
     * @param queueNames   接收exchange消息的队列名称
     * @return
     */
    public static final RabbitMqMessageTarget createFanoutTarget(String exchangeName, String... queueNames) {
        return new RabbitMqMessageTarget(exchangeName, null, ExchangeTypeEnum.FANOUT, queueNames);
    }

    /**
     * <p>返回一个向指定queue发送消息的信息对象，exchangeType为topic类型，
     * 所有指定的queue都将接收到发送的消息。</p>
     *
     * @param exchangeName
     * @param routingKey
     * @param queueNames
     * @return
     */
    public static final RabbitMqMessageTarget createTopicTarget(String exchangeName, String routingKey, String... queueNames) {
        return new RabbitMqMessageTarget(exchangeName, routingKey, ExchangeTypeEnum.TOPIC, queueNames);
    }

    private String[] queueNames;

    private String exchangeName;

    private String routingKey;

    private ExchangeTypeEnum exchangeType;

    protected RabbitMqMessageTarget() {

    }

    protected RabbitMqMessageTarget(String exchangeName, String routingKey, ExchangeTypeEnum exchangeType,
                                    String... queueNames) {
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.exchangeType = exchangeType;
        this.queueNames = queueNames;
    }

    public String[] getQueueNames() {
        return queueNames;
    }

    public void setQueueNames(String[] queueNames) {
        this.queueNames = queueNames;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public ExchangeTypeEnum getExchangeTypeEnum() {
        return exchangeType;
    }

    public void setExchangeTypeEnum(ExchangeTypeEnum exchangeType) {
        this.exchangeType = exchangeType;
    }

    public enum ExchangeTypeEnum {
        // 交换器类型：订阅发送
        TOPIC,

        // 直接发送
        DIRECT,

        // ？
        FANOUT
    }

}
