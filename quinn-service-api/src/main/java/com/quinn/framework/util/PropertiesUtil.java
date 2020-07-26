package com.quinn.framework.util;

import com.quinn.util.constant.ConfigConstant;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件工具类类
 *
 * @author Qunhua.Liao
 * @since 2020-04-25
 */
public final class PropertiesUtil {

    private PropertiesUtil() {
    }

    /**
     * 加载路径配置信息
     *
     * @param properties    配置信息
     * @param path          路径
     */
    public static void loadPath(Properties properties, String path) {
        try {
            File file = new File(path);
            Resource resource;
            if (file.exists()) {
                resource = new FileSystemResource(file);
            } else {
                resource = new ClassPathResource(path);
            }

            if (!resource.exists()) {
                return;
            }

            if (path.endsWith(ConfigConstant.FILE_SUFFIX_OF_YAML)) {
                YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
                yaml.setDocumentMatchers();
                yaml.setResources(resource);
                properties.putAll(yaml.getObject());
            } else {
                InputStream is = resource.getInputStream();
                properties.load(is);
            }
        } catch (IOException e) {
            // DO NOTHING
        }
    }

}
