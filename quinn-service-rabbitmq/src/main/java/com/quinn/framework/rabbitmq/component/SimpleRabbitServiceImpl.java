package com.quinn.framework.rabbitmq.component;

import com.quinn.framework.api.ConcurrentMqListener;
import com.quinn.framework.api.MqListener;
import com.quinn.framework.api.MqService;
import com.quinn.framework.api.MqTarget;
import com.quinn.framework.util.enums.ExchangeTypeEnum;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.enums.CommMessageEnum;
import com.quinn.util.base.exception.DataStyleNotMatchException;
import com.quinn.util.base.exception.ParameterShouldNotEmpty;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.StringConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

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

    @Value("${com.quinn-service.mq.rabbitmq.receive-interval:30}")
    private long receiveInterval;

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
    public RabbitMqMessageTarget createTarget(String exchangeType, String exchangeName, String routingKey,
                                              String... queueNames) {
        return RabbitMqMessageTarget.createTarget(exchangeType, exchangeName, routingKey, queueNames);
    }

    @Override
    public BaseResult sendDirect(Object data, String... queueNames) {
        return send(data, ExchangeTypeEnum.DIRECT.name(), StringConstant.ALL_OF_DATA, null, queueNames);
    }

    @Override
    public BaseResult send(Object data, MqTarget target) {
        if (!(target instanceof RabbitMqMessageTarget)) {
            throw new DataStyleNotMatchException()
                    .addParamI8n(CommMessageEnum.DATA_STYLE_NOT_MATCHED.paramNames[0], RabbitMqMessageTarget.class)
                    .addParam(CommMessageEnum.DATA_STYLE_NOT_MATCHED.paramNames[1], data)
                    .exception()
                    ;
        }

        RabbitMqMessageTarget rabbitMQMessageTarget = (RabbitMqMessageTarget) target;
        return send(data, rabbitMQMessageTarget.getExchangeType(), rabbitMQMessageTarget.getExchangeName(),
                rabbitMQMessageTarget.getRoutingKey(), rabbitMQMessageTarget.getQueueNames());
    }

    @Override
    public BaseResult send(Object data, String exchangeType, String exchangeName, String routingKey,
                           String... queueNames) {

        if (StringUtil.isEmpty(exchangeName)) {
            throw new ParameterShouldNotEmpty()
                    .addParam(CommMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], exchangeName)
                    .exception()
                    ;
        }

        this.declareExchangeAndQueue(exchangeType, exchangeName, routingKey, queueNames);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, data);

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
     * @param exchangeName 交换器名
     * @param exchangeType 交换器类型
     * @param routingKey   路由名
     * @param queueNames   队列名
     */
    private void declareExchangeAndQueue(String exchangeType, String exchangeName,
                                         String routingKey, String... queueNames) {
        if (queueNames != null && queueNames.length > 0) {
            for (String queueName : queueNames) {
                String key = exchangeName + StringConstant.CHAR_VERTICAL_BAR + queueName;

                if (!declaredExchangeAndQueues.contains(key)) {
                    Queue queue = new Queue(queueName);
                    queue.setAdminsThatShouldDeclare(rabbitAdmin);
                    rabbitAdmin.declareQueue(queue);

                    // FANOUT 同 default
                    switch (exchangeType) {
                        case "TOPIC":
                            TopicExchange topicExchange = new TopicExchange(exchangeName);
                            rabbitAdmin.declareExchange(topicExchange);
                            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(routingKey));
                            break;
                        case "DIRECT":
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
                    declaredExchangeAndQueues.add(key);
                }
            }
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
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
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
        return BaseResult.SUCCESS;
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
                        TimeUnit.MILLISECONDS.sleep(receiveInterval);
                    } catch (InterruptedException e) {
                    }
                }
            });
        }
        return BaseResult.SUCCESS;
    }

    @Override
    public BaseResult remove(MqListener listener) {
        container.removeQueueNames(listener.getTargetName());
        declaredQueues.remove(listener.getTargetName());
        return BaseResult.SUCCESS;
    }
}
