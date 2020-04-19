package com.quinn.framework.configuration;

import com.quinn.framework.filter.SwaggerDocumentationDisabledFilter;
import com.quinn.util.constant.enums.FilterOrderGroupEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-ui文档禁用配置
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
@Configuration
@ConditionalOnExpression("'${com.quinn-service.core.http.restful.doc:true}'=='false'")
public class SwaggerDisabledConfiguration {

    private static final String SWAGGER_DOCUMENTATION_URI = "/swagger-ui.html";

    @Bean("swaggerDocumentationDisabledFilter")
    public SwaggerDocumentationDisabledFilter swaggerDocumentationDisabledFilter() {
        return new SwaggerDocumentationDisabledFilter();
    }

    @Bean
    public FilterRegistrationBean swaggerDisabledRegistration(
            @Qualifier("swaggerDocumentationDisabledFilter")
                    SwaggerDocumentationDisabledFilter swaggerDocumentationDisabledFilter
    ) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(swaggerDocumentationDisabledFilter);
        registration.addUrlPatterns(SWAGGER_DOCUMENTATION_URI);
        registration.setName("swaggerDocumentationDisabledFilter");
        registration.setOrder(FilterOrderGroupEnum.TOP_FILTER.order + 1);
        return registration;
    }

}
