package com.quinn.framework.configuration;

import com.quinn.framework.compnonent.DefaultMessageApiService;
import com.quinn.framework.compnonent.DefaultMessageSendService;
import com.quinn.framework.service.MessageApiService;
import com.quinn.framework.service.MessageSendService;
import com.quinn.util.base.factory.PrefixThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 默认配置Bean：可以被后面的框架覆盖
 *
 * @author Qunhua.Liao
 * @since 2020-04-18
 */
@Configuration
@AutoConfigureOrder(Integer.MAX_VALUE - 2000)
public class DefaultMessageConfiguration {

    @Value("${com.quinn-service.message.thread-pool-size:2}")
    private int coreTheadSize;

    @Bean
    @ConditionalOnMissingBean(value = MessageApiService.class)
    public MessageApiService messageApiService() {
        return new DefaultMessageApiService();
    }

    @Bean
    @ConditionalOnMissingBean(value = MessageSendService.class)
    public MessageSendService messageSendService() {
        return new DefaultMessageSendService();
    }

    @Bean
    @ConditionalOnMissingBean(name = {"messageExecutorService"})
    public ScheduledExecutorService messageExecutorService() {
        return new ScheduledThreadPoolExecutor(coreTheadSize, new PrefixThreadFactory("message-pool-"),
                (r, executor) -> {
                    // TODO 超出处理能力保障
                });
    }

}
