package com.quinn.framework.api;

import com.quinn.util.constant.StringConstant;

import java.io.Serializable;

/**
 * 权限信息：校验通过后的权限信息（内）
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface AuthInfo<T> extends Serializable {

    /**
     * 获取令牌明文(用户名)
     *
     * @return 令牌明文（用户名）
     */
    Object getPrincipal();

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
     * 获取顶层组织
     *
     * @return 顶层组织
     */
    String getCurrentTenantCode();

    /**
     * 设置当前租户
     *
     * @param tenantCode 租户编码
     */
    void setCurrentTenantCode(String tenantCode);

    /**
     * 真实权限对象
     *
     * @return
     */
    T realInfo();

    /**
     * 真实权限对象
     *
     * @param t 真实授权信息
     */
    default void ofRealInfo(T t) {
    }

    /**
     * 附加属性
     *
     * @param name 属性名称
     * @return 属性
     */
    default Object attr(String name) {
        return null;
    }

    /**
     * 缓存键
     *
     * @return 权限缓存键
     */
    default String authCacheKey() {
        return getPrincipal() + StringConstant.CHAR_AT_SING + getCurrentTenantCode();
    }

}
