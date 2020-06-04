package com.quinn.framework.service.impl;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.LoginPostProcessor;
import com.quinn.framework.api.LoginPrevProcessor;
import com.quinn.framework.api.LoginProcessor;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.exception.AuthInfoNotFoundException;
import com.quinn.framework.model.AuthInfoFactory;
import com.quinn.framework.model.DefaultTokenInfo;
import com.quinn.framework.service.SsoService;
import com.quinn.framework.util.EntityUtil;
import com.quinn.framework.util.MultiAuthInfoFetcher;
import com.quinn.framework.util.MultiCredentialsMatcher;
import com.quinn.framework.util.SessionUtil;
import com.quinn.framework.util.enums.AuthMessageEnum;
import com.quinn.framework.util.enums.CmTypeEnum;
import com.quinn.framework.util.enums.CommonDataTypeEnum;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.enums.FunctionTypeEnum;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;
import com.quinn.util.base.model.StringKeyValueMsgKeyResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 登录验证抽象类（逻辑框架）
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
@Service("ssoService")
public class DefaultSsoService implements SsoService {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(DefaultSsoService.class);

    public DefaultSsoService(
            @Autowired(required = false) LoginProcessor loginProcessor
    ) {
        this.loginProcessor = loginProcessor;
    }

    /**
     * 登录逻辑执行器
     */
    private LoginProcessor loginProcessor;

    /**
     * 登录前置处理
     */
    private List<LoginPrevProcessor> loginPrevProcessors;

    /**
     * 登录后置处理
     */
    private List<LoginPostProcessor> loginPostProcessors;

    @Override
    public BaseResult<List<StringKeyValue>> selectAuthTypes() {
        Collection<String> authTypes = MultiAuthInfoFetcher.authTypes();
        StringKeyValueMsgKeyResolver keyResolver =
                new StringKeyValueMsgKeyResolver(CommonDataTypeEnum.AUTH_TYPE.code)
                        .ofCacheDelimiter(BaseDTO.CACHE_KEY_DELIMITER).ofPropDelimiter(BaseDTO.PROPERTY_DELIMITER);
        return EntityUtil.stringToKeyValue(authTypes, keyResolver);
    }

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
            if (authInfo == null) {
                throw new AuthInfoNotFoundException();
            }
        } catch (RuntimeException e) {
            LOGGER.errorError("login error", e);
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

        if (exception != null) {
            throw exception;
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
    public BaseResult selectMyTenant() {
        List<StringKeyValue> result = loginProcessor.listMyTenant();
        if (CollectionUtil.isEmpty(result)) {
            return BaseResult.fail()
                    .buildMessage(AuthMessageEnum.NO_TENANT.name(), 0, 0)
                    .result();
        }

        return BaseResult.success(result);
    }

    @Override
    public BaseResult setMyCurrentTenant(String tenantCode) {
        loginProcessor.setMyCurrentTenant(tenantCode);
        return BaseResult.SUCCESS;
    }

    @Override
    public BaseResult<List<StringKeyValue>> credentialsMatchers() {
        Collection<String> matcherTypes = MultiCredentialsMatcher.matcherTypes();
        StringKeyValueMsgKeyResolver keyResolver =
                new StringKeyValueMsgKeyResolver(CmTypeEnum.class.getSimpleName())
                        .ofCacheDelimiter(BaseDTO.CACHE_KEY_DELIMITER).ofPropDelimiter(BaseDTO.PROPERTY_DELIMITER);
        return EntityUtil.stringToKeyValue(matcherTypes, keyResolver);
    }

    @Override
    public void setLoginPrevProcessors(List<LoginPrevProcessor> loginPrevProcessors) {
        this.loginPrevProcessors = loginPrevProcessors;
    }

    @Override
    public void setLoginPostProcessors(List<LoginPostProcessor> loginPostProcessors) {
        this.loginPostProcessors = loginPostProcessors;
    }

    @Override
    public BaseResult<List> selectMyPermissions(String group, FunctionTypeEnum type, Long parentId) {
        return MultiAuthInfoFetcher.fetchPermissions(
                AuthInfoFactory.generate(SessionUtil.getAuthInfo()), group, type, parentId);
    }
}
