package com.quinn.framework.configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 德鲁伊监控配置
 *
 * @author Qunhua.Liao
 * @since 2020-02-22
 */
@Configuration
public class DruidConfiguration {

    @Value("${com.quinn-service.database.druid.user:admin}")
    private String druidUser;

    @Value("${com.quinn-service.database.druid.user:admin@quinn.com}")
    private String druidPassword;

    @Value("${com.quinn-service.database.druid.reset-enable:false}")
    private String druidResetEnable;

    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        servletRegistrationBean.addInitParameter("loginUsername", druidUser);
        servletRegistrationBean.addInitParameter("loginPassword", druidPassword);
        servletRegistrationBean.addInitParameter("resetEnable", druidResetEnable);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}