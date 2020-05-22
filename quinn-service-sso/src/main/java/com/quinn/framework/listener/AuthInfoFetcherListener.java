package com.quinn.framework.listener;

import com.quinn.framework.api.AuthInfoFetcher;
import com.quinn.framework.util.MultiAuthInfoFetcher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 证书比较器加载监听
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
@Component
public class AuthInfoFetcherListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Map<String, AuthInfoFetcher> authInfoFetcherMap
                = contextRefreshedEvent.getApplicationContext().getBeansOfType(AuthInfoFetcher.class);
        if (authInfoFetcherMap != null) {
            for (AuthInfoFetcher authInfoFetcher : authInfoFetcherMap.values()) {
                MultiAuthInfoFetcher.addAuthInfoFetcher(authInfoFetcher);
            }
        }
    }

}
