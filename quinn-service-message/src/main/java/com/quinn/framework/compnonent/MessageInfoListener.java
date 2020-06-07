package com.quinn.framework.compnonent;

import com.quinn.framework.api.message.MessageAddressResolver;
import com.quinn.framework.api.message.MessageInfoSupplier;
import com.quinn.framework.api.message.MessageSenderSupplier;
import com.quinn.framework.model.MessageInfoFactory;
import com.quinn.framework.model.MessageSenderFactory;
import com.quinn.framework.service.MessageHelpService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

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

        // 消息信息提供者
        MessageInfoSupplier supplier = applicationContext.getBean(MessageInfoSupplier.class);
        MessageInfoFactory.setMessageInfoSupplier(supplier);

        // 消息发送器
        Map<String, MessageSenderSupplier> sendMap = applicationContext.getBeansOfType(MessageSenderSupplier.class);
        for (Map.Entry<String, MessageSenderSupplier> entry : sendMap.entrySet()) {
            MessageSenderFactory.addMessageSenderSupplier(entry.getValue());
        }

        // 设置地址解析器
        Map<String, MessageAddressResolver> resolverMap = applicationContext.getBeansOfType(MessageAddressResolver.class);
        MessageInfoFactory.setAddressResolverMap(resolverMap);

        // 消息数据库信息获取协助者
        MessageHelpService messageHelpService = applicationContext.getBean(MessageHelpService.class);
        MessageSenderFactory.setMessageServerService(messageHelpService);
    }

}
