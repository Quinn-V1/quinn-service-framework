package com.quinn.framework.component.configcollector;

import com.quinn.framework.api.ConfigInfoCollector;
import com.quinn.framework.util.PropertiesUtil;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.StringUtil;

import java.io.File;
import java.util.Properties;

/**
 * 外部配置文件查找
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class OutPathConfigInfoCollector extends BaseConfigInfoCollector implements ConfigInfoCollector {

    /**
     * 默认配置文件
     */
    private static final String[] OUT_CONFIG_FILE_SUFFIXES
            = new String[]{".properties", ".yml"};

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public void collect(Properties properties) {
        String path = properties.getProperty(ConfigConstant.CONFIG_KEY_OUT_CONFIGURATION_FILE_PATH);
        if (StringUtil.isEmpty(path)) {
            String applicationName = properties.getProperty("spring.application.name");
            if (StringUtil.isEmpty(applicationName)) {
                return;
            }

            for (String suffix : OUT_CONFIG_FILE_SUFFIXES) {
                path = applicationName + suffix;
                if (new File(path).exists()) {
                    break;
                }
            }
        }

        PropertiesUtil.loadPath(properties, path);
    }
}
