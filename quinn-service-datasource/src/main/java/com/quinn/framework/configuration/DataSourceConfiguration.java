package com.quinn.framework.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.quinn.framework.api.ErrorHandler;
import com.quinn.framework.handler.DataIntegrityViolationExceptionHandler;
import com.quinn.framework.handler.DuplicateKeyExceptionHandler;
import com.quinn.framework.service.JdbcService;
import com.quinn.framework.service.impl.JdbcServiceImpl;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 *
 * @author Qunhua.Liao
 * @since 2020-04-01
 */
@Configuration
public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean("platformTransactionManager")
    @ConditionalOnExpression("'${com.quinn-service.database.transaction-way:annotation}'=='annotation'")
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean("txAdvice")
    @ConditionalOnExpression("'${com.quinn-service.database.transaction-way:annotation}'=='annotation'")
    public TransactionInterceptor txAdvice(PlatformTransactionManager platformTransactionManager) {
        Map<String, TransactionAttribute> txMap = new HashMap<>();

        // 只读事务
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        txMap.put("get*", readOnlyTx);
        txMap.put("query*", readOnlyTx);

        // 需要事务
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute(
                TransactionDefinition.PROPAGATION_REQUIRED,
                Collections.singletonList(new RollbackRuleAttribute(Exception.class)));

        requiredTx.setTimeout(5);
        txMap.put("add*", requiredTx);
        txMap.put("save*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("delete*", requiredTx);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.setNameMap(txMap);
        return new TransactionInterceptor(platformTransactionManager, source);
    }

    @Bean
    @Primary
    @ConditionalOnExpression("'${com.quinn-service.database.transaction-way:annotation}'=='annotation'")
    public DefaultPointcutAdvisor defaultPointcutAdvisor(TransactionInterceptor txAdvice) {
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
        pointcutAdvisor.setAdvice(txAdvice);
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.quinn..service..*.*(..))");
        pointcutAdvisor.setPointcut(pointcut);
        return pointcutAdvisor;
    }

    @Bean
    public JdbcService jdbcService() {
        return new JdbcServiceImpl();
    }

    @Bean
    public ErrorHandler duplicateKeyExceptionHandler() {
        return new DuplicateKeyExceptionHandler();
    }

    @Bean
    public ErrorHandler dataIntegrityViolationExceptionHandler() {
        return new DataIntegrityViolationExceptionHandler();
    }

}
