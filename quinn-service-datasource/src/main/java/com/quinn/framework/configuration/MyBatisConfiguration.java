package com.quinn.framework.configuration;

import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.StringConstant;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;

/**
 * <strong>Title : MyBatisConfiguration</strong><br>
 * <strong>Package : com.bjqh.framework.core.configuration</strong><br>
 * <strong>Description : Mybatis配置文件</strong><br>
 * <strong>Create on : 2018-05-03 22:35</strong><br>
 * <p>
 *
 * @author Quinn.Liao<br>
 * @version <strong>v1.0.0</strong><br>
 * <br>
 * <strong>修改历史:</strong><br>
 * 修改人 | 修改日期 | 修改描述<br>
 * -------------------------------------------<br>
 * <br>
 * <br>
 */
@Configuration
@ConditionalOnClass(DataSource.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class MyBatisConfiguration implements EnvironmentAware {

    @javax.annotation.Resource
    private DatabaseIdProvider databaseIdProvider;

    @javax.annotation.Resource
    private Interceptor[] interceptors;

    private String dbType;

    private String mybatisMapperScanBasePackage;

    private String mybatisMapperAnnotation;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] mybatisConfigurationResource = pathMatchingResourcePatternResolver.getResources("classpath*:/META-INF/mybatis-conf.xml");
        if (mybatisConfigurationResource != null && mybatisConfigurationResource.length > 0) {
            factory.setConfigLocation(mybatisConfigurationResource[0]);
        }

        Resource[] rs1 = pathMatchingResourcePatternResolver.getResources("classpath*:/mappers/**/*.xml");
        Resource[] rs2 = pathMatchingResourcePatternResolver.getResources("classpath*:/mappers-" + dbType + "/**/*.xml");
        rs1 = (rs1 == null ? new Resource[0] : rs1);
        rs2 = (rs2 == null ? new Resource[0] : rs2);
        Resource[] allResources = new Resource[rs1.length + rs2.length];

        int pos = 0;
        for (int i = 0; i < rs1.length; i++) {
            allResources[pos++] = rs1[i];
        }
        for (int i = 0; i < rs2.length; i++) {
            allResources[pos++] = rs2[i];
        }

        factory.setMapperLocations(allResources);
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            factory.setPlugins(this.interceptors);
        }
        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }
        return factory.getObject();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.mybatisMapperScanBasePackage = environment.resolvePlaceholders("${com.quinn-service.mybatis.mapper.scan.basePackages:}");
        this.mybatisMapperAnnotation = environment.resolvePlaceholders("${com.quinn-service.mybatis.mapper.scan.annotation:org.apache.ibatis.annotations.Mapper}");
        String modulesMapperScanBasePackage = environment.resolvePlaceholders("${" + ConfigConstant.PACKAGE_NAME_MODULES_MAPPER + ":}");
        String comma = ",";
        if ((!StringUtil.isNotEmpty(this.mybatisMapperScanBasePackage)) || this.mybatisMapperScanBasePackage.endsWith(StringConstant.CHAR_COMMA)) {
            comma = "";
        }
        if (StringUtil.isNotEmpty(modulesMapperScanBasePackage) && !(StringConstant.NULL_OF_STRING.equals(modulesMapperScanBasePackage))) {
            this.mybatisMapperScanBasePackage = this.mybatisMapperScanBasePackage + comma + modulesMapperScanBasePackage;
        }

        this.dbType = environment.resolvePlaceholders("${com.quinn.database.db-type:mysql}");
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() throws ClassNotFoundException {
        if (!StringUtil.isNotEmpty(mybatisMapperScanBasePackage)) {
            mybatisMapperScanBasePackage = "com.quinn.framework";
        }
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(mybatisMapperScanBasePackage);
        if (StringUtil.isNotEmpty(mybatisMapperAnnotation)) {
            mapperScannerConfigurer.setAnnotationClass(Class.forName(mybatisMapperAnnotation).asSubclass(java.lang.annotation.Annotation.class));
        }
        return mapperScannerConfigurer;
    }
}
