package com.quinn.framework.component;

import com.quinn.framework.api.AuthInfoFetcher;
import com.quinn.framework.model.DefaultAuthInfo;
import com.quinn.framework.model.DefaultTokenInfo;
import com.quinn.framework.util.enums.TokenTypeEnum;
import com.quinn.util.base.BaseUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 模拟登录业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
@Component("authInfoFetcher_MOCK_USER")
@ConfigurationProperties(prefix = "com.quinn-service.mock-auth")
public class MockAuthInfoFetcher implements AuthInfoFetcher<DefaultTokenInfo, DefaultAuthInfo> {

    private List<DefaultAuthInfo> authInfos;

    @Override
    public DefaultAuthInfo fetch(DefaultTokenInfo tokenInfo) {
        if (authInfos == null) {
            return null;
        }

        for (DefaultAuthInfo authInfo : authInfos) {
            if (BaseUtil.equals(authInfo.getPrincipal(), tokenInfo.getPrincipal())) {
                return authInfo;
            }
        }

        return null;
    }

    @Override
    public String tokenType() {
        return TokenTypeEnum.MOCK_USER.name();
    }

    public void setAuthInfos(List<DefaultAuthInfo> authInfos) {
        this.authInfos = authInfos;
    }
}
