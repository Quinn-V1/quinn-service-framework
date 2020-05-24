package com.quinn.framework.listener;

import com.quinn.util.base.api.LoggerGenerator;
import com.quinn.util.base.factory.LoggerExtendFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 国际化解析器监听器
 *
 * @author Qunhua.Liao
 * @since 2020-04-26
 */
@Component
public class LoggerGeneratorListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private LoggerGenerator loggerGenerator;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (loggerGenerator != null) {
            LoggerExtendFactory.setLoggerGenerator(loggerGenerator);
        }
    }
}
