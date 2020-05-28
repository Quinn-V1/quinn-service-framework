package com.quinn.framework.component;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.exception.AuthInfoNotFoundException;
import com.quinn.framework.model.AuthInfoFactory;
import com.quinn.framework.model.DefaultPermission;
import com.quinn.framework.model.QuinnAuthorizationInfoAdapter;
import com.quinn.framework.util.ModelTransferUtil;
import com.quinn.framework.util.MultiAuthInfoFetcher;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.model.BaseResult;
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
        BaseResult<AuthInfo> authInfo = MultiAuthInfoFetcher.fetchInfo(tokenInfo);
        if (!authInfo.isSuccess()) {
            throw new AuthInfoNotFoundException().getMessageProp().ofPrevProp(authInfo.getMessageProp()).exception();
        }
        return new SimpleAuthenticationInfo(authInfo.getData(), tokenInfo.getCredentials(), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal = this.getAvailablePrincipal(principalCollection);
        AuthInfo authInfo = AuthInfoFactory.generate(principal);
        DefaultPermission defaultPermission = MultiAuthInfoFetcher.fetchPermissions(authInfo);
        return defaultPermission == null ? null : new QuinnAuthorizationInfoAdapter(defaultPermission);
    }

    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        Object principal = super.getAvailablePrincipal(principals);
        AuthInfo generate = AuthInfoFactory.generate(principal);
        return generate.authCacheKey();
    }

}
