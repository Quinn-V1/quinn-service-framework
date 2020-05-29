package com.quinn.framework.model;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.AuthInfoSupplier;
import com.quinn.framework.api.TokenInfo;
import com.quinn.framework.api.TokenInfoAdapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 权限验证信息转换工厂
 *
 * @author Qunhua.Liao
 * @since 2020-05-23
 */
public class AuthInfoFactory {

    private static final Map<Class, AuthInfoSupplier> AUTH_INFO_SUPPLIER_MAP = new HashMap<>();

    static {
        ServiceLoader<AuthInfoSupplier> authInfoSuppliers = ServiceLoader.load(AuthInfoSupplier.class);
        Iterator<AuthInfoSupplier> authInfoSupplierIterator = authInfoSuppliers.iterator();
        while (authInfoSupplierIterator.hasNext()) {
            AuthInfoSupplier next = authInfoSupplierIterator.next();
            AUTH_INFO_SUPPLIER_MAP.put(next.getDivClass(), next);
        }
    }

    /**
     * 根据对象生成
     *
     * @param object 实际权限对象
     * @return 适配权限对象
     */
    public static AuthInfo generate(Object object) {
        if (object instanceof AuthInfo) {
            return (AuthInfo) object;
        }

        AuthInfoSupplier supplier = AUTH_INFO_SUPPLIER_MAP.get(object.getClass());
        if (supplier == null) {
            return new DefaultAuthInfoAdapter(object);
        }

        return supplier.supply(object);
    }

    /**
     * 根据对象生成
     *
     * @param object 实际令牌对象
     * @return 令牌对象
     */
    public static TokenInfo generateTokenInfo(Object object) {
        if (object instanceof TokenInfo) {
            return (TokenInfo) object;
        }

        if (object instanceof TokenInfoAdapter) {
            return ((TokenInfoAdapter) object).getTokenInfo();
        }

        return new DefaultTokenInfoAdapter(object);
    }

    /**
     * 获取全部权限信息提供器
     *
     * @return 权限信息提供器
     */
    public static Map<Class, AuthInfoSupplier> getAuthInfoSupplierMap() {
        return AUTH_INFO_SUPPLIER_MAP;
    }

}
