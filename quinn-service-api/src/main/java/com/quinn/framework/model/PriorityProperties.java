package com.quinn.framework.model;

import org.springframework.core.env.EnumerablePropertySource;

import java.util.Properties;

/**
 * 具有优先级的配置信息
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class PriorityProperties extends Properties {

    /**
     * 优先级：越大优先级越高
     */
    private int priority;

    /**
     * 添加Spring 配置参数
     *
     * @param propertySource    Spring配置参数
     */
    public void addPropertySource(EnumerablePropertySource propertySource) {
        String[] propertyNames = propertySource.getPropertyNames();
        for (String propertyName : propertyNames) {
            put(propertyName, propertySource.getProperty(propertyName));
        }
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
