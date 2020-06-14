package com.quinn.framework.listener;

import com.quinn.framework.api.AbstractMqListener;
import com.quinn.framework.api.cache.CacheServiceManager;
import com.quinn.framework.api.entityflag.CacheAble;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * EhCache 缓存刷新监听器
 * 如果主缓存是 EhCache 则需要这个
 *
 * @author Qunhua.Liao
 * @since 2020-05-27
 */
public class EhCacheRefreshListener extends AbstractMqListener<CacheAble> {

    @Value("${com.quinn-service.cache.ehcache.refresh-mq-name:eh-cache-refresh-listener}")
    private String queueName;

    @Value("${com.quinn-service.cache.ehcache.keys-namespace:}")
    private String keysNamespace;

    @Resource
    @Qualifier("ehCacheServiceManager")
    private CacheServiceManager ehCacheServiceManager;

    @Override
    public void doHandleMessage(CacheAble object) {
        ehCacheServiceManager.refresh("ehCacheAllService", keysNamespace, object.cacheKey(), object);
    }

    @Override
    public void afterPropertiesSet() {
        setTargetName(queueName);
        setListenerMode(ACK_MODE_AUTO);
    }
}
