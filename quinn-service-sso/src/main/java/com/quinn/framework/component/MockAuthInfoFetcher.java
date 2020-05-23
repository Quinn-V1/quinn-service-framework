package com.quinn.framework.component;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.AuthInfoFetcher;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.model.DefaultAuthInfo;
import com.quinn.framework.model.DefaultPermission;
import com.quinn.framework.util.enums.TokenTypeEnum;
import com.quinn.util.base.BaseUtil;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模拟登录业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
@Component("authInfoFetcher_MOCK_USER")
@ConfigurationProperties(prefix = "com.quinn-service.mock-auth")
public class MockAuthInfoFetcher implements AuthInfoFetcher {

    private List<DefaultAuthInfo> authInfos;

    @Override
    public BaseResult<AuthInfo> fetchInfo(TokenInfo tokenInfo) {
        if (authInfos == null) {
            return null;
        }

        for (DefaultAuthInfo authInfo : authInfos) {
            if (BaseUtil.equals(authInfo.getPrincipal(), tokenInfo.getPrincipal())) {
                return BaseResult.success(authInfo);
            }
        }

        return BaseResult.fail();
    }

    @Override
    public String tokenType() {
        return TokenTypeEnum.MOCK_USER.name();
    }

    @Override
    public DefaultPermission fetchPermissions(AuthInfo authInfo) {
        Map<String, DefaultPermission> principals = getStringPermissionMap(authInfo);
        if (principals == null) {
            return null;
        }
        return principals.get(authInfo.getCurrentTenantCode());
    }

    @Override
    public List<StringKeyValue> listTenant(AuthInfo authInfo) {
        Map<String, DefaultPermission> principals = getStringPermissionMap(authInfo);
        if (principals == null) {
            return null;
        }

        List<StringKeyValue> result = new ArrayList<>();
        for (Map.Entry<String, DefaultPermission> entry : principals.entrySet()) {
            StringKeyValue ele = new StringKeyValue();
            ele.setDataKey(entry.getKey());
            ele.setDataCode(entry.getKey());

            ele.setDataValue(entry.getValue().getName());
            result.add(ele);
        }

        return result;
    }

    @Override
    public boolean hasTenant(AuthInfo authInfo, String tenantCode) {
        Map<String, DefaultPermission> principals = getStringPermissionMap(authInfo);
        if (principals == null) {
            return false;
        }
        return principals.containsKey(tenantCode);
    }

    @Override
    public Class<?> getDivClass() {
        return DefaultAuthInfo.class;
    }

    /**
     * 设置用户信息
     * 已办来自配置信息
     *
     * @param authInfos 用户信息
     */
    public void setAuthInfos(List<DefaultAuthInfo> authInfos) {
        this.authInfos = authInfos;
    }

    /**
     * 获取用户所有权限信息
     *
     * @param authInfo 用户信息
     * @return 权限信息（多租户）
     */
    private Map<String, DefaultPermission> getStringPermissionMap(AuthInfo authInfo) {
        if (!(authInfo instanceof DefaultAuthInfo)) {
            return null;
        }

        DefaultAuthInfo defaultAuthInfo = (DefaultAuthInfo) authInfo;
        Map<String, DefaultPermission> principals = defaultAuthInfo.getPrincipals();
        return principals;
    }
}
