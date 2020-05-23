package com.quinn.framework.service;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.LoginPostProcessor;
import com.quinn.framework.api.LoginPrevProcessor;
import com.quinn.framework.model.DefaultTokenInfo;
import com.quinn.util.base.model.BaseResult;

import java.util.List;

/**
 * 权限业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface SsoService {

    /**
     * 登录
     *
     * @param token 令牌信息
     * @return 注销结果（权限信息）
     */
    BaseResult<AuthInfo> login(DefaultTokenInfo token);

    /**
     * 注销
     *
     * @return 注销结果
     */
    BaseResult logout();

    /**
     * 我的租户列表
     *
     * @return 我的租户列表
     */
    BaseResult listMyTenant();

    /**
     * 设置我的当前租户
     *
     * @param tenantCode 租户编码
     * @return 设置是否成功
     */
    BaseResult setMyCurrentTenant(String tenantCode);

    /**
     * 添加前置登录增强
     *
     * @param loginPrevProcessors 前置增强器
     */
    default void setLoginPrevProcessors(List<LoginPrevProcessor> loginPrevProcessors) {
    }

    /**
     * 添加后置登录增强
     *
     * @param loginPostProcessors 后置增强器
     */
    default void setLoginPostProcessors(List<LoginPostProcessor> loginPostProcessors) {
    }
}
