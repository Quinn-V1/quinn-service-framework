package com.quinn.framework.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 切面配置
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
@Configuration()
@ImportResource({"classpath*:conf/quinn-aop.xml"})
public class AopConfiguration {

}
