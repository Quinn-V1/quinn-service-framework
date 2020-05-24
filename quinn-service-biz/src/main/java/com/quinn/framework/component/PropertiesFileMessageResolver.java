package com.quinn.framework.component;

import com.quinn.util.base.handler.AbstractMessageResolver;
import com.quinn.util.base.handler.PlaceholderHandler;
import com.quinn.util.constant.OrderedConstant;
import com.quinn.util.constant.StringConstant;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

/**
 * 默认消息解析器
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
public class PropertiesFileMessageResolver extends AbstractMessageResolver {

    public PropertiesFileMessageResolver(PlaceholderHandler placeholderHandler, String[] resourceNames) {
        this.placeholderHandler = placeholderHandler;
        this.resourceNames = resourceNames;
    }

    /**
     * 资源路径
     */
    private String[] resourceNames;

    /**
     * 路径资源解析器
     */
    private final static PathMatchingResourcePatternResolver resourcePatternResolver
            = new PathMatchingResourcePatternResolver();

    @Override
    public int priority() {
        return OrderedConstant.HIGHER_V2;
    }

    /**
     * 获取国际化配置
     *
     * @return 国际化配置
     */
    @Override
    public Properties getProperties(Locale locale) {
        Properties properties = localeMessagesMap.get(locale);
        if (properties == null) {
            load(locale);
            properties = localeMessagesMap.get(locale);
            properties = properties == null ? defaultProperties : properties;
        }
        return properties;
    }

    /**
     * 加载语言版本
     *
     * @param locale 本地化对象
     * @return 指定语言配置
     */
    @SneakyThrows
    private Properties load(Locale locale) {
        Properties localeMessages = new Properties();
        localeMessagesMap.put(locale, localeMessages);

        for (String resourceName : resourceNames) {
            Resource[] resources = resourcePatternResolver
                    .getResources(resourceName + "*_" + locale + ".properties");
            Properties properties = new Properties();
            for (Resource resource : resources) {
                InputStreamReader reader = null;
                try {
                    reader = new InputStreamReader(resource.getInputStream(), StringConstant.SYSTEM_DEFAULT_CHARSET);
                    properties.load(reader);
                    Set<String> keys = properties.stringPropertyNames();
                    for (String key : keys) {
                        localeMessages.setProperty(key, properties.getProperty(key));
                    }
                } catch (IOException e) {
                    throw e;
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return localeMessages;
    }

}
