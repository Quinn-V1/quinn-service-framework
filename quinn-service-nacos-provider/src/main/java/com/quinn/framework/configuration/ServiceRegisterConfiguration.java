package com.quinn.framework.configuration;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 服务注册配置类
 *
 * @author Qunhua.Liao
 * @since 2020-06-30
 */
@Configuration
@EnableDiscoveryClient
public class ServiceRegisterConfiguration {

    @Bean
    public Object testObject() {
        return new Object();
    }

}
