package com.quinn.framework.component.configcollector;

import com.quinn.framework.api.ConfigInfoCollector;
import com.quinn.util.base.util.StringUtil;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * ClassPath下默认文件查找
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class ClassPathConfigInfoCollector extends BaseConfigInfoCollector implements ConfigInfoCollector {

    /**
     * 默认配置文件
     */
    private static final String[] CLASSPATH_CONFIG_RESOURCE_NAMES
            = new String[]{"application.properties", "application.yml"};

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public void collect(Properties properties, Set<String> ignoreKeys) {
        for (String path : CLASSPATH_CONFIG_RESOURCE_NAMES) {
            if (StringUtil.isEmpty(path)) {
                continue;
            }

            try {
                ClassPathResource classPathResource = new ClassPathResource(path);
                if (path.endsWith("yml")) {
                    YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
                    yaml.setResources(classPathResource);
                    properties.putAll(yaml.getObject());
                } else {
                    InputStream is = classPathResource.getInputStream();
                    properties.load(is);
                }
            } catch (IOException e) {
                // DO NOTHING
            }
        }
    }
}
