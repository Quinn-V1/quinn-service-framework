package com.quinn.framework.util;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.AuthInfoFetcher;
import com.quinn.framework.api.TokenInfo;
import com.quinn.util.base.exception.BaseBusinessException;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限信息获取工具
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
public final class MultiAuthInfoFetcher {

    private MultiAuthInfoFetcher() {
    }

    private static final Map<String, AuthInfoFetcher> AUTH_INFO_FETCHER_MAP = new HashMap<>();

    /**
     * 添加授权信息获取器
     *
     * @param authInfoFetcher 授权信息获取器
     */
    public static void addAuthInfoFetcher(AuthInfoFetcher authInfoFetcher) {
        AUTH_INFO_FETCHER_MAP.put(authInfoFetcher.tokenType(), authInfoFetcher);
    }

    /**
     * 综合获取授权信息
     *
     * @param tokenInfo 令牌信息
     * @return 授权信息
     */
    public static AuthInfo fetch(TokenInfo tokenInfo) {
        AuthInfoFetcher authInfoFetcher = AUTH_INFO_FETCHER_MAP.get(tokenInfo.getTokenType());
        if (authInfoFetcher == null) {
            // FIXME 抛出实际异常
            throw new BaseBusinessException();
        }
        return authInfoFetcher.fetch(tokenInfo);
    }

}
