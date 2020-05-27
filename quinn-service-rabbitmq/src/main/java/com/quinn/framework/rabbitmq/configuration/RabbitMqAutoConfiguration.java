package com.quinn.framework.rabbitmq.configuration;

import com.quinn.framework.api.MqListener;
import com.quinn.framework.api.MqService;
import com.quinn.framework.rabbitmq.component.SimpleRabbitServiceImpl;
import com.quinn.util.base.factory.PrefixThreadFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * RabbitMQ 自动配置类
 *
 * @author Qunhua.Liao
 * @since 2020-05-27
 */
@Configuration
public class RabbitMqAutoConfiguration implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    /**
     * 消息转换器类型：Jackson
     */
    private final static int SERIALIZE_TYPE_JACKSON = 2;

    @Value("${com.quinn-service.mq.rabbitmq.auto-listen:true}")
    private boolean autoListen;

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
    @Autowired
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        return template;
    }

    @Bean
    @Autowired
    public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        RabbitAdmin admin = new RabbitAdmin(cf);
        return admin;
    }

    @Bean
    @Autowired
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        if (autoListen) {
            Collection<MqListener> rabbitListeners = applicationContext.getBeansOfType(MqListener.class).values();
            MqService rabbitMQService = applicationContext.getBean(MqService.class);
            Iterator<MqListener> it = rabbitListeners.iterator();
            while (it.hasNext()) {
                rabbitMQService.listen(it.next());
            }
        }
    }

}
