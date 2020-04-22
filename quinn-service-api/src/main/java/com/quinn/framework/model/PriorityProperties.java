package com.quinn.framework.model;

import org.springframework.core.env.EnumerablePropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 具有优先级的配置信息
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class PriorityProperties extends Properties {

    /**
     * 往后加载需要忽略的Key
     */
    private Set<String> priorityKeys = new ConcurrentSkipListSet<>();

    /**
     * 优先级：越大优先级越高
     */
    private int priority;

    @Override
    public synchronized Object setProperty(String key, String value) {
        if (priorityKeys.contains(key)) {
            return super.putIfAbsent(key, value);
        }
        return super.setProperty(key, value);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        if (priorityKeys.contains(key)) {
            return super.putIfAbsent(key, value);
        }
        return super.put(key, value);
    }

    @Override
    public synchronized void putAll(Map<?, ?> t) {
        if (t != null) {
            for (Map.Entry entry : t.entrySet()) {
                if (priorityKeys.contains(entry.getKey())) {
                    super.putIfAbsent(entry.getKey(), entry.getValue());
                } else {
                    super.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    @Override
    public synchronized void load(Reader reader) throws IOException {
        Properties properties = new Properties();
        properties.load(reader);
        putAll(properties);
    }

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inStream);
        putAll(properties);
    }

    /**
     * 添加忽略键
     *
     * @param keys 忽略键
     */
    public void addPriorityKeys(String... keys) {
        if (keys != null) {
            for (String key : keys) {
                priorityKeys.add(key);
            }
        }
    }

    /**
     * 添加Spring 配置参数
     *
     * @param propertySource Spring配置参数
     */
    public void addPropertySource(EnumerablePropertySource propertySource) {
        String[] propertyNames = propertySource.getPropertyNames();
        for (String propertyName : propertyNames) {
            put(propertyName, propertySource.getProperty(propertyName));
        }
    }

}
