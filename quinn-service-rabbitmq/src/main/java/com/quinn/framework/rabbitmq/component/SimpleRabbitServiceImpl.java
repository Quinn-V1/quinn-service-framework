package com.quinn.framework.rabbitmq.component;

import com.quinn.framework.api.ConcurrentMqListener;
import com.quinn.framework.api.MqListener;
import com.quinn.framework.api.MqService;
import com.quinn.framework.api.MqTarget;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.model.BaseResult;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Rabbit Mq 服务端简单实现
 *
 * @author Qunhua.Liao
 * @since 2020-05-26
 */
public class SimpleRabbitServiceImpl implements MqService {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(SimpleRabbitServiceImpl.class);

    protected static final long DEFAULT_SLEEP_MILLIS = 30;

    @Resource
    @Qualifier("mqExecutorService")
    private ScheduledExecutorService mqExecutorService;

    private ConnectionFactory rabbitMQConnectionFactory;

    private SimpleMessageListenerContainer container;

    private RabbitTemplate rabbitTemplate;

    private RabbitAdmin rabbitAdmin;

    private Set<String> declaredQueues = new HashSet<>();

    private Set<String> declaredExchangeAndQueues = new HashSet<>();

    private MessageConverter messageConverter;

    public SimpleRabbitServiceImpl() {
    }

    public SimpleRabbitServiceImpl(ConnectionFactory cf, RabbitTemplate rt, RabbitAdmin admin) {
        this(cf, rt, admin, null);
    }

    public SimpleRabbitServiceImpl(ConnectionFactory cf, RabbitTemplate rt, RabbitAdmin admin, MessageConverter mc) {
        this.rabbitMQConnectionFactory = cf;
        this.container = new SimpleMessageListenerContainer(rabbitMQConnectionFactory);
        this.rabbitTemplate = rt;
        this.rabbitAdmin = admin;
        this.messageConverter = mc;
        if (this.messageConverter != null) {
            rabbitTemplate.setMessageConverter(messageConverter);
        }
    }

    @Override
    public BaseResult listen(final MqListener listener) {
        int concurrent = 1;
        if (listener instanceof ConcurrentMqListener) {
            concurrent = ((ConcurrentMqListener) listener).getConcurrent();
        }

        listen(listener, concurrent);
        return BaseResult.SUCCESS;
    }

    @Override
    public BaseResult listen(final MqListener listener, Integer concurrentConsumers) {
        String targetQueue = listener.getTargetName();
        this.ensureQueueDeclared(targetQueue);

        // 注册监听接口
        MessageListenerAdapter adapter = new MessageListenerAdapter(new Object() {
            @SuppressWarnings("unused")
            public void handleMessage(Object message) {
                try {
                    listener.handleMessage(message);
                } catch (Exception e) {
                    LOGGER.error("MQ listener handle method exception " + e.getMessage(), e);
                } finally {
                }
            }
        });

        if (messageConverter != null) {
            adapter.setMessageConverter(messageConverter);
        }

        container.setMessageListener(adapter);
        container.setQueueNames(listener.getTargetName());
        if (concurrentConsumers != null && concurrentConsumers.intValue() > 1) {
            container.setConcurrentConsumers(concurrentConsumers);
        }

        // ACK_MODE_AUTO 同 default
        switch (listener.getListenerMode()) {
            case MqListener.ACK_MODE_MANUAL:
                container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
                break;
            case MqListener.ACK_MODE_NONE:
                container.setAcknowledgeMode(AcknowledgeMode.NONE);
                break;
            default:
                container.setAcknowledgeMode(AcknowledgeMode.AUTO);
                break;
        }

        container.start();
        return new BaseResult();
    }

    @Override
    public BaseResult receive(final MqListener listener, int threadCount) {
        for (int i = 0; i < threadCount; i++) {
            mqExecutorService.execute(() -> {
                while (true) {
                    Object o = rabbitTemplate.receiveAndConvert(listener.getTargetName());
                    if (o != null) {
                        listener.handleMessage(o);
                        continue;
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(DEFAULT_SLEEP_MILLIS);
                    } catch (InterruptedException e) {
                    }
                }
            });
        }
        return new BaseResult();
    }

