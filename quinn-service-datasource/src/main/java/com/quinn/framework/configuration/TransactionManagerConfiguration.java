package com.quinn.framework.configuration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 事务配置
 *
 *
 * @author Qunhua.Liao
 * @since 2020-04-01
 */
@Configuration
@ImportResource({"classpath*:config/transaction-aop.xml"})
@ConditionalOnExpression("'${com.quinn-service.database.transaction-way:annotation}'=='xml'")
public class TransactionManagerConfiguration {
}
