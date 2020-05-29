package com.quinn.framework.api;

/**
 * 令牌对象适配器
 *
 * @author Qunhua.Liao
 * @since 2020-05-29
 */
public interface TokenInfoAdapter {

    /**
     * 获取实际令牌对象
     *
     * @return 实际令牌对象
     */
    TokenInfo getTokenInfo();

}
