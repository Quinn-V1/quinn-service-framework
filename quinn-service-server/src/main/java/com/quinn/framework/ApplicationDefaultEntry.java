package com.quinn.framework;

import com.quinn.framework.api.CustomApplicationListener;
import com.quinn.framework.component.BaseConfigInfoReWriter;
import com.quinn.framework.component.configcollector.BaseConfigInfoCollector;
import com.quinn.framework.model.PriorityProperties;
import com.quinn.framework.model.SpringApplicationFactory;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.util.CollectionUtil;
import com.quinn.util.base.util.StringUtil;
import com.quinn.util.licence.model.ApplicationInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 应用默认入口
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
@SpringBootApplication(
        scanBasePackages = {
                ConfigConstant.PACKAGE_PATH_FRAMEWORK,
                "${" + ConfigConstant.PACKAGE_NAME_APPLICATION + ":}",
                "${" + ConfigConstant.PACKAGE_NAME_MODULES_BEAN + ":}"
        }
)
public class ApplicationDefaultEntry {

    /**
     * 应用入口函数
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        // 收集属性
        Properties properties = collectProperties(args);

        // 重写属性
        BaseConfigInfoReWriter.decryptProperties(properties);

        SpringApplication application =
                SpringApplicationFactory.buildApplication(ApplicationDefaultEntry.class, properties);

        // 启动容器
        ApplicationInfo.getInstance().starting();
        args = convert2LaunchArgs(properties);
        ConfigurableApplicationContext applicationContext = application.run(args);

        // 容器启动后监听
        Map<String, CustomApplicationListener> listeners =
                applicationContext.getBeansOfType(CustomApplicationListener.class);

        ApplicationInfo.getInstance().stated();
        for (Map.Entry<String, CustomApplicationListener> listener : listeners.entrySet()) {
            listener.getValue().applicationStarted(applicationContext, ApplicationInfo.getInstance());
        }
    }

    /**
     * 整合配置参数
     *
     * @param args 命令行参数
     * @return 整合后的属性集
     */
    private static PriorityProperties collectProperties(String[] args) {
        PriorityProperties priorityProperties = new PriorityProperties();

        // 增加命令行参数（优先级最高）
        SimpleCommandLinePropertySource simpleCommandLinePropertySource = new SimpleCommandLinePropertySource(args);
        priorityProperties.addPropertySource(simpleCommandLinePropertySource);
        Set<String> commandLineParamNames = CollectionUtil.asHashSet(simpleCommandLinePropertySource.getPropertyNames());

        // 加入系统参数
        priorityProperties.putAll(System.getProperties());

        // 自定义获取系统参数
        BaseConfigInfoCollector.collectProperties(priorityProperties, commandLineParamNames);

        return priorityProperties;
    }

    /**
     * 将Properties转换为启动参数
     *
     * @param properties 键值参数
     * @return 命令行参数
     */
    public static String[] convert2LaunchArgs(Properties properties) {
        String[] ret = new String[properties.size()];
        Set<Object> keys = properties.keySet();
        int idx = 0;
        for (Object key : keys) {
            String cfgValue = properties.get(key).toString();
            if (StringUtil.isNotEmpty(cfgValue)) {
                String arg = "--" + key.toString() + "=" + cfgValue;
                ret[idx++] = arg;
            } else {
                ret[idx++] = "";
            }
        }
        return ret;
    }

}
