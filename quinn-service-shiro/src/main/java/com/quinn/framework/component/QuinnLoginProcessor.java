package com.quinn.framework.component;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.LoginProcessor;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.model.AuthInfoFactory;
import com.quinn.framework.model.DefaultAuthInfoAdapter;
import com.quinn.framework.model.QuinnTokenAdapter;
import com.quinn.framework.util.MultiAuthInfoFetcher;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Shiro登录验证实现
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
@Service
public class QuinnLoginProcessor implements LoginProcessor {

    @Override
    public AuthInfo login(TokenInfo token) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new QuinnTokenAdapter(token));
        Object principal = subject.getPrincipal();

        if (principal instanceof AuthInfo) {
            return (AuthInfo) principal;
        }

        return new DefaultAuthInfoAdapter(principal);
    }

    @Override
    public Object logout() {
        SecurityUtils.getSubject().logout();
        return BaseResult.SUCCESS;
    }

    @Override
    public List<StringKeyValue> listMyTenant() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            throw new BaseBusinessException();
        }

        Object principal = subject.getPrincipal();
        AuthInfo authInfo = AuthInfoFactory.generate(principal);
        return MultiAuthInfoFetcher.listMyTenant(authInfo);
    }

    @Override
    public void setMyCurrentTenant(String tenantCode) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            throw new BaseBusinessException();
        }

        Object principal = subject.getPrincipal();
        AuthInfo authInfo = AuthInfoFactory.generate(principal);
        if (!MultiAuthInfoFetcher.hasTenant(authInfo, tenantCode)) {
            // FIXME 抛出正确异常
            throw new BaseBusinessException();
        }

        authInfo.setCurrentTenantCode(tenantCode);
        subject.getSession().setAttribute(SessionUtil.SESSION_KEY_CURR_TENANT, tenantCode);
    }

}
