package com.quinn.framework.configuration;

import com.quinn.framework.api.JobExecuteService;
import com.quinn.framework.component.QuartzExecuteServiceAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时任务配置类
 *
 * @author Qunhua.Liao
 * @since 2020-02-16
 */
@Configuration
public class QuartzConfiguration {

    @Bean(name = "scheduler")
    @ConditionalOnMissingBean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setAutoStartup(true);
        return factoryBean;
    }

    @Bean("jobExecuteService")
    public JobExecuteService jobExecuteService() {
        return new QuartzExecuteServiceAdapter();
    }

}