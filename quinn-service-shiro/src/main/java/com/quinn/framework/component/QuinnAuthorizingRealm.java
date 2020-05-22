package com.quinn.framework.component;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.model.DefaultAuthInfoAdapter;
import com.quinn.framework.util.ModelTransferUtil;
import com.quinn.framework.util.MultiAuthInfoFetcher;
import com.quinn.util.base.exception.BaseBusinessException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 权限信息解析器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class QuinnAuthorizingRealm extends AuthorizingRealm {

    @Override
    public Class getAuthenticationTokenClass() {
        return AuthenticationToken.class;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        TokenInfo tokenInfo = ModelTransferUtil.authenticationTokenToTokenInfo(authenticationToken);
        AuthInfo authInfo = MultiAuthInfoFetcher.fetch(tokenInfo);
        if (authInfo == null) {
            // FIXME 抛出正确异常
            throw new BaseBusinessException();
        }

        return new SimpleAuthenticationInfo(authInfo, tokenInfo.getCredentials(), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        Object principal = super.getAvailablePrincipal(principals);
        return new DefaultAuthInfoAdapter(principal).getPrincipals();
    }

}
