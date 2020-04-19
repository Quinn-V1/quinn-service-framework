package com.quinn.framework.component.configcollector;

import com.quinn.framework.api.ConfigInfoCollector;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.util.StringUtil;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * 外部配置文件查找
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
public class OutPathConfigInfoCollector extends BaseConfigInfoCollector implements ConfigInfoCollector {

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public void collect(Properties properties, Set<String> ignoreKeys) {
        String path = properties.getProperty(ConfigConstant.CONFIG_KEY_OUT_CONFIGURATION_FILE_PATH);
        if (StringUtil.isEmpty(path)) {
            return;
        }

        try {
            ClassPathResource classPathResource = new ClassPathResource(path);
            if (classPathResource == null) {
                return;
            }

            InputStream is = classPathResource.getInputStream();
            properties.load(is);
        } catch (IOException e) {
        }
    }
}
