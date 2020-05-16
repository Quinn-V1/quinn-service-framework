package com.quinn.framework.service;

import com.quinn.framework.api.ApplicationSerializer;
import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.util.licence.model.ApplicationInfo;
import com.quinn.util.base.StringUtil;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * 缓存通用功能
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
public class RedisAllServiceImpl extends AbstractRedisService implements CacheAllService {

    public RedisAllServiceImpl(
            RedisTemplate redisTemplate, ApplicationSerializer serializer,
            String name, String nameSpace
    ) {
        super(redisTemplate, serializer, name, nameSpace);
    }

    @Override
    public void set(String key, Object value) {
        doSet(key, value, -1L);
    }

    @Override
    public void set(final String key, final Object value, final long expire) {
        doSet(key, value, expire);
    }

    @Override
    public <T> T get(final String key) {
        return doGet(key);
    }

    @Override
    public <T> T get(final String key, final Class<T> tpl) {
        return doGet(key, tpl);
    }

    @Override
    public int size(String pattern) {
        Set<String> keys = keys(pattern);
        return keys == null ? 0 : keys.size();
    }

    @Override
    public int size() {
        return size("*");
    }

    @Override
    public Set<String> keys(final String pattern) {
        return (Set<String>) redisTemplate.execute((RedisCallback) redisConnection -> {
            Set<byte[]> keysSet = redisConnection.keys(StringUtil.getBytes(wrapperKey(pattern)));
            Set<String> ret1 = new HashSet();
            for (byte[] k : keysSet) {
                ret1.add(cleanKey(StringUtil.forBytes(k)));
            }
            return ret1;
        });
    }

    @Override
    public Set<String> keys() {
        return keys("*");
    }

    @Override
    public <T> Collection<T> values(String pattern) {
        return (Collection<T>) redisTemplate.execute((RedisCallback) redisConnection -> {
            Set<byte[]> keysSet = redisConnection.keys(StringUtil.getBytes(wrapperKey(pattern)));
            List<T> ret1 = new ArrayList(keysSet.size());
            for (byte[] k : keysSet) {
                ret1.add((T) redisConnection.get(k));
            }

            return ret1;
        });
    }

    @Override
    public Collection<Object> values() {
        return values("*");
    }

    @Override
    public boolean exists(final String key) {
        return (boolean) redisTemplate.execute((RedisCallback) connection ->
                connection.exists(StringUtil.getBytes(wrapperKey(key)))
        );
    }

    @Override
    public long getCurrent(String key) {
        String wKey = wrapperKey(key);
        return (Long) redisTemplate.execute((RedisCallback) connection -> {
            byte[] byteKeys = StringUtil.getBytes(wKey);
            long result = connection.incr(byteKeys);
            connection.decr(byteKeys);
            return result;
        });
    }

    @Override
    public long increase(String key) {
        return increaseBy(key, 1);
    }

    @Override
    public long decrease(String key) {
        return decreaseBy(key, 1);
    }

    @Override
    public long increaseBy(String key, int step) {
        return increaseByEx(key, step, 0L);
    }

    @Override
    public long increaseByEx(String key, int step, long expire) {
        String wKey = wrapperKey(key);
        return (Long) redisTemplate.execute((RedisCallback) connection -> {
            byte[] byteKeys = StringUtil.getBytes(wKey);
            long result = connection.incrBy(byteKeys, step);

            if (expire > 0) {
                connection.expire(byteKeys, expire);
            }
            return result;
        });
    }

    @Override
    public long decreaseBy(String key, int step) {
        String wKey = wrapperKey(key);
        return (Long) redisTemplate.execute((RedisCallback) connection -> {
            byte[] byteKeys = StringUtil.getBytes(wKey);
            long result = connection.decrBy(byteKeys, step);
            return result;
        });
    }

    @Override
    public void reset(String key, long val) {
        String wKey = wrapperKey(key);
        redisTemplate.execute((RedisCallback) connection -> {
            byte[] byteKeys = StringUtil.getBytes(wKey);
            connection.del(byteKeys);
            connection.incrBy(byteKeys, val);
            return null;
        });
    }

    @Override
    public void delete(String key) {
        doDelete(key);
    }

    @Override
    public void delete(final String... keys) {
        doDelete(keys);
    }

    @Override
    public void clear(String pattern) {
        redisTemplate.execute((RedisCallback) redisConnection -> {
            Set<byte[]> keysSet = redisConnection.keys(StringUtil.getBytes(wrapperKey(pattern)));
            for (byte[] key : keysSet) {
                redisConnection.del(key);
            }
            return null;
        });
    }

    @Override
    public void clear() {
        redisTemplate.execute((RedisCallback) connection -> {
            connection.flushDb();
            return null;
        });
    }

    @Override
    public boolean lock(String key) {
        return tryLock(key, 0L);
    }

    @Override
    public boolean tryLock(String key, long selfReleaseExpired) {
        String wKey = wrapperKey(key);
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            boolean flag = connection.setNX(StringUtil.getBytes(wKey), StringUtil.getBytes(ApplicationInfo.getAppKey()));
            if (flag && selfReleaseExpired > 0) {
                connection.expire(StringUtil.getBytes(wKey), selfReleaseExpired);
            }
            return flag;
        });
    }

    @Override
    public boolean unLock(String key) {
        delete(key);
        return true;
    }

    @Override
    public String whoHasLock(String key) {
        return get(key);
    }

    @Override
    public boolean lockRemote(String key) {
        // TODO 脚本学习
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            String script = ""
                    + "local rs=redis.call('get',KEYS[1]);"
                    + "if(rs<1) then return 'F';"
                    + "end; "
                    + "redis.call('expire',KEYS[1],tonumber(ARGV[2]))"
                    + ";return 'S';"
                    ;
            String result = connection.eval(script.getBytes(), ReturnType.VALUE, 1, new byte[0]);

            if ("S".equals(result)) {
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean lockLocal(String key) {
        return false;
    }

}
