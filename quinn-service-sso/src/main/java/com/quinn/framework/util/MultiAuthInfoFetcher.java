package com.quinn.framework.util;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.AuthInfoFetcher;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.exception.AuthTypeNotSupportException;
import com.quinn.framework.model.DefaultPermission;
import com.quinn.framework.util.enums.AuthMessageEnum;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;

import java.util.*;

/**
 * 权限信息获取工具
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
public final class MultiAuthInfoFetcher {

    private MultiAuthInfoFetcher() {
    }

    private static final Map<String, AuthInfoFetcher> AUTH_INFO_FETCHER_MAP = new HashMap<>();

    private static final Map<Class, AuthInfoFetcher> AUTH_INFO_FETCHER_MAP_CLASS = new HashMap<>();

    private static final String activeProfile = System.getProperty(ConfigConstant.PROP_KEY_OF_ACTIVE_PROFILE,
            ConfigConstant.DEFAULT_ACTIVE_PROFILE);

    /**
     * 支持的校验方式
     *
     * @return
     */
    public static Collection<String> authTypes() {
        return AUTH_INFO_FETCHER_MAP.keySet();
    }

    /**
     * 添加授权信息获取器
     *
     * @param authInfoFetcher 授权信息获取器
     */
    public static void addAuthInfoFetcher(AuthInfoFetcher authInfoFetcher) {
        AUTH_INFO_FETCHER_MAP.put(authInfoFetcher.tokenType(), authInfoFetcher);
        AUTH_INFO_FETCHER_MAP_CLASS.put(authInfoFetcher.getDivClass(), authInfoFetcher);
    }

    /**
     * 综合获取授权信息
     *
     * @param tokenInfo 令牌信息
     * @return 授权信息
     */
    public static BaseResult<AuthInfo> fetchInfo(TokenInfo tokenInfo) {
        return pickAuthInfoFetcher(tokenInfo.getTokenType()).fetchInfo(tokenInfo);
    }

    /**
     * 获取权限信息
     *
     * @param authInfo 验证信息
     */
    public static DefaultPermission fetchPermissions(AuthInfo authInfo) {
        return pickAuthInfoFetcher(authInfo.getClass()).fetchPermissions(authInfo);
    }

    /**
     * 获取权限信息
     *
     * @param authInfo 验证信息
     */
    public static BaseResult<List> fetchPermissions(AuthInfo authInfo, String group, String type, Long parentId) {
        return pickAuthInfoFetcher(authInfo.getClass()).selectMyPermissions(authInfo, group, type, parentId);
    }

    /**
     * 获取用户所有租户
     *
     * @param authInfo 用户信息
     * @return 所有租户
     */
    public static List<StringKeyValue> listMyTenant(AuthInfo authInfo) {
        return pickAuthInfoFetcher(authInfo.getClass()).listTenant(authInfo);
    }

    /**
     * 用户是否有租户权限
     *
     * @param authInfo   用户信息
     * @param tenantCode 租户编码
     * @return
     */
    public static boolean hasTenant(AuthInfo authInfo, String tenantCode) {
        return pickAuthInfoFetcher(authInfo.getClass()).hasTenant(authInfo, tenantCode);
    }

    /**
     * 挑选授权信息带回工具
     *
     * @param type 令牌类型
     * @return 授权信息获取器
     */
    private static AuthInfoFetcher pickAuthInfoFetcher(String type) {
        AuthInfoFetcher authInfoFetcher = AUTH_INFO_FETCHER_MAP.get(type);
        if (authInfoFetcher == null) {
            throw new AuthTypeNotSupportException()
                    .addParamI8n(AuthMessageEnum.AUTH_TYPE_NOT_SUPPORT.paramNames[0], activeProfile)
                    .addParamI8n(AuthMessageEnum.AUTH_TYPE_NOT_SUPPORT.paramNames[1], type)
                    .exception()
                    ;
        }
        return authInfoFetcher;
    }

    /**
     * 挑选授权信息带回工具
     *
     * @param type 授权信息类型
     * @return 授权信息获取器
     */
    private static AuthInfoFetcher pickAuthInfoFetcher(Class type) {
        AuthInfoFetcher authInfoFetcher = AUTH_INFO_FETCHER_MAP_CLASS.get(type);
        if (authInfoFetcher == null) {
            throw new AuthTypeNotSupportException()
                    .addParamI8n(AuthMessageEnum.AUTH_TYPE_NOT_SUPPORT.paramNames[0], activeProfile)
                    .addParamI8n(AuthMessageEnum.AUTH_TYPE_NOT_SUPPORT.paramNames[1], type.getSimpleName())
                    .exception()
                    ;
        }
        return authInfoFetcher;
    }
}
