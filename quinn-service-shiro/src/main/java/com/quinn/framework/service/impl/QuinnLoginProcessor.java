package com.quinn.framework.service.impl;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.LoginProcessor;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.model.DefaultAuthInfoAdapter;
import com.quinn.framework.model.QuinnTokenAdapter;
import com.quinn.util.base.model.BaseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

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

}
