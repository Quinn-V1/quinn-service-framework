package com.quinn.framework.api;

import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.StringConstant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限信息：校验通过后的权限信息（内）
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface AuthInfo<T> extends Serializable {

    /**
     * 验证方式
     */
    String ATTR_NAME_VALIDATE_WAY = "validateWay";

    /**
     * ID
     */
    String ATTR_NAME_ID = "id";

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
     * 获取验证方式
     *
     * @return 验证方式
     */
    default String getValidateWay() {
        return null;
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
     * 附加属性
     *
     * @param name  属性名称
     * @param value 属性值
     * @return 属性
     */
    void attr(String name, Object value);

    /**
     * 获取附加属性
     *
     * @return 附加属性
     */
    default Map<String, Object> getExtraProps() {
        return new HashMap<>(NumberConstant.INT_ZERO);
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
