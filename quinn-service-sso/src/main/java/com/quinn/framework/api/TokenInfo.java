package com.quinn.framework.api;

import com.quinn.util.base.model.BaseResult;

import java.io.Serializable;

/**
 * 令牌信息：登录时传入（外）
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface TokenInfo extends Serializable {

    /**
     * 获取令牌类型（用户名密码、微信、手机、外部系统-单点登录）
     *
     * @return 令牌类型
     */
    String getTokenType();

    /**
     * 内部校验
     *
     * @return 校验结果
     */
    BaseResult validate();

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

    /**
     * 附加属性
     *
     * @param name 属性名称
     * @return 属性
     */
    Object attr(String name);

}
