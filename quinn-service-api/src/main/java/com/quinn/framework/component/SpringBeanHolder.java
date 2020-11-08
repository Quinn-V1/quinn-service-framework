package com.quinn.framework.component;

import com.quinn.framework.api.KeyValueService;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.service.BaseEntityService;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.exception.DataStyleNotMatchException;
import com.quinn.util.base.exception.MethodNotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring Bean 容器
 *
 * @author Qunhua.Liao
 * @since 2020-11-08
 */
public class SpringBeanHolder {

    public static void setApplicationContext(ApplicationContext applicationContext) {
        throw new MethodNotFoundException();
    }

    public static KeyValueService getKeyValueService(String dataType) {
        throw new MethodNotFoundException();
    }

    public static BaseDTO getDto(String dataType, String dataKey) {
        throw new MethodNotFoundException();
    }

    private static BaseDTO create(Class clazz, String dataKey) {
        throw new MethodNotFoundException();
    }

    public static <T> T getServiceByEntityClass(Class<?> clazz) {
        throw new MethodNotFoundException();
    }

    public static void registerEntityClass(BaseEntityService object, Class<?>... clazz) {
        throw new MethodNotFoundException();
    }

    public static ApplicationContext getApplicationContext() {
        throw new MethodNotFoundException();
    }

    public static <T> T getBean(String name) {
        throw new MethodNotFoundException();
    }

    public static <T> Map<String, T> getBean(Class<T> clazz) {
        throw new MethodNotFoundException();
    }

    public static boolean containsBean(String name) {
        throw new MethodNotFoundException();
    }

    public static String[] getAliases(String name) {
        throw new MethodNotFoundException();
    }

    public static void autowireBean(Object bean) {
        throw new MethodNotFoundException();
    }

    public static synchronized <T> T registerBeanDefinition(
            String beanName, Class<?> clazz, Object[] argValues, Map<String, Object> propertyValues,
            boolean isSingleton
    ) {
        throw new MethodNotFoundException();
    }

}
