package com.quinn.framework.api;

import org.springframework.boot.SpringApplication;

import java.util.Properties;

/**
 * SpringApplication 装饰器
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
public interface SpringApplicationDecorator {

    /**
     * 对Spring应用做一些装饰
     *
     * @param springApplication Spring应用
     * @param properties 属性
     */
    void decorate(SpringApplication springApplication, Properties properties);

}
