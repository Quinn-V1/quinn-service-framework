package com.quinn.framework.listener;

import com.quinn.util.base.api.I18nMsgResolver;
import com.quinn.util.base.handler.MultiI18nMsgResolver;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

/**
 * 国际化解析器监听器
 *
 * @author Qunhua.Liao
 * @since 2020-04-26
 */
@Component
public class I18nResolverListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, I18nMsgResolver> beansOfType = event.getApplicationContext().getBeansOfType(I18nMsgResolver.class);
        if (beansOfType != null) {
            MultiI18nMsgResolver.setResolverList(new ArrayList<>(beansOfType.values()));
        }
    }
}
