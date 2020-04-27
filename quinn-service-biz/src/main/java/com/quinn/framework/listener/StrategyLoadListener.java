package com.quinn.framework.listener;

import com.quinn.framework.api.strategy.StrategyExecutor;
import com.quinn.framework.model.strategy.StrategyFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 策略加载监听器
 *
 * @author Qunhua.Liao
 * @since 2020-04-26
 */
@Component
public class StrategyLoadListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, StrategyExecutor> beansOfType = event.getApplicationContext()
                .getBeansOfType(StrategyExecutor.class);
        if (beansOfType != null) {
            StrategyFactory.addStrategies(beansOfType);
        }
    }
}
