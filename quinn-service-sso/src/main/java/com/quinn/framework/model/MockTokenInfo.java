package com.quinn.framework.model;

import com.quinn.framework.api.TokenInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * 模拟令牌
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
@Setter
@Getter
public class MockTokenInfo implements TokenInfo {

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * 令牌明文(用户名)用户名
     */
    private String principal;

    /**
     * 密码
     */
    private String credentials;

}
