package com.quinn.framework.component;

import com.quinn.framework.api.SpringApplicationDecorator;
import org.springframework.boot.SpringApplication;

import java.util.Properties;

/**
 * 因为数据源扫描被接管：要去除框架自带的
 *
 * @author Qunhua.Liao
 * @since 2020-04-01
 */
public class CustomDataSourceApplicationDecorator implements SpringApplicationDecorator {

    @Override
    public void decorate(SpringApplication springApplication, Properties properties) {
    }

}
