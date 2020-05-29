package com.quinn.framework.configuration;

import com.quinn.framework.api.AuthInfoFetcher;
import com.quinn.framework.component.MockAuthInfoFetcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 权限配置
 *
 * @author Qunhua.Liao
 * @since 2020-05-29
 */
@Configuration
public class SsoAuthConfiguration {

    @Bean("authInfoFetcher_MOCK_USER")
    @ConfigurationProperties(prefix = "com.quinn-service.mock-auth")
    @ConditionalOnExpression("'${com.quinn-service.mock-auth.disabled:true}'=='false'")
    public AuthInfoFetcher mockAuthInfoFetcher() {
        return new MockAuthInfoFetcher();
    }

}
