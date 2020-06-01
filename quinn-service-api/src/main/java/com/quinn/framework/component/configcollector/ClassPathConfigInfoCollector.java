package com.quinn.framework.component.configcollector;

import com.quinn.framework.api.ConfigInfoCollector;
import com.quinn.framework.util.PropertiesUtil;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;

import java.util.Properties;

/**
 * ClassPath下默认文件查找
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class ClassPathConfigInfoCollector extends BaseConfigInfoCollector implements ConfigInfoCollector {

    /**
     * 配置文件名
     */
    private static final String[] PROFILE_FILE_NAMES = new String[]{"application"};

    /**
     * 支持文件扩展名
     */
    private static final String[] PROFILE_FILE_SUFFIXES = new String[]{".properties", ".yml"};

    /**
     * 不同环境文件占位
     */
    private static final String ACTIVE_PROFILE_PLACE_HOLDER = "activeProfile";

    /**
     * 默认配置文件
     */
    private static final String[] CLASSPATH_CONFIG_RESOURCE_NAMES;

    /**
     * 环境配置文件
     */
    private static final String[] CLASSPATH_CONFIG_RESOURCE_NAMES_PROFILE;

    static {
        CLASSPATH_CONFIG_RESOURCE_NAMES = new String[PROFILE_FILE_NAMES.length * PROFILE_FILE_SUFFIXES.length];
        CLASSPATH_CONFIG_RESOURCE_NAMES_PROFILE = new String[PROFILE_FILE_NAMES.length * PROFILE_FILE_SUFFIXES.length];
        for (int i = 0; i < PROFILE_FILE_NAMES.length; i++) {
            for (int j = 0; j < PROFILE_FILE_SUFFIXES.length; j++) {
                CLASSPATH_CONFIG_RESOURCE_NAMES[i * PROFILE_FILE_SUFFIXES.length + j]
                        = PROFILE_FILE_NAMES[i] + PROFILE_FILE_SUFFIXES[j];

                CLASSPATH_CONFIG_RESOURCE_NAMES_PROFILE[i * PROFILE_FILE_SUFFIXES.length + j]
                        = PROFILE_FILE_NAMES[i] + StringConstant.CHAR_HYPHEN + ACTIVE_PROFILE_PLACE_HOLDER
                        + PROFILE_FILE_SUFFIXES[j];
            }
        }
    }

    @Override
    public int getPriority() {
        return NumberConstant.INT_HUNDRED;
    }

    @Override
    public void collect(Properties properties) {
        for (String path : CLASSPATH_CONFIG_RESOURCE_NAMES) {
            PropertiesUtil.loadPath(properties, path);
        }

        String activeProfile = properties.getProperty(ConfigConstant.PROP_KEY_OF_ACTIVE_PROFILE,
                ConfigConstant.DEFAULT_ACTIVE_PROFILE);

        for (String path : CLASSPATH_CONFIG_RESOURCE_NAMES_PROFILE) {
            PropertiesUtil.loadPath(properties, path.replace(ACTIVE_PROFILE_PLACE_HOLDER, activeProfile));
        }
    }

}
