package com.quinn.framework.component.configcollector;

import com.quinn.framework.api.ConfigInfoCollector;
import com.quinn.framework.model.PriorityProperties;
import com.quinn.util.base.util.CollectionUtil;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.util.*;

/**
 * 配置信息收集基础类
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
public abstract class BaseConfigInfoCollector implements ConfigInfoCollector {

    /**
     * 整合配置参数
     *
     * @param properties 命令行参数
     * @return 整合后的属性集
     */
    public static void collectProperties(Properties properties) {
        // 获取配置信息收集器
        List<ConfigInfoCollector> configInfoCollectorList = new ArrayList<>();
        ServiceLoader<ConfigInfoCollector> configInfoCollectors = ServiceLoader.load(ConfigInfoCollector.class);
        Iterator<ConfigInfoCollector> configInfoCollectorIterator = configInfoCollectors.iterator();
        while (configInfoCollectorIterator.hasNext()) {
            configInfoCollectorList.add(configInfoCollectorIterator.next());
        }

        // 按优先级增加不同类型参数
        Collections.sort(configInfoCollectorList, Comparator.comparingInt(ConfigInfoCollector::getPriority));
        for (ConfigInfoCollector configInfoCollector : configInfoCollectors) {
            configInfoCollector.collect(properties);
        }
    }

}
