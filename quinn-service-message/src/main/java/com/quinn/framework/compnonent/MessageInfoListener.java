package com.quinn.framework.compnonent;

import com.quinn.framework.api.message.MessageInfoSupplier;
import com.quinn.framework.model.MessageInfoFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 消息信息加载监听器
 *
 * @author Qunhua.Liao
 * @since 2020-06-02
 */
@Component
public class MessageInfoListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        MessageInfoSupplier supplier = applicationContext.getBean(MessageInfoSupplier.class);
        MessageInfoFactory.setMessageInfoSupplier(supplier);
    }
}
