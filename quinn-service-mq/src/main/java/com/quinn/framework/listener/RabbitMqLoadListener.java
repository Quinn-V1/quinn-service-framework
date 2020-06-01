package com.quinn.framework.listener;

import com.quinn.framework.api.MqListener;
import com.quinn.framework.api.MqService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;

/**
 * 证书比较器加载监听
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
@Component
public class RabbitMqLoadListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${com.quinn-service.mq.rabbitmq.auto-listen:true}")
    private boolean autoListen;

    @Resource
    private MqService rabbitMQService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (autoListen) {
            ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
            Collection<MqListener> rabbitListeners = applicationContext.getBeansOfType(MqListener.class).values();
            Iterator<MqListener> it = rabbitListeners.iterator();
            while (it.hasNext()) {
                rabbitMQService.listen(it.next());
            }
        }
    }

}
