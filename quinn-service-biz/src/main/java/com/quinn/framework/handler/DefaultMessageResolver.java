package com.quinn.framework.handler;

import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.api.MessageResolver;
import com.quinn.util.base.api.PlaceholderHandler;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.handler.DefaultPlaceholderHandler;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.MessageProp;
import com.quinn.util.base.util.CollectionUtil;
import com.quinn.util.base.util.I18nUtil;
import com.quinn.util.base.util.StringUtil;
import com.quinn.util.constant.StringConstant;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * 默认消息解析器
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
public class DefaultMessageResolver implements MessageResolver {

    private final static LoggerExtend LOGGER = LoggerExtendFactory.getLogger(DefaultMessageResolver.class);

    /**
     * 路径资源解析器
     */
    private final static PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * 按语言区分的国际化配置（其他语言懒加载）
     */
    private final static Map<Locale, Properties> localeMessagesMap = new HashMap<>();

    /**
     * 默认语言
     */
    private static Locale DEFAULT_LOCALE;

    /**
     * 国际化配置的基础文件
     */
    private static String i18nBaseNames;

    /**
     * 是否初始化完成
     */
    private static boolean initialized;

    /**
     * 默认的属性
     */
    private static Properties defaultProperties;

    /**
     * 初始化
     *
     * @param baseNames
     * @param localeName
     */
    public static synchronized void init(String baseNames, String localeName) {
        DefaultMessageResolver.i18nBaseNames = baseNames;
        if (StringUtils.hasText(localeName)) {
            DEFAULT_LOCALE = new Locale(localeName);
        } else {
            DEFAULT_LOCALE = Locale.getDefault();
        }

        initialized = true;
        try {
            defaultProperties = load(DEFAULT_LOCALE);
        } catch (IOException e) {
            LOGGER.errorError("", e);
        }
    }

    @Override
    public String resolveMessage(String messageCode, Map params, Map i18nParams, String defaultMessage) {

        Properties properties = getProperties();
        if (properties == null) {
            return defaultMessage;
        }

        String result = properties.getProperty(messageCode);
        if (StringUtil.isEmpty(result)) {
            result = defaultMessage;
        }

        if (CollectionUtil.isEmpty(params) && CollectionUtil.isEmpty(i18nParams)) {
            return result;
        }

        Map<String, Object> directParam = new HashMap<>();
        if (params == null) {
            directParam.putAll(params);
        }

        if (!CollectionUtils.isEmpty(i18nParams)) {
            Set<Map.Entry<String, String>> set = i18nParams.entrySet();
            for (Map.Entry<String, String> entry : set) {
                String value = properties.getProperty(entry.getValue());
                value = StringUtil.isEmpty(value) ? entry.getValue() : value;
                directParam.put(entry.getKey(), value);
            }
        }

        return new DefaultPlaceholderHandler(StringConstant.CHAR_OPEN_BRACE, StringConstant.CHAR_CLOSE_BRACE)
                .parseStringWithMap(result, directParam);
    }

    @Override
    public String resolveMessageProp(MessageProp messageProp, String defaultMessage) {
        return resolveMessage(messageProp.getMessageCode(), messageProp.getParams(),
                messageProp.getI18nParams(), defaultMessage);
    }

    @Override
    public String resolveResult(BaseResult result) {
        if (result instanceof BaseResult) {

            return "";
        }
        return resolveMessageProp(result.getMessageProp(), result.getMessage());
    }

    @Override
    public String resolveMessageWithArrayParams(String messageCode, String defaultMessage, Object... args) {
        Properties properties = getProperties();
        if (properties == null) {
            return defaultMessage;
        }

        String result = properties.getProperty(defaultMessage);
        if (CollectionUtil.isEmpty(args)) {
            return result;
        }

        for (int i = 0; i < args.length; i++) {
            args[i] = I18nUtil.tryGetI18n(BaseConverter.staticToString(args[i]), properties);
        }

        return new DefaultPlaceholderHandler(StringConstant.CHAR_OPEN_BRACE, StringConstant.CHAR_CLOSE_BRACE)
                .parseStringWithArray(result, args);
    }

    /**
     * 加载语言版本
     *
     * @param locale 本地化对象
     * @return
     * @throws IOException
     */
    private static Properties load(Locale locale) throws IOException {
        if (!initialized) {
            return new Properties();
        }

        Properties localeMessages = new Properties();
        localeMessagesMap.put(locale, localeMessages);
        String[] resourceNames = i18nBaseNames.split(",");
        for (String resourceName : resourceNames) {
            Resource[] resources = resourcePatternResolver.getResources(resourceName + "*_" + locale + ".properties");
            Properties properties = new Properties();
            for (Resource resource : resources) {
                String resourcePath;
                URL path = resource.getURL();
                if (path == null) {
                    resourcePath = resource.getFilename();
                } else {
                    resourcePath = path.getPath();
                }
                LOGGER.info("Loading ResponseEntity i18n source(ONLY support 'UTF-8' file encoding) => " + resourcePath);

                InputStreamReader reader = null;
                try {
                    reader = new InputStreamReader(resource.getInputStream(), StringConstant.SYSTEM_DEFAULT_CHARSET);
                    properties.load(reader);
                    Set<String> keys = properties.stringPropertyNames();
                    for (String key : keys) {
                        if (localeMessages.containsKey(key)) {
                            LOGGER.warn("duplicated ResponseEntity message key => " + key);
                        }
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

    /**
     * 获取国际化配置
     *
     * @return  国际化配置
     */
    public static Properties getProperties() {
        Locale locale = SessionUtil.getLocale();
        Properties properties = localeMessagesMap.get(locale);
        if (properties == null) {
            try {
                load(locale);
                properties = localeMessagesMap.get(locale);
                properties = properties == null ? defaultProperties : properties;
            } catch (IOException e) {
            }
        }
        return properties;
    }

}
