package com.quinn.framework.api;

/**
 * 登录核心逻辑处理器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public interface LoginProcessor {

    /**
     * 登录接口
     *
     * @param tokenInfo 令牌信息
     * @return 权限信息
     */
    AuthInfo login(TokenInfo tokenInfo);

    /**
     * 注销接口
     *
     * @return 和登录后的动作相关的一些信息
     */
    Object logout();

}
