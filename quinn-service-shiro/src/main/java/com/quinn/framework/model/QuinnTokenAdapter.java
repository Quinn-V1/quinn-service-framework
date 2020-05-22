package com.quinn.framework.model;

import com.quinn.framework.api.TokenInfo;
import org.apache.shiro.authc.AuthenticationToken;

import java.io.Serializable;

/**
 * Shiro 令牌适配器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class QuinnTokenAdapter implements AuthenticationToken, Serializable {

    /**
     * 真实令牌
     */
    private TokenInfo tokenInfo;

    public QuinnTokenAdapter(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    @Override
    public Object getPrincipal() {
        return tokenInfo.getPrincipal();
    }

    @Override
    public Object getCredentials() {
        return tokenInfo.getCredentials();
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }
}
