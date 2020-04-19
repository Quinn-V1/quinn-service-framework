package com.quinn.framework.api;

/**
 * 令牌信息：登录时传入
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface TokenInfo {

    /**
     * 获取令牌类型（用户名密码、微信、手机、外部系统-单点登录）
     *
     * @return 令牌类型
     */
    String getTokenType();

    /**
     * 获取令牌明文(用户名)
     *
     * @return 令牌明文（用户名）
     */
    Object getPrincipal();

    /**
     * 获取令牌密文（密码）
     *
     * @return 令牌密文（密码）
     */
    Object getCredentials();

}
