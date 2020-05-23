package com.quinn.framework.api;

import com.quinn.util.base.api.ClassDivAble;

/**
 * @author 权限验证供应商
 * @since 2020
 */
public interface AuthInfoSupplier<T> extends ClassDivAble {

    /**
     * 供应授权信息
     *
     * @param object 真实权限信息
     * @return 授权信息
     */
    AuthInfo supply(T object);

}
