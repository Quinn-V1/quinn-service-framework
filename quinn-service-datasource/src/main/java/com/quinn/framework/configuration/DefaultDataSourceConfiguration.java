package com.quinn.framework.configuration;

import com.quinn.framework.service.IdGenerateAbleService;
import com.quinn.framework.service.impl.DefaultIdGenerateAbleService;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 默认配置Bean：可以被后面的框架覆盖
 *
 * @author Qunhua.Liao
 * @since 2020-04-18
 */
@Configuration
@AutoConfigureOrder(Integer.MAX_VALUE - 1000)
public class DefaultDataSourceConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = {"idGenerateAbleService"})
    public IdGenerateAbleService idGenerateAbleService() {
        return new DefaultIdGenerateAbleService();
    }

}
