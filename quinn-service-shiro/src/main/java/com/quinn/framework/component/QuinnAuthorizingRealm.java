package com.quinn.framework.component;

import com.quinn.framework.model.DefaultAuthInfoAdapter;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
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
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        return null;
    }

    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        Object principal = super.getAvailablePrincipal(principals);
        return new DefaultAuthInfoAdapter(principal).getPrincipals();
    }

}
