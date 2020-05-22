package com.quinn.framework.util;

import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.model.DefaultTokenInfo;
import com.quinn.framework.model.QuinnTokenAdapter;
import com.quinn.util.base.convertor.BaseConverter;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 实体对象转换工具
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
public final class ModelTransferUtil {

    private ModelTransferUtil() {
    }

    /**
     * Shiro 中的令牌转为通用令牌
     *
     * @param token Shiro令牌
     * @return 通用令牌
     */
    public static TokenInfo authenticationTokenToTokenInfo(AuthenticationToken token) {
        if (token instanceof QuinnTokenAdapter) {
            return ((QuinnTokenAdapter) token).getTokenInfo();
        }

        return new DefaultTokenInfo(
                BaseConverter.staticToString(token.getPrincipal()),
                BaseConverter.staticToString(token.getCredentials())
        );
    }

}
