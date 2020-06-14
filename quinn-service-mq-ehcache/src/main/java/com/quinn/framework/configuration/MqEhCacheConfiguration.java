package com.quinn.framework.configuration;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.MqListener;
import com.quinn.framework.api.MqService;
import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.framework.api.cache.CacheServiceManager;
import com.quinn.framework.component.EhCacheServiceManager;
import com.quinn.framework.component.MqWriteEntityServiceInterceptor;
import com.quinn.framework.listener.EhCacheRefreshListener;
import lombok.SneakyThrows;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

/**
 * EhCache 配置类
 *
 * @author Qunhua.Liao
 * @since 2020-05-24
 */
@Configuration
public class MqEhCacheConfiguration {

    @Bean("ehCacheRefreshListener")
    @ConditionalOnExpression("'${com.quinn-service.cache.main-cache-name:ehCacheAllService}'=='ehCacheAllService'")
    public MqListener ehCacheRefreshListener() {
        return new EhCacheRefreshListener();
    }

    @Autowired(required = false)
    @Bean("mqWriteEntityServiceInterceptor")
    @ConditionalOnBean(name = "mqService")
    @ConditionalOnExpression("'${com.quinn-service.cache.main-cache-name:ehCacheAllService}'=='ehCacheAllService'")
    public EntityServiceInterceptor mqWriteEntityServiceInterceptor(
            MqService mqService
    ) {
        return new MqWriteEntityServiceInterceptor(mqService);
    }

}
