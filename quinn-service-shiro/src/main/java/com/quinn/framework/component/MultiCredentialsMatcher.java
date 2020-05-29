package com.quinn.framework.component;

import com.quinn.framework.api.CredentialsSubMatcher;
import com.quinn.framework.model.AuthInfoFactory;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 证书认证工具
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public class MultiCredentialsMatcher implements CredentialsMatcher {

    /**
     * 所有的认证机制实现
     */
    private static final Map<Class, CredentialsSubMatcher> AUTH_SUB_SERVICE_MAP = new LinkedHashMap<>();

    @Override
    public boolean doCredentialsMatch(AuthenticationToken tokenInfo, AuthenticationInfo authInfo) {
        Object principal = authInfo.getPrincipals().getPrimaryPrincipal();
        CredentialsSubMatcher credentialsMatcher = credentialsMatcher(principal.getClass());
        if (credentialsMatcher == null) {
            return true;
        }

        return credentialsMatcher.doCredentialsMatch(AuthInfoFactory.generateTokenInfo(tokenInfo),
                AuthInfoFactory.generate(principal));
    }

    /**
     * 根据令牌类型获取认证机制
     *
     * @param authClass 令牌类型
     */
    private static CredentialsSubMatcher credentialsMatcher(Class authClass) {
        CredentialsSubMatcher credentialsSubMatcher = AUTH_SUB_SERVICE_MAP.get(authClass);
        if (credentialsSubMatcher != null) {
            return credentialsSubMatcher;
        }

        for (Map.Entry<Class, CredentialsSubMatcher> entity : AUTH_SUB_SERVICE_MAP.entrySet()) {
            if (authClass.isAssignableFrom(entity.getKey())) {
                return entity.getValue();
            }
        }

        return null;
    }

    /**
     * 添加认证工具
     *
     * @param clazz      适用类
     * @param subMatcher 认证工具
     */
    public static void addCredentialMatcher(Class<?> clazz, CredentialsSubMatcher subMatcher) {
        AUTH_SUB_SERVICE_MAP.put(clazz, subMatcher);
    }

}