    @Override
    public BaseResult remove(MqListener listener) {
        container.removeQueueNames(listener.getTargetName());
        declaredQueues.remove(listener.getTargetName());
        return BaseResult.SUCCESS;
    }

    @Override
    public BaseResult send(String queueName, Object data) {
        return send(queueName, queueName, RabbitMqMessageTarget.ExchangeTypeEnum.DIRECT, data, new String[]{queueName});
    }

    @Override
    public BaseResult send(MqTarget target, Object data) {
        if (!(target instanceof RabbitMqMessageTarget)) {
            throw new BaseBusinessException();
        }

        RabbitMqMessageTarget rabbitMQMessageTarget = (RabbitMqMessageTarget) target;
        return send(rabbitMQMessageTarget.getExchangeName(), rabbitMQMessageTarget.getRoutingKey(),
                rabbitMQMessageTarget.getExchangeTypeEnum(), data, rabbitMQMessageTarget.getQueueNames());
    }

    /**
     * 发送消息
     *
     * @param exchangeName 绑定交换器名
     * @param routingKey   路由Key
     * @param exchangeType 交换器类型
     * @param data         数据
     * @param queueNames   队列名
     * @return 发送结果
     */
    protected BaseResult send(String exchangeName, String routingKey,
                              RabbitMqMessageTarget.ExchangeTypeEnum exchangeType, Object data, String... queueNames) {
        if (StringUtil.isEmpty(exchangeName)) {
            throw new IllegalArgumentException("exchange or routingKey must not be null");
        }

        this.declareExchangeAndQueue(exchangeName, exchangeType, routingKey, queueNames);

        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, data);
        } catch (AmqpException e) {
            LOGGER.error("RabbitMQ send exception" + e.getMessage(), e);
            throw e;
        } finally {
        }
        return BaseResult.SUCCESS;
    }

    /**
     * 声明简单队列（监听过程中）
     *
     * @param queueName 队列名
     */
    private void ensureQueueDeclared(String queueName) {
        if (!declaredQueues.contains(queueName)) {
            Queue queue = new Queue(queueName);
            queue.setAdminsThatShouldDeclare(rabbitAdmin);
            rabbitAdmin.declareQueue(queue);
            declaredQueues.add(queueName);
        }
    }

    /**
     * 声明复杂队列（发送过程中）
     *
     * @param exchangeName     交换器名
     * @param exchangeTypeEnum 交换器类型
     * @param routingKey       路由名
     * @param queueNames       队列名
     */
    private void declareExchangeAndQueue(String exchangeName, RabbitMqMessageTarget.ExchangeTypeEnum exchangeTypeEnum,
                                         String routingKey, String... queueNames) {
        if (queueNames != null && queueNames.length > 0) {
            for (String queueName : queueNames) {
                if (!declaredExchangeAndQueues.contains(exchangeName + "|" + queueName)) {
                    Queue queue = new Queue(queueName);
                    queue.setAdminsThatShouldDeclare(rabbitAdmin);
                    rabbitAdmin.declareQueue(queue);

                    // FANOUT 同 default
                    switch (exchangeTypeEnum) {
                        case TOPIC:
                            TopicExchange topicExchange = new TopicExchange(exchangeName);
                            rabbitAdmin.declareExchange(topicExchange);
                            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(routingKey));
                            break;
                        case DIRECT:
                            DirectExchange directExchange = new DirectExchange(exchangeName);
                            rabbitAdmin.declareExchange(directExchange);
                            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(routingKey));
                            break;
                        default:
                            FanoutExchange exchange = new FanoutExchange(exchangeName);
                            rabbitAdmin.declareExchange(exchange);
                            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange));
                            break;
                    }
                    declaredExchangeAndQueues.add(exchangeName + "|" + queueName);
                }
            }
        }
    }
}
