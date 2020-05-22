package com.quinn.framework.api;

import com.quinn.util.base.api.ClassDivAble;

/**
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public interface CredentialsSubMatcher extends ClassDivAble {

    /**
     * 用户密码对比
     *
     * @param tokenInfo 令牌信息
     * @param authInfo  身份信息
     * @return
     */
    boolean doCredentialsMatch(TokenInfo tokenInfo, AuthInfo authInfo);

}
