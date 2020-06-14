package com.quinn.framework.listener;

import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.factory.LoggerExtendFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 主缓存监听
 *
 * @author Qunhua.Liao
 * @since 2020-05-25
 */
@Component
public class CacheServicePostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(CacheServicePostProcessor.class);

    /**
     * 主缓存别名
     */
    String[] MAIN_CACHE_ALIAS =
            new String[]{"cacheAllService", "cacheCommonService", "cacheLockService", "cacheCounterService"};

    /**
     * 会话缓存别名
     */
    String SESSION_CACHE_ALIAS = "sessionCacheAllService";

    /**
     * 主缓存名称
     */
    private String mainCacheAllService;

    /**
     * 会话缓存名称
     */
    private String sessionCacheAllService;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BeanDefinition beanDefinition;
        try {
            beanDefinition = registry.getBeanDefinition(mainCacheAllService);
            for (String alias : MAIN_CACHE_ALIAS) {
                registry.registerBeanDefinition(alias, beanDefinition);
            }
        } catch (Exception e) {
            LOGGER.error("Main cache {0} not exists in BeanDefinitionRegistry," +
                            " we suggest that you should config one using {1}", mainCacheAllService,
                    ConfigConstant.PROP_KEY_OF_MAIN_CACHE_NAME);
            System.exit(-1);
        }

        try {
            beanDefinition = registry.getBeanDefinition(sessionCacheAllService);
            registry.registerBeanDefinition(SESSION_CACHE_ALIAS, beanDefinition);
        } catch (Exception e) {
            LOGGER.error("Main cache {0} not exists in BeanDefinitionRegistry," +
                            " we suggest that you should config one using {1}", sessionCacheAllService,
                    ConfigConstant.PROP_KEY_OF_SESSION_CACHE_NAME);
            System.exit(-1);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        mainCacheAllService = environment
                .getProperty(ConfigConstant.PROP_KEY_OF_MAIN_CACHE_NAME, ConfigConstant.DEFAULT_MAIN_CACHE_NAME);

        sessionCacheAllService = environment
                .getProperty(ConfigConstant.PROP_KEY_OF_SESSION_CACHE_NAME, ConfigConstant.DEFAULT_SESSION_CACHE_NAME);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

}
