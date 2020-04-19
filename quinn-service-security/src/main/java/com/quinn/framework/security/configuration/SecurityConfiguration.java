package com.quinn.framework.security.configuration;

import com.quinn.framework.security.filter.CsrfDefenceFilter;
import com.quinn.framework.security.filter.XssDefenceFilter;
import com.quinn.util.constant.enums.FilterOrderGroupEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 安全防御配置类
 *
 * @author Qunhua.Liao
 * @since 2020-03-30
 */
@Configuration
public class SecurityConfiguration {

    @Value("${com.quinn-service.core.mvc.dynamic-resource.pattern:/api/*}")
    private String dynamicResourcePattern;

    @Bean
    @ConditionalOnExpression("'${com.quinn-service.core.http.defence.csrf:true}'=='true'")
    public FilterRegistrationBean csrfDefenceFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(csrfDefenceFilter());
        registration.addUrlPatterns(dynamicResourcePattern);
        registration.setName("csrfDefenceFilter");
        registration.setOrder(FilterOrderGroupEnum.SECURITY_FILTER.order + 1);
        return registration;
    }

    @Bean
    @ConditionalOnExpression("'${com.quinn-service.core.http.defence.xss:true}'=='true'")
    public FilterRegistrationBean xssDefenceFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(XSSDefenceFilter());
        registration.addUrlPatterns(dynamicResourcePattern);
        registration.setName("xssDefenceFilter");
        registration.setOrder(FilterOrderGroupEnum.SECURITY_FILTER.order + 2);
        return registration;
    }

    @Bean
    public Filter csrfDefenceFilter() {
        return new CsrfDefenceFilter();
    }

    @Bean
    public Filter XSSDefenceFilter() {
        return new XssDefenceFilter();
    }

}
