package com.quinn.framework.configuration;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 因为@Value的注入不能在BeanPostProcessor中执行, @Configuration的对象生命周期取决于其定义的@Bean的生命周期,
 * LifecycleBeanPostProcessor是一个BeanPostProcessor,
 * 需要把它移出来, 否则@Value取不到值(ShiroAutoConfiguration.workMode)
 *
 * @author Quinn.Liao<br>
 * @version <strong>v1.0.0</strong><br>
 */
@Configuration
public class QuinnLifecycleConfiguration {

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
