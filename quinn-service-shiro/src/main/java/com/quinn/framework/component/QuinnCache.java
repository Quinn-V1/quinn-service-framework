package com.quinn.framework.component;

import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.DataConverter;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.enums.CommonMessageEnum;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.base.exception.KeyInfoMissException;
import com.quinn.util.base.exception.MethodNotFoundException;
import com.quinn.util.base.exception.ParameterShouldNotEmpty;
import com.quinn.util.constant.NumberConstant;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Shiro 缓存简易实现（redis）
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
public class QuinnCache<K, V> implements Cache<K, V> {

    private CacheAllService cacheAllService;

    private String keyPrefix = "";

    private int expire = 0;

    private String principalIdFieldName = ConfigConstant.DEFAULT_PRINCIPAL_ID_FIELD_NAME;

    public QuinnCache(CacheAllService cacheAllService, String prefix, int expire, String principalIdFieldName) {
        if (cacheAllService == null) {
            throw new ParameterShouldNotEmpty()
                    .addParam(CommonMessageEnum.PARAM_SHOULD_NOT_NULL.paramNames[0], "cacheAllService")
                    .exception();
        }

        this.cacheAllService = cacheAllService;

        if (StringUtil.isNotEmpty(prefix)) {
            this.keyPrefix = prefix;
        }

        if (expire > NumberConstant.INT_ZERO) {
            this.expire = expire;
        }

        if (StringUtil.isNotEmpty(principalIdFieldName)) {
            this.principalIdFieldName = principalIdFieldName;
        }
    }

    @Override
    public V get(K key) throws CacheException {
        if (key == null) {
            return null;
        }

        try {
            String cacheKey = getCacheKey(key);
            V v = cacheAllService.get(cacheKey);
            if (v != null) {
                cacheAllService.set(cacheKey, v, expire);
            }
            return v;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        if (key == null) {
            return value;
        }

        try {
            String realKey = getCacheKey(key);
            cacheAllService.set(realKey, value != null ? value : "", expire);
            V result = cacheAllService.get(realKey);
            return result;
        } catch (Exception e) {
            throw new CacheException(e);
        }

    }

    @Override
    public V remove(K key) throws CacheException {
        if (key == null) {
            return null;
        }

        String cacheKey = getCacheKey(key);
        try {
            Object o = cacheAllService.get(cacheKey);
            cacheAllService.delete(cacheKey);
            return (V) o;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }


    @Override
    public void clear() throws CacheException {
        try {
            cacheAllService.clear(this.keyPrefix + "*");
        } catch (Exception e) {
        }
    }

    @Override
    public int size() {
        try {
            cacheAllService.size(this.keyPrefix + "*");
        } catch (Exception e) {
        }
        return 0;
    }

    @Override
    public Set<K> keys() {
        Set<String> keys;
        try {
            keys = cacheAllService.keys(this.keyPrefix + "*");
        } catch (Exception e) {
            return Collections.emptySet();
        }

        if (CollectionUtil.isEmpty(keys)) {
            return Collections.emptySet();
        }

        Set<K> convertedKeys = new HashSet<>();
        for (String key : keys) {
            try {
                convertedKeys.add((K) key);
            } catch (Exception e) {
            }
        }
        return convertedKeys;
    }

    @Override
    public Collection<V> values() {
        return cacheAllService.values(this.keyPrefix + "*");
    }

    /**
     * 生成缓存的Key
     *
     * @param key AuthorizingRealm.getAuthorizationCacheKey 得到的Key
     * @return 存入Redis 的Key (加上前缀)
     */
    private String getCacheKey(K key) {
        if (key == null) {
            return null;
        }
        return this.keyPrefix + getStringKey(key);
    }

    /**
     * 解析原始Key，得到字符串 Key
     *
     * @param key AuthorizingRealm.getAuthorizationCacheKey 得到的Key
     * @return 字符串Key
     */
    private String getStringKey(K key) {
        String realKey;
        DataConverter converter = BaseConverter.getInstance(key.getClass());
        if (converter != null) {
            realKey = converter.toStr(key);
        } else {
            realKey = getKeyFromPrincipalIdField(key);
        }
        return realKey;
    }

    /**
     * AuthorizingRealm.getAuthorizationCacheKey 如果是个复杂对象
     *
     * @param key 复杂对象key
     * @return 根据 PrincipalIdField（权限信息属性）获取字符串Key
     */
    private String getKeyFromPrincipalIdField(Object key) {
        String realKey;
        Object principalObject;
        if (key instanceof PrincipalCollection) {
            principalObject = ((PrincipalCollection) key).getPrimaryPrincipal();
        } else {
            principalObject = key;
        }

        Method principalIdGetter = null;
        Method[] methods = principalObject.getClass().getMethods();

        String methodName = "get" + this.principalIdFieldName.substring(0, 1).toUpperCase()
                + this.principalIdFieldName.substring(1);

        for (Method m : methods) {
            boolean isThisMethod = m.getName().equals(methodName);
            if (isThisMethod) {
                principalIdGetter = m;
                break;
            }
        }

        if (principalIdGetter == null) {
            throw new MethodNotFoundException()
                    .addParam(CommonMessageEnum.METHOD_NOT_FOUND.paramNames[0], methodName)
                    .addParam(CommonMessageEnum.METHOD_NOT_FOUND.paramNames[1],
                            principalObject.getClass().getSimpleName())
                    .exception()
                    ;
        }

        try {
            Object idObj = principalIdGetter.invoke(principalObject);
            if (idObj == null) {
                throw new KeyInfoMissException()
                        .addParam(CommonMessageEnum.KEY_INFO_MISS.paramNames[0], methodName)
                        .addParam(CommonMessageEnum.KEY_INFO_MISS.paramNames[1],
                                principalObject.getClass().getSimpleName())
                        .exception()
                        ;
            }
            realKey = idObj.toString();
        } catch (Exception e) {
            throw new BaseBusinessException(e);
        }

        return realKey;
    }

}