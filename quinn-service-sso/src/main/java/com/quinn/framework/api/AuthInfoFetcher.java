package com.quinn.framework.api;

/**
 * 权限验证相关信息获取类
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public interface AuthInfoFetcher<T extends TokenInfo, A extends AuthInfo> {

    /**
     * 根据令牌获取授权信息
     *
     * @param t 令牌
     * @return 权限
     */
    A fetch(T t);

}
