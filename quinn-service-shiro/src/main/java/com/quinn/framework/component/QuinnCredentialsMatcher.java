package com.quinn.framework.component;

import com.quinn.framework.model.AuthInfoFactory;
import com.quinn.framework.util.MultiCredentialsMatcher;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * 证书认证工具
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class QuinnCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken tokenInfo, AuthenticationInfo authInfo) {
        return MultiCredentialsMatcher.doCredentialsMatch(AuthInfoFactory.generateTokenInfo(tokenInfo),
                authInfo.getPrincipals().getPrimaryPrincipal());
    }

}

