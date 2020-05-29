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
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Shiro登录验证实现
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
@Service
public class QuinnLoginProcessor implements LoginProcessor {

    @Value("${com.quinn-service.session.authentication-cache-name:authenticationCache}")
    private String authenticationCache;

    @Value("${com.quinn-service.session.authorization-cache-name:authorizationCache}")
    private String authorizationCache;

    @Resource
    private CacheManager cacheManager;

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
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal == null) {
            return BaseResult.SUCCESS;
        }

        subject.logout();
        AuthInfo authInfo = AuthInfoFactory.generate(principal);
        cacheManager.getCache(authenticationCache).remove(authInfo.getPrincipal());
        cacheManager.getCache(authorizationCache).remove(authInfo.getPrincipal());

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
