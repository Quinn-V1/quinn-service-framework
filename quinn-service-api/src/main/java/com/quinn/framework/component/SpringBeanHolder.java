package com.quinn.framework.component;

import com.quinn.framework.service.BaseEntityService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Spring bean 操作容器
 *
 * @author Qunhua.Liao
 * @since 2020-02-15
 */
@Component
public class SpringBeanHolder implements ApplicationContextAware {

    /**
     * 实体类对应Service
     */
    private static final Map<Class, Object> ENTITY_CLASS_SERVICE_MAP = new ConcurrentHashMap<>();

    /**
     * 实体类对应Service
     */
    private static final Map<Class, Object> ENTITY_CLASS_CONVERTER_MAP = new ConcurrentHashMap<>();

    /**
     * 应用上下文
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanHolder.applicationContext = applicationContext;
    }

    /**
     * 获取Spring容器
     *
     * @return Spring 上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取指定名称Bean
     *
     * @param clazz 实体类
     * @return 对应的Service
     */
    public static <T> T getServiceByEntityClass(Class<?> clazz) {
        return (T) ENTITY_CLASS_SERVICE_MAP.get(clazz);
    }

    /**
     * 注册实体类Service
     *
     * @param object Service
     * @param clazz  实体类
     */
    public static void registerEntityClass(BaseEntityService object, Class<?>... clazz) {
        if (object != null && clazz != null) {
            for (Class cl : clazz) {
                ENTITY_CLASS_SERVICE_MAP.put(cl, object);
            }
        }
    }

    /**
     * 获取指定名称Bean
     *
     * @param name
     * @return
     */
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 获取指定类型Bean
     *
     * @param clazz 类型
     * @param <T>   类型泛型
     * @return 所有Bean
     */
    public static <T> Map<String, T> getBean(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    /**
     * 容器是否含有Bean
     *
     * @param name Bean名称
     * @return 是否含有
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 获取Bean别名
     *
     * @param name Bean名称
     * @return 别名
     */
    public static String[] getAliases(String name) {
        return applicationContext.getAliases(name);
    }

    /**
     * 字动装载Bean
     *
     * @param bean Bean
     */
    public static void autowireBean(Object bean) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(bean);
    }

    /**
     * 注册Bean定义信息
     *
     * @param beanName       Bean名称
     * @param clazz          Bean类型
     * @param argValues      构造器参数
     * @param propertyValues 属性名称
     * @param isSingleton    是否单例
     * @param <T>            Bean类型泛型
     * @return Bean实例
     */
    public static synchronized <T> T registerBeanDefinition(
            String beanName, Class<?> clazz, Object[] argValues, Map<String, Object> propertyValues,
            boolean isSingleton
    ) {
        ConfigurableApplicationContext configurableApplicationContext =
                (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory =
                (DefaultListableBeanFactory) configurableApplicationContext
                        .getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanDefinitionBuilder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
        beanDefinitionBuilder.setScope(
                isSingleton ? ConfigurableBeanFactory.SCOPE_SINGLETON : ConfigurableBeanFactory.SCOPE_PROTOTYPE);

        if (null != propertyValues && 0 != propertyValues.size()) {
            for (Map.Entry<String, Object> entry : propertyValues.entrySet()) {
                beanDefinitionBuilder.addPropertyValue(entry.getKey(), entry.getValue());
            }
        }

        if (null != argValues && 0 != argValues.length) {
            for (Object argValue : argValues) {
                beanDefinitionBuilder.addConstructorArgValue(argValue);
            }
        }

        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        return (T) defaultListableBeanFactory.getBean(beanName);
    }

}
