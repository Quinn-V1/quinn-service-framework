package com.quinn.framework.component;

import com.quinn.framework.api.ConfigInfoReWriter;
import com.quinn.framework.model.PriorityProperties;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.util.StringUtil;

import java.util.*;

/**
 * 默认配置信息重写器
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public abstract class BaseConfigInfoReWriter implements ConfigInfoReWriter {

    /**
     * 影响到的键
     */
    private Set<String> keysEffected = new HashSet<>();

    @Override
    public Set<String> effectedKeys() {
        return keysEffected;
    }

    @Override
    public void encrypt(Properties properties) {
        for (String key : effectedKeys()) {
            properties.setProperty(key, encryptValue(properties.getProperty(key)));
        }
    }

    @Override
    public void decrypt(Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = BaseConverter.staticToString(entry.getKey());
            String value = BaseConverter.staticToString(entry.getValue());

            if (keyMatches(key) || valueMatches(value)) {
                keysEffected.add(key);
                properties.put(key, decryptValue(value));
            }
        }
    }

    /**
     * 重写属性
     *
     * @param properties 属性
     * @return 属性
     */
    public static Properties decryptProperties(Properties properties) {
        List<ConfigInfoReWriter> configInfoReWriters = new ArrayList<>();
        ServiceLoader<ConfigInfoReWriter> configInfoCollectors = ServiceLoader.load(ConfigInfoReWriter.class);
        Iterator<ConfigInfoReWriter> configInfoCollectorIterator = configInfoCollectors.iterator();
        while (configInfoCollectorIterator.hasNext()) {
            configInfoReWriters.add(configInfoCollectorIterator.next());
        }

        Collections.sort(configInfoReWriters, Comparator.comparingInt(ConfigInfoReWriter::getPriority));
        for (ConfigInfoReWriter writer : configInfoReWriters) {
            writer.decrypt(properties);
        }
        return properties;
    }

}
