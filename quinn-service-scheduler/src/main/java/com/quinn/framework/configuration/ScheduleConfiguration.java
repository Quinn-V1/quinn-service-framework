package com.quinn.framework.configuration;

import com.quinn.framework.api.BusinessJob;
import com.quinn.framework.component.TestBusinessJob;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务配置类
 *
 * @author Qunhua.Liao
 * @since 2020-02-16
 */
@Configuration
public class ScheduleConfiguration {

    @Bean
    public BusinessJob testBusinessJob() {
        return new TestBusinessJob();
    }

}