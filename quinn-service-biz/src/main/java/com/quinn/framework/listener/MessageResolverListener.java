package com.quinn.framework.listener;

import com.quinn.util.base.api.MessageResolver;
import com.quinn.util.base.handler.EnumMessageResolver;
import com.quinn.util.base.handler.MultiMessageResolver;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * 国际化解析器监听器
 *
 * @author Qunhua.Liao
 * @since 2020-04-26
 */
@Component
public class MessageResolverListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, MessageResolver> beansOfType = event.getApplicationContext().getBeansOfType(MessageResolver.class);
        if (beansOfType != null) {
            ArrayList arrayList = new ArrayList(beansOfType.values());
            arrayList.add(new EnumMessageResolver());
            MultiMessageResolver.setResolverList(arrayList);
        } else {
            MultiMessageResolver.setResolverList(Arrays.asList(new EnumMessageResolver()));
        }
    }
}
