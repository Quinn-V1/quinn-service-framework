package com.quinn.framework.service.impl;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.LoginPostProcessor;
import com.quinn.framework.api.LoginPrevProcessor;
import com.quinn.framework.api.LoginProcessor;
import com.quinn.framework.model.DefaultTokenInfo;
import com.quinn.framework.service.SsoService;
import com.quinn.util.base.model.BaseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 登录验证抽象类（逻辑框架）
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
@Service
public class DefaultSsoService implements SsoService {

    @Value("${spring.profile.active:prd}")
    private String activeProfile;

    @Resource
    private LoginProcessor loginProcessor;

    private List<LoginPrevProcessor> loginPrevProcessors;

    private List<LoginPostProcessor> loginPostProcessors;

    @Override
    public BaseResult<AuthInfo> login(DefaultTokenInfo token) {
        BaseResult validate = token.validate();
        if (!validate.isSuccess()) {
            return BaseResult.fromPrev(validate);
        }

        // 前置增强（解密、验证码、状态判断）
        if (loginPrevProcessors != null) {
            for (LoginPrevProcessor processor : loginPrevProcessors) {
                BaseResult result = processor.process(token);
                if (!result.isSuccess()) {
                    return BaseResult.fromPrev(result);
                }
            }
        }

        // 登录动作
        AuthInfo authInfo = null;
        RuntimeException exception = null;
        try {
            authInfo = loginProcessor.login(token);
        } catch (RuntimeException e) {
            exception = e;
        } finally {
            // 后置增强（日志、Cookie、其他）
            if (loginPostProcessors != null) {
                for (LoginPostProcessor processor : loginPostProcessors) {
                    BaseResult result = processor.process(authInfo, token, exception);
                    if (!result.isSuccess()) {
                        loginProcessor.logout();
                        return BaseResult.fromPrev(result);
                    }
                }
            }
        }

        if (authInfo == null) {
            return BaseResult.fail();
        }

        return BaseResult.success(authInfo);
    }

    @Override
    public BaseResult logout() {
        Object o = loginProcessor.logout();
        if (o instanceof BaseResult) {
            return (BaseResult) o;
        }
        return BaseResult.success(o);
    }

    @Override
    public void setLoginPrevProcessors(List<LoginPrevProcessor> loginPrevProcessors) {
        this.loginPrevProcessors = loginPrevProcessors;
    }

    @Override
    public void setLoginPostProcessors(List<LoginPostProcessor> loginPostProcessors) {
        this.loginPostProcessors = loginPostProcessors;
    }
}
