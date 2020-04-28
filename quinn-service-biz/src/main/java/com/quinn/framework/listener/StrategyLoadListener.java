package com.quinn.framework.listener;

import com.quinn.framework.api.strategy.StrategyBean;
import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.component.strategy.MethodBeanStrategy;
import com.quinn.framework.model.strategy.StrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 策略加载监听器
 *
 * @author Qunhua.Liao
 * @since 2020-04-26
 */
@Component
public class StrategyLoadListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    @Qualifier("strategyExecutorService")
    private ExecutorService strategyExecutorService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        StrategyFactory.setExecutorService(strategyExecutorService);

        Map<String, StrategyExecutor> beansOfType = event.getApplicationContext()
                .getBeansOfType(StrategyExecutor.class);

        if (beansOfType != null) {
            StrategyFactory.addStrategies(beansOfType);
        }

        Map<String, StrategyBean> strategyBeans = event.getApplicationContext()
                .getBeansOfType(StrategyBean.class);

        if (beansOfType != null) {
            MethodBeanStrategy.addStrategyBeanMap(strategyBeans);
        }

    }

}
