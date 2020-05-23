package com.quinn.framework.model;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.AuthInfoSupplier;

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

    public static final Map<Class, AuthInfoSupplier> AUTH_INFO_SUPPLIER_MAP = new HashMap<>();

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
     * @param object
     * @return
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

}
