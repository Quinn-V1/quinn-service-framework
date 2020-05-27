package com.quinn.framework.listener;

import com.quinn.framework.api.MqListener;
import com.quinn.framework.api.cache.CacheServiceManager;
import com.quinn.util.base.model.BaseResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * EhCache 缓存刷新监听器
 *
 * @author Qunhua.Liao
 * @since 2020-05-27
 */
@Component
public class EhCacheRefreshListener implements MqListener {

    @Resource
    @Qualifier("ehCacheServiceManager")
    private CacheServiceManager ehCacheServiceManager;

    @Override
    public BaseResult handleMessage(Object message) {
        System.out.println(message);
        return BaseResult.SUCCESS;
    }

    @Override
    public String getTargetName() {
        return "eh-cache-refresh-listener";
    }

    @Override
    public int getListenerMode() {
        return ACK_MODE_AUTO;
    }

}
