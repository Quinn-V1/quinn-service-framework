package com.quinn.framework.service.impl;

import com.quinn.framework.exception.AuthTypeNotSupportException;
import com.quinn.framework.model.MockAuthInfo;
import com.quinn.framework.model.MockTokenInfo;
import com.quinn.framework.service.AuthService;
import com.quinn.framework.util.enums.AuthExceptionEnum;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.util.StringUtil;
import com.quinn.util.constant.enums.ProfileEnum;
import org.springframework.beans.factory.annotation.Value;

/**
 * 模拟登录业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public class MockAuthServiceImpl implements AuthService<MockTokenInfo, MockAuthInfo> {

    @Value("${spring.profile.active:prd}")
    private String activeProfile;

    @Override
    public BaseResult<MockAuthInfo> login(MockTokenInfo token) {
        if (StringUtil.isNotEmpty(activeProfile)
                && ProfileEnum.findCodeByName(activeProfile) > ProfileEnum.UAT.level) {
            throw new AuthTypeNotSupportException().getMessageProp()
                    .addParam(AuthExceptionEnum.AUTH_TYPE_NOT_SUPPORT.paramNames[0], activeProfile)
                    .addParam(AuthExceptionEnum.AUTH_TYPE_NOT_SUPPORT.paramNames[1], token.getTokenType())
                    .exception();
        }

        return null;
    }

    @Override
    public BaseResult logout() {
        return null;
    }

}
