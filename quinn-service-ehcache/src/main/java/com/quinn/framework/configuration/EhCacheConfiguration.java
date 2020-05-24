package com.quinn.framework.configuration;

import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.framework.service.impl.EhCacheAllServiceImpl;
import lombok.SneakyThrows;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.net.URL;

/**
 * EhCache 配置类
 *
 * @author Qunhua.Liao
 * @since 2020-05-24
 */
public class EhCacheConfiguration {

    @Value("${com.quinn-service.ehcache.config-url:conf/ehcache.xml}")
    private String configUrl;

    @Value("${com.quinn-service.ehcache.cache-name:}")
    private String cacheName;

    @Value("${com.quinn-service.ehcache.keys-namespace:}")
    private String keysNamespace;

    @SneakyThrows
    @Bean("ehCacheManager")
    public CacheManager ehCacheManager() {
        CacheManager cacheManager = CacheManager.create(new URL(configUrl));
        return cacheManager;
    }

    @Bean("ehCacheAllService")
    public CacheAllService ehCacheAllService(CacheManager ehCacheManager) {
        EhCacheAllServiceImpl ehCacheAllService =
                new EhCacheAllServiceImpl(ehCacheManager, keysNamespace + cacheName);
        return ehCacheAllService;
    }

}
