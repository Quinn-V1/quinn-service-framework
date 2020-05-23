package com.quinn.framework.api;

import com.quinn.util.base.model.StringKeyValue;

import java.util.List;

/**
 * 登录核心逻辑处理器
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
public interface LoginProcessor {

    /**
     * 登录接口
     *
     * @param tokenInfo 令牌信息
     * @return 权限信息
     */
    AuthInfo login(TokenInfo tokenInfo);

    /**
     * 注销接口
     *
     * @return 和登录后的动作相关的一些信息
     */
    Object logout();

    /**
     * 我的租户列表
     *
     * @return 租户列表
     */
    List<StringKeyValue> listMyTenant();

    /**
     * 设置我的当前租户
     *
     * @param tenantCode 租户编码
     */
    void setMyCurrentTenant(String tenantCode);

}
