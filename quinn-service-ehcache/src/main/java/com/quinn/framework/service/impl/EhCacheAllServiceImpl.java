package com.quinn.framework.service.impl;

import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.constant.NumberConstant;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.util.*;
import java.util.regex.Pattern;

/**
 * EhCache 缓存实现类
 *
 * @author Qunhua.Liao
 * @since 2020-05-24
 */
public class EhCacheAllServiceImpl implements CacheAllService {

    /**
     * 缓存名称
     */
    private String name;

    /**
     * 缓存实例
     */
    private Cache cache;

    public EhCacheAllServiceImpl(Cache cache, String name) {
        this.name = name;
        this.cache = cache;
    }

    @Override
    public boolean lock(String key) {
        cache.acquireWriteLockOnKey(key);
        return cache.isWriteLockedByCurrentThread(key);
    }

    @Override
    public boolean tryLock(String key, long selfReleaseExpired) {
        try {
            return cache.tryWriteLockOnKey(key, selfReleaseExpired);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean unLock(String key) {
        cache.releaseWriteLockOnKey(key);
        return true;
    }

    @Override
    public String whoHasLock(String key) {
        return BaseConverter.staticToString(get(key));
    }

    @Override
    public boolean lockRemote(String key) {
        return lock(key);
    }

    @Override
    public boolean lockLocal(String key) {
        return lock(key);
    }

    @Override
    public void set(String key, Object value) {
        cache.put(new Element(key, value));
    }

    @Override
    public void set(String key, Object value, long expire) {
        Element element = new Element(key, value);
        element.setTimeToLive(BaseConverter.staticConvert(expire, Integer.class));
        cache.put(element);
    }

    @Override
    public <T> T get(String key) {
        Element element = cache.get(key);
        if (element == null) {
            return null;
        }
        return (T) element.getObjectValue();
    }

    @Override
    public <T> T get(String key, Class<T> tpl) {
        return get(key);
    }

    @Override
    public Set<String> keys(String pattern) {
        List keys = cache.getKeys();
        if (keys != null) {
            HashSet hashSet = new HashSet();
            for (Object key : keys) {
                if (Pattern.matches(pattern, key.toString())) {
                    hashSet.add(key.toString());
                }
            }
            return hashSet;
        }
        return Collections.emptySet();
    }

    @Override
    public Set<String> keys() {
        List keys = cache.getKeys();
        if (keys != null) {
            return new HashSet<>(keys);
        }
        return Collections.emptySet();
    }

    @Override
    public int size(String pattern) {
        return keys().size();
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public <T> Collection<T> values(String pattern) {
        Map<Object, Element> all = cache.getAll(keys(pattern));
        if (all == null) {
            return Collections.emptyList();
        }

        List<Object> res = new ArrayList<>(all.size());
        for (Map.Entry<Object, Element> entry : all.entrySet()) {
            res.add(entry.getValue().getObjectValue());
        }

        return (Collection<T>) res;
    }

    @Override
    public <T> Collection<T> values() {
        Map<Object, Element> all = cache.getAll(keys());
        if (all == null) {
            return Collections.emptyList();
        }

        List<Object> res = new ArrayList<>(all.size());
        for (Map.Entry<Object, Element> entry : all.entrySet()) {
            res.add(entry.getValue().getObjectValue());
        }

        return (Collection<T>) res;
    }

    @Override
    public boolean exists(String key) {
        return cache.isKeyInCache(key);
    }

    @Override
    public long getCurrent(String key) {
        Long curr = get(key);
        if (curr == null) {
            cache.put(new Element(key, NumberConstant.LONG_ZERO));
            return NumberConstant.LONG_ZERO;
        }
        return curr.longValue();
    }

    @Override
    public long increase(String key) {
        Long curr = get(key);
        if (curr == null) {
            cache.put(new Element(key, NumberConstant.LONG_ONE));
            return NumberConstant.LONG_ZERO;
        }

        cache.put(new Element(key, curr + NumberConstant.LONG_ONE));
        return curr.longValue();
    }

    @Override
    public long decrease(String key) {
        Long curr = get(key);
        if (curr == null) {
            cache.put(new Element(key, NumberConstant.LONG_ONE_NEGATIVE));
            return NumberConstant.LONG_ZERO;
        }

        cache.put(new Element(key, curr + NumberConstant.LONG_ONE_NEGATIVE));
        return curr.longValue();
    }

    @Override
    public long increaseBy(String key, int step) {
        Long curr = get(key);
        if (curr == null) {
            cache.put(new Element(key, step * NumberConstant.LONG_ONE));
            return NumberConstant.LONG_ZERO;
        }

        cache.put(new Element(key, curr + step));
        return curr.longValue();
    }

    @Override
    public long increaseByEx(String key, int step, long expire) {
        Long curr = get(key);
        if (curr == null) {
            Element element = new Element(key, step * NumberConstant.LONG_ONE);
            element.setTimeToLive(BaseConverter.staticConvert(expire, Integer.class));
            cache.put(element);
            return NumberConstant.LONG_ZERO;
        }

        Element element = new Element(key, curr + step);
        element.setTimeToLive(BaseConverter.staticConvert(expire, Integer.class));
        cache.put(element);
        return curr.longValue();
    }

    @Override
    public long decreaseBy(String key, int step) {
        Long curr = get(key);
        if (curr == null) {
            cache.put(new Element(key, step * NumberConstant.LONG_ONE_NEGATIVE));
            return NumberConstant.LONG_ZERO;
        }

        cache.put(new Element(key, curr - step));
        return curr.longValue();
    }

    @Override
    public void reset(String key, long val) {
        cache.put(new Element(key, val * NumberConstant.LONG_ONE_NEGATIVE));
    }

    @Override
    public void delete(String key) {
        cache.remove(key);
    }

    @Override
    public void delete(String... keys) {
        cache.removeAll(Arrays.asList(keys));
    }

    @Override
    public void clear(String pattern) {
        Set<String> keys = keys(pattern);
        cache.removeAll(keys);
    }

    @Override
    public void clear() {
        cache.removeAll();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void expire(String key, long expire) {
        Element element = cache.get(key);
        element.setTimeToLive(BaseConverter.staticConvert(element, Integer.class));
        cache.put(element);
    }
}
