package com.quinn.framework.component;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.LoginProcessor;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.exception.UnauthorizedException;
import com.quinn.framework.model.AuthInfoFactory;
import com.quinn.framework.model.DefaultAuthInfoAdapter;
import com.quinn.framework.model.QuinnTokenAdapter;
import com.quinn.framework.util.MultiAuthInfoFetcher;
import com.quinn.framework.util.SessionUtil;
import com.quinn.framework.util.enums.AuthMessageEnum;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Shiro登录验证实现
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
@Service("loginProcessor")
public class QuinnLoginProcessor implements LoginProcessor {

    @Resource
    private AuthorizingRealm realm;

    @Override
    public AuthInfo login(TokenInfo token) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new QuinnTokenAdapter(token));
        Object principal = subject.getPrincipal();

        AuthInfo authInfo = AuthInfoFactory.generate(principal);
        boolean superAdmin = subject.hasRole(SessionUtil.SUPER_ADMIN_ROLE_NAME);
        boolean orgAdmin = subject.hasRole(SessionUtil.ORG_ADMIN_ROLE_NAME);
        authInfo.attr("superAdmin", superAdmin);
        authInfo.attr("orgAdmin", orgAdmin);

        return authInfo;
    }

    @Override
    public Object logout() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal == null) {
            return BaseResult.SUCCESS;
        }

        subject.logout();
        AuthInfo authInfo = AuthInfoFactory.generate(principal);
        realm.getAuthorizationCache().remove(authInfo.authCacheKey());
        realm.getAuthenticationCache().remove(authInfo.getPrincipal());

        return BaseResult.SUCCESS;
    }

    @Override
    public List<StringKeyValue> listMyTenant() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            throw new UnauthorizedException().ofStatusCode(HttpStatus.UNAUTHORIZED.value())
                    .buildParam(AuthMessageEnum.UNAUTHORIZED_ACCESS.key(), 0, 0)
                    .exception();
        }

        Object principal = subject.getPrincipal();
        AuthInfo authInfo = AuthInfoFactory.generate(principal);
        return MultiAuthInfoFetcher.listMyTenant(authInfo);
    }

    @Override
    public void setMyCurrentTenant(String tenantCode) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            throw new UnauthorizedException().ofStatusCode(HttpStatus.UNAUTHORIZED.value())
                    .buildParam(AuthMessageEnum.UNAUTHORIZED_ACCESS.key(), 0, 0)
                    .exception();
        }

        Object principal = subject.getPrincipal();
        AuthInfo authInfo = AuthInfoFactory.generate(principal);
        if (!MultiAuthInfoFetcher.hasTenant(authInfo, tenantCode)) {
            throw new UnauthorizedException().ofStatusCode(HttpStatus.FORBIDDEN.value())
                    .buildParam(AuthMessageEnum.ERROR_TENANT.key(), 1, 0)
                    .addParam(AuthMessageEnum.ERROR_TENANT.paramNames[0], tenantCode)
                    .exception();
        }

        authInfo.setCurrentTenantCode(tenantCode);
        subject.getSession().setAttribute(SessionUtil.SESSION_KEY_CURR_TENANT, tenantCode);
    }

}
