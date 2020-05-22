package com.quinn.framework.component;

import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.DataConverter;
import com.quinn.util.base.convertor.BaseConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class QuinnCache<K, V> implements Cache<K, V> {

    private CacheAllService cacheAllService;

    private String keyPrefix = "";

    private int expire = 0;

    private String principalIdFieldName = "userKey";

    public QuinnCache(CacheAllService cacheAllService, String prefix, int expire, String principalIdFieldName) {
        if (cacheAllService == null) {
            throw new IllegalArgumentException("cacheBaseService cannot be null.");
        }
        this.cacheAllService = cacheAllService;

        if (prefix != null && !"".equals(prefix)) {
            this.keyPrefix = prefix;
        }

        if (expire != -1) {
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

        if (CollectionUtils.isEmpty(keys)) {
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

    private String getCacheKey(K key) {
        if (key == null) {
            return null;
        }
        return this.keyPrefix + getStringKey(key);
    }

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

    private String getKeyFromPrincipalIdField(Object key) {
        String realKey;
        Object principalObject = null;
        if (key instanceof PrincipalCollection) {
            principalObject = ((PrincipalCollection) key).getPrimaryPrincipal();
        } else {
            principalObject = key;
        }

        Method pincipalIdGetter = null;
        Method[] methods = principalObject.getClass().getMethods();
        for (Method m : methods) {
            boolean isThisMethod = m.getName().equals("get" + this.principalIdFieldName.substring(0, 1).toUpperCase() + this.principalIdFieldName.substring(1));
            if (isThisMethod) {
                pincipalIdGetter = m;
                break;
            }
        }
        if (pincipalIdGetter == null) {
            // FIXME
            throw new RuntimeException("");
        }

        try {
            Object idObj = pincipalIdGetter.invoke(principalObject);
            if (idObj == null) {
                // FIXME
                throw new RuntimeException("");
            }
            realKey = idObj.toString();
        } catch (IllegalAccessException e) {
            // FIXME
            throw new RuntimeException("");
        } catch (InvocationTargetException e) {
            // FIXME
            throw new RuntimeException("");
        }

        return realKey;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getPrincipalIdFieldName() {
        return principalIdFieldName;
    }

    public void setPrincipalIdFieldName(String principalIdFieldName) {
        this.principalIdFieldName = principalIdFieldName;
    }
}