package com.quinn.framework.configuration;

import com.quinn.framework.api.message.MessageSenderSupplier;
import com.quinn.framework.component.EmailSenderSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 邮件配置类
 *
 * @author Qunhua.Liao
 * @since 2020-04-07
 */
@Configuration
public class EmailConfiguration {

    @Bean("messageSenderFactoryEMAIL")
    public MessageSenderSupplier messageSenderFactoryEmail() {
        return new EmailSenderSupplier();
    }

}
