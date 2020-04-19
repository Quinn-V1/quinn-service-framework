package com.quinn.framework.model;

import com.quinn.framework.api.SpringApplicationDecorator;
import org.springframework.boot.SpringApplication;

import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * 静态应用装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-04-01
 */
public class SpringApplicationFactory {

    /**
     * 修饰应用
     *
     * @param applicationEntry 应用对象
     */
    public static SpringApplication buildApplication(Class applicationEntry, Properties properties) {
        // 采用工厂模式生成不同的应用
        SpringApplication application = new SpringApplication(applicationEntry);

        // 修饰应用
        ServiceLoader<SpringApplicationDecorator> decorators = ServiceLoader.load(SpringApplicationDecorator.class);
        Iterator<SpringApplicationDecorator> decoratorIterator = decorators.iterator();
        while (decoratorIterator.hasNext()) {
            decoratorIterator.next().decorate(application, properties);
        }

        return application;
    }

}
