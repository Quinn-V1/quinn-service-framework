package com.quinn.framework.api;

/**
 * 权限信息：校验通过后的权限信息（内）
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface AuthInfo {

    /**
     * 授权信息
     *
     * @return 授权信息
     */
    Object getPrincipals();

    /**
     * 证书认证信息
     *
     * @return 证书认证信息
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
