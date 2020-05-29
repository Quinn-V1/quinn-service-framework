package com.quinn.framework.component;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.AuthInfoFetcher;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.model.DefaultAuthInfo;
import com.quinn.framework.model.DefaultPermission;
import com.quinn.framework.util.enums.AuthMessageEnum;
import com.quinn.framework.util.enums.TokenTypeEnum;
import com.quinn.util.base.BaseUtil;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.enums.CommonMessageEnum;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;
import com.quinn.util.constant.enums.MessageLevelEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模拟登录业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public class MockAuthInfoFetcher implements AuthInfoFetcher {

    private List<DefaultAuthInfo> authInfos;

    @Override
    public BaseResult<AuthInfo> fetchInfo(TokenInfo tokenInfo) {
        if (authInfos == null) {
            return null;
        }

        for (DefaultAuthInfo authInfo : authInfos) {
            if (BaseUtil.equals(authInfo.getPrincipal(), tokenInfo.getPrincipal())) {
                Map<String, DefaultPermission> principals = authInfo.getPrincipals();
                BaseResult result = BaseResult.success(authInfo);

                String tenantCode = tokenInfo.getTenantCode();
                if (!StringUtil.isEmptyInFrame(tenantCode)) {
                    if (CollectionUtil.isEmpty(principals)) {
                        result.ofLevel(MessageLevelEnum.WARN)
                                .buildMessage(AuthMessageEnum.NO_TENANT.name(), 0, 0)
                        ;
                    } else if (principals.containsKey(tenantCode)) {
                        authInfo.setCurrentTenantCode(tenantCode);
                    } else {
                        result.ofLevel(MessageLevelEnum.WARN)
                                .buildMessage(AuthMessageEnum.ERROR_TENANT.name(), 1, 0)
                                .addParam(AuthMessageEnum.ERROR_TENANT.paramNames[0], tenantCode)
                        ;
                    }
                } else {
                    if (CollectionUtil.isEmpty(principals)) {
                        result.ofLevel(MessageLevelEnum.WARN)
                                .buildMessage(AuthMessageEnum.NO_TENANT.name(), 0, 0)
                        ;
                    } else if (principals.size() == 1) {
                        authInfo.setCurrentTenantCode(principals.keySet().iterator().next());
                    } else {
                        result.ofLevel(MessageLevelEnum.WARN)
                                .buildMessage(AuthMessageEnum.MULTI_TENANT.name(), 1, 0)
                                .addParam(AuthMessageEnum.MULTI_TENANT.paramNames[0], principals.size())
                        ;
                    }
                }
                return result;
            }
        }

        return BaseResult.fail()
                .buildMessage(CommonMessageEnum.RESULT_NOT_FOUND.name(), 0, 1)
                .addParamI8n(CommonMessageEnum.RESULT_NOT_FOUND.paramNames[0], DefaultAuthInfo.class.getSimpleName())
                .result();
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
