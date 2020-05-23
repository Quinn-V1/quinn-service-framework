package com.quinn.framework.api;

import com.quinn.framework.model.DefaultPermission;
import com.quinn.util.base.api.ClassDivAble;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.StringKeyValue;

import java.util.List;

/**
 * 权限验证相关信息获取类
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public interface AuthInfoFetcher extends ClassDivAble {

    /**
     * 根据令牌获取授权信息
     *
     * @param t 令牌
     * @return 权限
     */
    BaseResult<AuthInfo> fetchInfo(TokenInfo t);

    /**
     * 获取权限信息
     *
     * @param authInfo 验证信息
     * @return 权限信息
     */
    DefaultPermission fetchPermissions(AuthInfo authInfo);

    /**
     * 获取指定用户的所有租户列表
     *
     * @param authInfo 用户信息
     * @return 租户列表
     */
    List<StringKeyValue> listTenant(AuthInfo authInfo);

    /**
     * 指定用户是否拥有租户权限
     *
     * @param authInfo   用户信息
     * @param tenantCode 租户列表
     * @return 是否有租户列表
     */
    boolean hasTenant(AuthInfo authInfo, String tenantCode);

    /**
     * 作用令牌类型
     *
     * @return 令牌类型
     */
    String tokenType();

}
