package com.quinn.framework.component.configcollector;

import com.quinn.framework.api.ConfigInfoCollector;
import com.quinn.util.base.constant.ConfigConstant;
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

    /**
     * 环境配置文件
     */
    private static final String[] CLASSPATH_CONFIG_RESOURCE_NAMES_PROFILE
            = new String[]{"application-activeProfile.properties",
            "application-activeProfile.yml"};

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public void collect(Properties properties) {
        for (String path : CLASSPATH_CONFIG_RESOURCE_NAMES) {
            loadPath(properties, path);
        }

        String activeProfile = properties.getProperty("spring.profiles.active", "dev");
        for (String path : CLASSPATH_CONFIG_RESOURCE_NAMES_PROFILE) {
            loadPath(properties, path.replace("activeProfile", activeProfile));
        }
    }

    /**
     * 加载路径配置信息
     *
     * @param properties    配置信息
     * @param path          路径
     */
    private void loadPath(Properties properties, String path) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(path);
            if (path.endsWith(ConfigConstant.FILE_SUFFIX_OF_YAML)) {
                YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
                yaml.setDocumentMatchers();
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
