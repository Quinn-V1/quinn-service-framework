package com.quinn.framework.configuration;

import com.quinn.framework.component.DegreeCacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置类
 *
 * @author Qunhu.Liao
 * @since 2020-06-15
 */
@Configuration
public class CacheConfiguration {

    @Bean("messageDegreeCacheService")
    public DegreeCacheService<String> messageDegreeCacheService() {
        DegreeCacheService degreeCacheService = new DegreeCacheService();
        return degreeCacheService;
    }

}
