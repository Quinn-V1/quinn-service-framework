package com.quinn.framework.model;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.AuthInfoSupplier;
import com.quinn.util.constant.ConfigConstant;

/**
 * 数据库权限信息供应商
 *
 * @author Qunhua.Liao
 * @since 2020-05-23
 */
public class DefaultAuthInfoSupplier implements AuthInfoSupplier<DefaultAuthInfo> {

    /**
     * 密码认证方式名称
     */
    private String credentialsMatcher;

    {
        credentialsMatcher = System.getProperty(ConfigConstant.PROP_KEY_OF_MOCK_CREDENTIALS_MATCHER,
                ConfigConstant.DEFAULT_MAIN_CREDENTIALS_MATCHER);
    }

    @Override
    public Class<?> getDivClass() {
        return DefaultAuthInfo.class;
    }

    @Override
    public AuthInfo supply(DefaultAuthInfo object) {
        return object;
    }

    @Override
    public String credentialsMatcherName() {
        return credentialsMatcher;
    }

}
