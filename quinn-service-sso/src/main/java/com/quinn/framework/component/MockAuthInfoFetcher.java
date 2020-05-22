package com.quinn.framework.component;

import com.quinn.framework.api.AuthInfoFetcher;
import com.quinn.framework.model.DefaultAuthInfo;
import com.quinn.framework.model.DefaultTokenInfo;

/**
 * 模拟登录业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public class MockAuthInfoFetcher implements AuthInfoFetcher<DefaultTokenInfo, DefaultAuthInfo> {

    @Override
    public DefaultAuthInfo fetch(DefaultTokenInfo defaultTokenInfo) {
        return null;
    }

}
