package com.quinn.framework.configuration;

import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.framework.api.cache.CacheServiceManager;
import com.quinn.framework.component.EhCacheServiceManager;
import lombok.SneakyThrows;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

/**
 * EhCache 配置类
 *
 * @author Qunhua.Liao
 * @since 2020-05-24
 */
@Configuration
public class EhCacheConfiguration {

    @Value("${com.quinn-service.cache.ehcache.config-url:classpath:config/ehcache.xml}")
    private String configUrl;

    @Value("${com.quinn-service.cache.ehcache.keys-namespace:}")
    private String keysNamespace;

    @SneakyThrows
    @Bean("ehCacheManager")
    public CacheManager ehCacheManager() {
        CacheManager cacheManager = CacheManager.create(ResourceUtils.getFile(configUrl).toURI().toURL());
        return cacheManager;
    }

    @Bean("ehCacheServiceManager")
    public CacheServiceManager ehCacheServiceManager(CacheManager ehCacheManager) {
        return new EhCacheServiceManager(ehCacheManager);
    }

    @Bean("ehCacheAllService")
    public CacheAllService ehCacheAllService(CacheServiceManager ehCacheServiceManager) {
        return ehCacheServiceManager.getCacheService("ehCacheAllService", keysNamespace);
    }

}
