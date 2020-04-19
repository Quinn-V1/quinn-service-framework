package com.quinn.framework.configuration;

import com.quinn.framework.api.message.MessageSenderFactory;
import com.quinn.framework.component.EmailSenderFactory;
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
    public MessageSenderFactory messageSenderFactoryEmail() {
        return new EmailSenderFactory();
    }

}
