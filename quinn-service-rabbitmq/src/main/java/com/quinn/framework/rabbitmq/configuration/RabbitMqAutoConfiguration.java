package com.quinn.framework.rabbitmq.configuration;

import com.quinn.framework.api.MqService;
import com.quinn.framework.rabbitmq.component.SimpleRabbitServiceImpl;
import com.quinn.util.base.factory.PrefixThreadFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * RabbitMQ 自动配置类
 *
 * @author Qunhua.Liao
 * @since 2020-05-27
 */
@Configuration
public class RabbitMqAutoConfiguration {

    /**
     * 消息转换器类型：Jackson
     */
    private final static int SERIALIZE_TYPE_JACKSON = 2;

    @Value("${com.quinn-service.mq.rabbitmq.thread-pool-size:2}")
    private int coreTheadSize;

    @Value("${com.quinn-service.mq.rabbitmq.host:127.0.0.1}")
    private String host;

    @Value("${com.quinn-service.mq.rabbitmq.port:5672}")
    private int port;

    @Value("${com.quinn-service.mq.rabbitmq.addresses:}")
    private String addresses;

    @Value("${com.quinn-service.mq.rabbitmq.username:guest}")
    private String username;

    @Value("${com.quinn-service.mq.rabbitmq.password:guest}")
    private String password;

    @Value("${com.quinn-service.mq.rabbitmq.virtual-host:/}")
    private String virtualHost;

    @Value("${com.quinn-service.mq.rabbitmq.serialize-type:1}")
    private int serializationType;

    @Value("${com.quinn-service.mq.rabbitmq.max-channels:25}")
    private int maxChannels;

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory cf = new CachingConnectionFactory();
        cf.setHost(host);
        cf.setPassword(password);
        cf.setUsername(username);
        cf.setPort(port);
        cf.setVirtualHost(virtualHost);
        cf.setAddresses(addresses);
        cf.setChannelCacheSize(maxChannels);
        return cf;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        return template;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        RabbitAdmin admin = new RabbitAdmin(cf);
        return admin;
    }

    @Bean(name = {"rabbitMQService", "mqService"})
    public MqService rabbitMQService(
            ConnectionFactory rabbitConnectionFactory,
            RabbitTemplate rabbitTemplate,
            RabbitAdmin admin) {
        if (serializationType == SERIALIZE_TYPE_JACKSON) {
            return new SimpleRabbitServiceImpl(rabbitConnectionFactory, rabbitTemplate, admin,
                    new Jackson2JsonMessageConverter());
        } else {
            return new SimpleRabbitServiceImpl(rabbitConnectionFactory, rabbitTemplate, admin);
        }

    }

    @Bean
    @ConditionalOnMissingBean(name = {"mqExecutorService"})
    public ScheduledExecutorService mqExecutorService() {
        return new ScheduledThreadPoolExecutor(coreTheadSize, new PrefixThreadFactory("mq-pool-"),
                (r, executor) -> {
                    // TODO 超出处理能力保障
                });
    }

}
