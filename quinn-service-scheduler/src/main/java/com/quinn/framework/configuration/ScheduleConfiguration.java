package com.quinn.framework.configuration;

import com.quinn.framework.api.BusinessJob;
import com.quinn.framework.component.JobLoadOnStartedListener;
import com.quinn.framework.component.TestBusinessJob;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationListener;
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

    @Bean("jobLoadOnStartedListener")
    @ConditionalOnBean(name = {"jobHelpService", "jobExecuteService"})
    public ApplicationListener jobLoadOnStartedListener() {
        return new JobLoadOnStartedListener();
    }

}