package com.quinn.framework.api;

/**
 * 权限信息：校验通过后的权限信息
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface AuthInfo {

    /**
     *
     *
     * @return
     */
    Object getPrincipals();

    Object getCredentials();

}
