package com.quinn.framework.api;

/**
 * 证书认证对比器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public interface CredentialsSubMatcher {

    /**
     * 用户密码对比
     *
     * @param tokenInfo 令牌信息
     * @param authInfo  身份信息
     * @return
     */
    boolean doCredentialsMatch(TokenInfo tokenInfo, AuthInfo authInfo);

}
