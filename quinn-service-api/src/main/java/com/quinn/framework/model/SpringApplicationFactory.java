package com.quinn.framework.model;

import com.quinn.framework.api.SpringApplicationDecorator;
import com.quinn.framework.component.configcollector.BaseConfigInfoCollector;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.util.CollectionUtil;
import com.quinn.util.constant.CharConstant;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;

import java.util.*;

/**
 * 静态应用装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-04-01
 */
public class SpringApplicationFactory {

    /**
     * 整合配置参数
     *
     * @param args 命令行参数
     * @return 整合后的属性集
     */
    public static PriorityProperties collectProperties(String[] args) {
        PriorityProperties priorityProperties = generatePropertyFromArgs(args);
        BaseConfigInfoCollector.collectProperties(priorityProperties);
        System.setProperties(priorityProperties);
        return priorityProperties;
    }

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

    /**
     * 添加系统参数，并标记命令行参数的最高优先级
     *
     * @param args  命令行参数
     * @return      具有优先级标识的配置信息
     */
    private static PriorityProperties generatePropertyFromArgs(String[] args) {
        PriorityProperties priorityProperties = new PriorityProperties();
        priorityProperties.putAll(System.getProperties());

        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        Set<String> optionNames = applicationArguments.getOptionNames();
        if (!CollectionUtil.isEmpty(optionNames)) {
            for (String name : optionNames) {
                priorityProperties.addPriorityKeys(name);
                List<String> optionValues = applicationArguments.getOptionValues(name);
                if (CollectionUtil.isEmpty(optionValues)) {
                    continue;
                }
                if (optionValues.size() == 1) {
                    priorityProperties.put(name, optionValues.get(0));
                } else {
                    priorityProperties.put(name, optionValues.toArray(new String[optionValues.size()]));
                }
            }
        }

        List<String> nonOptionArgs = applicationArguments.getNonOptionArgs();
        if (!CollectionUtil.isEmpty(nonOptionArgs)) {
            for (String arg : nonOptionArgs) {
                if (arg.startsWith(ConfigConstant.NON_OPTION_ARG_PREFIX_SYSTEM)) {

                    int ind = arg.indexOf(CharConstant.CHAR_EQUAL);
                    if (ind > 0) {
                        String key = arg.substring(2, ind);
                        priorityProperties.addPriorityKeys(key);
                        priorityProperties.put(key, arg.substring(ind + 1));
                    }
                }
            }
        }
        return priorityProperties;
    }

}
