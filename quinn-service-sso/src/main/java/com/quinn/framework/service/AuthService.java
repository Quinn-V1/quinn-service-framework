package com.quinn.framework.service;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.TokenInfo;
import com.quinn.util.base.model.BaseResult;

/**
 * 权限业务接口
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public interface AuthService<T extends TokenInfo, A extends AuthInfo> {

    /**
     * 登录
     *
     * @param token     令牌信息
     * @return 注销结果（权限信息）
     */
    BaseResult<A> login(T token);

    /**
     * 注销
     *
     * @return 注销结果
     */
    BaseResult logout();

}
