package com.quinn.framework.activiti.configuration;

import com.quinn.framework.activiti.component.ActivitiInfoFiller;
import com.quinn.framework.activiti.component.ActivityBehaviorFactoryExt;
import com.quinn.framework.activiti.component.ExclusiveGatewayActivityBehaviorExt;
import com.quinn.framework.activiti.component.ServiceTaskDelegate;
import com.quinn.framework.activiti.listener.GlobalBpmListener;
import com.quinn.framework.api.*;
import com.quinn.util.base.api.MethodInvokerOneParam;
import com.quinn.util.base.factory.PrefixThreadFactory;
import com.quinn.util.base.model.BatchResult;
import com.quinn.util.constant.OrderedConstant;
import org.activiti.engine.*;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.bpmn.parser.factory.ActivityBehaviorFactory;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.activiti.engine.impl.history.HistoryLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Bpm配置类-Activiti相关的Bean
 *
 * @author Qunhua.Liao
 * @since 2020-04-30
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnBean(name = {"dataSource"})
@AutoConfigureOrder(OrderedConstant.HIGHER_V1)
public class BpmActivitiConfiguration {

    @Value("${com.ming-cloud.bpm.thread-core-size:5}")
    private int coreTheadSize;

    @Resource
    private DataSource dataSource;

    @Bean
    public ActivityBehavior exclusiveGatewayActivityBehaviorExt() {
        return new ExclusiveGatewayActivityBehaviorExt();
    }

    @Bean
    public ActivityBehaviorFactory activityBehaviorFactoryExt() {
        return new ActivityBehaviorFactoryExt();
    }

    @Bean("bpmInstSupplier")
    @ConditionalOnMissingBean(BpmInstSupplier.class)
    public BpmInstSupplier bpmInstSupplier() {
        return new BpmInstSupplier() {
            @Override
            public BpmInstInfo newBpmInstInfo() {
                return null;
            }

            @Override
            public BpmTaskInfo newBpmTaskInfo() {
                return null;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(ProcessEngineConfiguration.class)
    public ProcessEngineConfiguration processEngineConfiguration(
            ActivityBehaviorFactory activityBehaviorFactoryExt
    ) {
        StandaloneProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        // 自动更新表结构，数据库表不存在时会自动创建表
        configuration.setDatabaseSchemaUpdate("true");
        // 保存历史数据级别设置为full最高级别，便于历史数据的追溯
        configuration.setHistoryLevel(HistoryLevel.FULL);
        // 表示使用历史表，如果不配置，则工程启动后可以检查数据库，只建立了17张表，历史表没有建立
        configuration.setDbHistoryUsed(true);
        // 全局监听事件
        configuration.setEventListeners(Arrays.asList(globalBpmListener()));
        configuration.setAsyncExecutorActivate(false);
        configuration.setActivityBehaviorFactory(activityBehaviorFactoryExt);
        return configuration;
    }

    @Bean
    @ConditionalOnMissingBean(ProcessEngine.class)
    public ProcessEngine processEngine(ProcessEngineConfiguration processEngineConfiguration) {
        return processEngineConfiguration.buildProcessEngine();
    }

    @Bean
    @ConditionalOnMissingBean(RepositoryService.class)
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    @ConditionalOnMissingBean(RuntimeService.class)
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    @ConditionalOnMissingBean(TaskService.class)
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    @ConditionalOnMissingBean(HistoryService.class)
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    @ConditionalOnMissingBean(DynamicBpmnService.class)
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
        return processEngine.getDynamicBpmnService();
    }

    @Bean("globalBpmListener")
    @ConditionalOnMissingBean(name = {"globalBpmListener"})
    public ActivitiEventListener globalBpmListener() {
        return new GlobalBpmListener();
    }

    @Bean("activitiInfoFiller")
    @ConditionalOnMissingBean(BpmInfoFiller.class)
    public BpmInfoFiller activitiInfoFiller() {
        return new ActivitiInfoFiller();
    }

    @Bean("modelSupplier")
    @ConditionalOnMissingBean(BpmModelSupplier.class)
    public BpmModelSupplier modelSupplier() {
        return new BpmModelSupplier() {
            @Override
            public BpmModelInfo newBpmModelInfo() {
                return null;
            }

            @Override
            public BpmNodeInfo newBpmNodeInfo() {
                return null;
            }

            @Override
            public BpmNodeRelateInfo newBpmNodeRelateInfo() {
                return null;
            }
        };
    }

    @Bean("serviceTaskDelegate")
    @ConditionalOnMissingBean(name = "serviceTaskDelegate")
    public JavaDelegate serviceTaskDelegate() {
        return new ServiceTaskDelegate();
    }

    @Bean
    @ConditionalOnMissingBean(name = {"bpmExecutorService"})
    public ScheduledExecutorService bpmExecutorService() {
        return new ScheduledThreadPoolExecutor(coreTheadSize, new PrefixThreadFactory("bpm-pool-"),
                (r, executor) -> {
                    // TODO 超出处理能力保障
                });
    }

    @Bean("serviceTaskDelegateProxy")
    public MethodInvokerOneParam<BpmTaskInfo, BatchResult> serviceTaskDelegateProxy() {
        return (MethodInvokerOneParam<BpmTaskInfo, BatchResult>) bpmTaskInfo -> null;
    }
}
