package com.quinn.framework.component;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 数据源监听事件
 *
 * @author Qunhua.Liao
 * @since 2020-04-01
 */
public class DataSourceApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    }

}
