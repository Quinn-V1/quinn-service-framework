package com.quinn.framework.api;

/**
 * 权限对象适配器
 *
 * @author Qunhua.Liao
 * @since 2020-05-29
 */
public interface AuthInfoAdapter {

    /**
     * 获取实际权限对象
     *
     * @return 实际权限对象
     */
    AuthInfo getAuthInfo();

}
