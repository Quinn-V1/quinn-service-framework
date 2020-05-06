package com.quinn.framework.listener;

import com.quinn.framework.component.SpringBeanHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Bean持有容器监听
 *
 * @author Qunhua.Liao
 * @since 2020-04-23
 */
@Component
public class SpringHolderListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        SpringBeanHolder.setApplicationContext(event.getApplicationContext());
    }
}
