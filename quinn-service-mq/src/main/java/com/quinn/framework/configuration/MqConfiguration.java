package com.quinn.framework.configuration;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.MqService;
import com.quinn.framework.component.MqWriteEntityServiceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置类
 *
 * @author Qunhua.Liao
 * @since 2020-05-27
 */
@Configuration
public class MqConfiguration {

    @ConditionalOnBean(name = "mqService")
    @Bean("mqWriteEntityServiceInterceptor")
    @Autowired(required = false)
    public EntityServiceInterceptor mqWriteEntityServiceInterceptor(
            MqService mqService
    ) {
        return new MqWriteEntityServiceInterceptor(mqService);
    }

}
